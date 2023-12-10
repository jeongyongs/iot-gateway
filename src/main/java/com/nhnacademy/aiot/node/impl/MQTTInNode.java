package com.nhnacademy.aiot.node.impl;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

import com.nhnacademy.aiot.node.InputNode;
import com.nhnacademy.aiot.node.NodeProperty;
import com.nhnacademy.aiot.port.Packet;
import lombok.extern.slf4j.Slf4j;

/**
 * MQTTInNode는 MQTT 라이브러리를 사용하여 MQTT 메시지를 수신하는 노드입니다.
 */
@Slf4j
public class MQTTInNode extends InputNode {

    private static final int DEFAULT_PORT = 0;
    private static final int TOTAL_OUT_PORTS = 1;
    private static final String BROKER_FORMAT = "tcp://%s:%d";
    private static final String PORT = "port";
    private static final String BROKER = "broker";
    private static final String APPLICATION_NAME = "an";
    private static final String TOPIC = "topic";
    private static final String PAYLOAD = "payload";
    private static final String ERROR = "에러";
    private static final String CONNECT_MESSAGE = "subscriber connect in MQTT Broker";
    private static final String ERROR_MESSAGE = "MqttException Occurrence!!";

    private MqttClient subscriber;
    private final String applicationName;

    /**
     * MQTTInNode의 생성자입니다.
     * 
     * @param nodeProperty 연결정보를 NodeProperty를 통해 받습니다.
     */

    public MQTTInNode(NodeProperty nodeProperty) {
        super(TOTAL_OUT_PORTS);
        this.applicationName = nodeProperty.getString(APPLICATION_NAME);
        try {
            subscriber = new MqttClient(
                    String.format(BROKER_FORMAT,
                            nodeProperty.getJSONObject(BROKER).getString(BROKER),
                            nodeProperty.getJSONObject(BROKER).getInt(PORT)),
                    UUID.randomUUID().toString());
            subscriber.connect();
            log.info(CONNECT_MESSAGE);

        } catch (MqttException e) {
            log.error(ERROR_MESSAGE, e);
        }
    }


    @Override
    public void run() {
        preprocess();
        process();
        try {
            pause();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // pause() 라는 메서드를 사용하는 이유는 subscriber 동작하면 스레드로 동작하고
    // 여러 스레드가 생성되어 무수이 많은 subscriber가 생겨 문제가 발생하기 때문
    private synchronized void pause() throws InterruptedException {
        while (!Thread.interrupted()) {
            wait();
        }
    }

    /**
     * MQTT 메시지를 처리하는 메서드입니다.
     * <p>
     * 지정된 토픽에 대한 구독을 시작하고, 메시지를 Packet에 담아 출력 포트로 전송합니다.
     */
    @Override
    protected void process() {
        try {
            subscriber.subscribe(applicationName, (topic, msg) -> {
                Packet packet = new Packet();
                packet.put(TOPIC, topic);
                packet.put(PAYLOAD, new JSONObject(new String(msg.getPayload())));
                send(DEFAULT_PORT, packet);
            });
        } catch (MqttException e) {
            log.error(ERROR, e);
        }
    }
}
