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

    private static final boolean RETAINED = false;
    private static final int QOS = 1;
    private static final String WILL_TOPIC = "gateway/";
    private static final String WILL_PAYLOAD = "Disconnected";
    private static final String BROKER_FORMAT = "tcp://%s:%d";
    private static final String CONNECT_MESSAGE = "connect";
    private static final String PAYLOAD = "payload";
    private static final String TOPIC = "topic";
    private static final String PORT = "port";
    private static final String BROKER = "broker";

    private MqttClient client;

    /**
     * broker connect 및 option 설정
     */
    public MQTTOutNode(NodeProperty nodeProperty) {
        try {
            client = new MqttClient(String.format(BROKER_FORMAT, nodeProperty.getString(BROKER),
                    nodeProperty.getInt(PORT)), UUID.randomUUID().toString());
            log.info(CONNECT_MESSAGE);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setWill(WILL_TOPIC, WILL_PAYLOAD.getBytes(), QOS, RETAINED);

            client.connect(options);

        } catch (MqttException e) {
            log.error(e.getMessage(), e);
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
            String pTopic = packet.get(TOPIC).toString();
            String pMsg = packet.get(PAYLOAD).toString();

            MqttMessage stringPacket = new MqttMessage();
            stringPacket.setPayload(pMsg.getBytes());
            client.publish(pTopic, stringPacket);
            countSentPacket();

        } catch (MqttException e) {
            countFailedPacket();
            log.error(e.getMessage(), e);
        }
    }
}
