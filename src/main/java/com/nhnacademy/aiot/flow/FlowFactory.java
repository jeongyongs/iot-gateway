package com.nhnacademy.aiot.flow;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.nhnacademy.aiot.node.impl.FunctionNode;
import com.nhnacademy.aiot.node.impl.MQTTInNode;
import com.nhnacademy.aiot.node.impl.MQTTOutNode;
import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.setting.Setting;

public class FlowFactory {

    private static AtomicInteger count = new AtomicInteger();

    private FlowFactory() {}

    public static Flow getInstance(Setting setting) {
        Flow flow = new Flow();

        Pipe p1 = new Pipe();
        Pipe p2 = new Pipe();

        MQTTInNode mqttInNode = new MQTTInNode("tcp://ems.nhnacademy.com:1883", "#");
        mqttInNode.connectOutPort(p1);
        flow.add("aaa", mqttInNode);

        FunctionNode functionNode = new FunctionNode(1, p -> {
            p.put("topic", "test/p");
            p.put("payload", count.incrementAndGet());
            System.out.println(p);

            return List.of(p);
        });
        flow.add("bbb", functionNode);
        functionNode.connectInPipe(p1);
        functionNode.connectOutPipe(0, p2);

        MQTTOutNode mqttOutNode = new MQTTOutNode();
        mqttOutNode.connectInPort(p2);
        flow.add("ccc", mqttOutNode);

        return flow;
    }

}
