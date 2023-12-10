package com.nhnacademy.aiot.node.impl;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.nhnacademy.aiot.node.NodeProperty;
import com.nhnacademy.aiot.node.OutputNode;
import com.nhnacademy.aiot.port.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTOutNode extends OutputNode {
    private MqttClient client;

    private static final String BROKER = "broker";
    private static final String BROKER_FORMAT = "tcp://%s:%d";
    private static final String PORT = "port";
    private static final String WILL_TOPIC = "connect/will";
    private static final byte[] WILL_MESSAGE = "Disconnected".getBytes();
    private static final int QOS = 1;

    /**
     * broker connect 및 option 설정
     */
    public MQTTOutNode(NodeProperty nodeProperty) {
        try {
            client = new MqttClient(String.format(BROKER_FORMAT, nodeProperty.getJSONObject(BROKER).getString(BROKER), nodeProperty.getJSONObject(BROKER).getInt(PORT)),
                                        UUID.randomUUID().toString());
            log.info("MqttClient connect_publish");

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setWill(WILL_TOPIC, WILL_MESSAGE, QOS, true);

            client.connect(options);
        } catch (MqttException e) {
            log.info("MQTTClient_pub_ connect fail");
        }
    }

    /**
     * JSONObject로 되어있는 packet을 꺼내와서 String 형식으로 변환
     * <p>
     * thread로 동작
     */
    @Override
    protected void process() {
        try {
            Packet packet = receive();
            String pTopic = packet.getString("topic");
            String pMsg = packet.getString("payload");

            MqttMessage stringPacket = new MqttMessage();
            stringPacket.setPayload(pMsg.getBytes());
            client.publish(pTopic, stringPacket);
            countSentPacket();

            // 연결 종료
            client.disconnect();
            log.info("MqttClient disconnect_publish");
        } catch (MqttException e) {
            countFailedPacket();
            e.printStackTrace();
        }
    }

}
