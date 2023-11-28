package com.nhnacademy.aiot.node.Impl;

import java.util.List;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import com.nhnacademy.aiot.node.InNode;
import com.nhnacademy.aiot.node.Info;
import com.nhnacademy.aiot.pipe.Pipe;
import com.nhnacademy.aiot.setting.Setting;

public class MQTTinNode implements InNode, Runnable {



    private final Thread thread;
    private final List<Pipe> outpipes;
    IMqttClient subscriber;

    private final Info info;
    private final Pipe[] outPipe;
    private final Setting setting;
    private final String host;

    private MQTTinNode(Setting setting, String host) {
        this.setting = setting;
        this.host = host;

        subscriber = new MqttClient("tcp://ems.nhnacademy.com:1883", "myID");
        thread = new Thread(this);
    }

    private static MQTTinNode instance = new MQTTinNode(setting, host);

    public static MQTTinNode getInstance() {
        return instance;
    }

    @Override
    public void connectOutPipe(Pipe inPipe) {

    }

    @Override
    public Info getInfo() {
        // TODO Auto-generated method stub
    }

    @Override
    public void start() {
        thread.run();
    }

    @Override
    public void run() {}
}
