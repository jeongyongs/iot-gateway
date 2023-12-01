package com.nhnacademy.aiot.node.impl;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.nhnacademy.aiot.node.InputNode;
import com.nhnacademy.aiot.port.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTInNode extends InputNode {

    private static final String TOPIC = "topic";
    private static final String PAYLOAD = "payload";
    private static final String ERROR = "에러";
    private static final String CONNECT_MESSAGE = "subscriber connect in MQTT Broker";
    private static final String ERROR_MESSAGE = "MqttException Occurrence!!";

    private MqttClient subscriber;
    private final String applicationName;
    private final String subscriberId;

    public MQTTInNode(String brokerId, String applicationName) {
        subscriberId = UUID.randomUUID().toString();
        this.applicationName = applicationName;
        try {
            subscriber = new MqttClient(brokerId, subscriberId);
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
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
        postprocess();
    }

    private synchronized void pause() throws InterruptedException {
        while (!Thread.interrupted()) {
            wait();
        }
    }

    @Override
    protected void process() {
        try {
            subscriber.subscribe(applicationName, (topic, msg) -> {
                Packet packet = new Packet();
                packet.append(TOPIC, topic);
                packet.append(PAYLOAD, msg.getPayload());
                send(packet);
            });
        } catch (MqttException e) {
            log.error(ERROR, e);
        }
    }
}
