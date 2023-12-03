package com.nhnacademy.aiot.node.impl;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import com.nhnacademy.aiot.node.InputNode;
import com.nhnacademy.aiot.node.NodeProperty;
import com.nhnacademy.aiot.port.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTInNode extends InputNode {

    private static final int DEFAULT_PORT = 0;
    private static final int TOTAL_OUT_PORT = 1;
    private static final String BROKER_FORMAT = "tcp://%s:%d";
    private static final String PORT = "port";
    private static final String BROKER = "broker";
    private static final String APPLICATION_NAME = "applicationName";
    private static final String TOPIC = "topic";
    private static final String PAYLOAD = "payload";
    private static final String ERROR = "에러";
    private static final String CONNECT_MESSAGE = "subscriber connect in MQTT Broker";
    private static final String ERROR_MESSAGE = "MqttException Occurrence!!";

    private MqttClient subscriber;
    private final String applicationName;

    public MQTTInNode(NodeProperty nodeProperty) {
        super(TOTAL_OUT_PORT);
        this.applicationName = nodeProperty.getString(APPLICATION_NAME);
        try {
            subscriber = new MqttClient(String.format(BROKER_FORMAT, nodeProperty.getString(BROKER),
                    nodeProperty.getInt(PORT)), UUID.randomUUID().toString());
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
                packet.put(TOPIC, topic);
                packet.put(PAYLOAD, new JSONObject(new String(msg.getPayload())));
                send(DEFAULT_PORT, packet);
            });
        } catch (MqttException e) {
            log.error(ERROR, e);
        }
    }
}
