package com.nhnacademy.aiot.node;

import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.nhnacademy.aiot.node.impl.Info;
import com.nhnacademy.aiot.node.impl.OutNode;
import com.nhnacademy.aiot.pipe.Pipe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTOutNode extends Thread {
    private static MQTTOutNode mqttOutNode;
    private static Info info;
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
    void connectInPipe() {
        for (Pipe pipe : pipeList) {
            if (pipe.put())
                Pipe pipes[] = new pipe.take();
            }
    }

    // publish
    public void publish() {
        // thread 구현
        run() {
        //packet 받아와서
            try(IMqttClient client = new MqttClient("tcp://localhost:1883", "myId")) {
                log.info("connect");
                MqttConnectOptions options = new MqttConnectOptions();
                options.setWill("test/will", "Disconnected".getBytes(), 1, true);
                client.connect(options);

                client.publish(getName(), null);  //packet 보내기

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
    }

}
