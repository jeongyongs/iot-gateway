package com.nhnacademy.aiot.node.impl;

import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.nhnacademy.aiot.node.Info;
import com.nhnacademy.aiot.node.Outputable;
import com.nhnacademy.aiot.pipe.Pipe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTOutNode implements Outputable, Runnable {
    private static MQTTOutNode mqttOutNode;
    private static ArrayList<Pipe> pipeList = new ArrayList<>();

    private MQTTOutNode() {
        super();
    }

    public static MQTTOutNode getMqttOutNode() {
        if (mqttOutNode == null) {
            mqttOutNode = new MQTTOutNode();
        }
        return mqttOutNode;
    }

    // info (increase _recieve, fail)
    // try-catch 발생 -> fail increase
    @Override
    public void connectInPipe(Pipe outPipe) {
        for (Pipe pipeList : outPipe) {
            if (pipeList.put != null) {
                return Info.increase;
            }

        }
    }

    @Override
    public void run() {
        // packet 받아와서
        try (IMqttClient client = new MqttClient("tcp://localhost:1883", "myId")) {
            log.info("connect");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setWill("test/will", "Disconnected".getBytes(), 1, true);
            client.connect(options);

            client.publish(getName(), null); // packet 보내기

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }

            client.disconnect();
            log.info("disconenct");
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("connect fail");
        }
    }

    // publish
    public void publish() {
        Thread.start();
    }
}

