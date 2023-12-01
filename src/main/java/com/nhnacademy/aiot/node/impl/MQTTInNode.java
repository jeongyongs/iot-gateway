package com.nhnacademy.aiot.node.impl;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.nhnacademy.aiot.node.InputNode;
import com.nhnacademy.aiot.port.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTInNode extends InputNode {

    private static final String ERROR = "에러";
    private static final String SUBSCRIBER_ID = UUID.randomUUID().toString();

    private final String connectMessage = "subscriber connect in MQTT Broker";
    private final String errorMessage = "MqttException Occurrence!!";
    private MqttClient subscriber;
    private final String applicationName;

    public MQTTInNode(String brokerId, String applicationName) {

        this.applicationName = applicationName;
        try {
            subscriber = new MqttClient(brokerId, SUBSCRIBER_ID);
            subscriber.connect();
            log.info(connectMessage);

        } catch (MqttException e) {
            log.error(errorMessage, e);
        }
    }

    @Override
    protected void process() {
        try {
            Packet packet = new Packet();
            subscriber.subscribe(applicationName, (topic, msg) -> {
                packet.append("topic", topic);
                packet.append("payload", msg.getPayload());
            });
            send(packet);
        } catch (MqttException e) {
            log.error(ERROR, e);
        }
    }
}
