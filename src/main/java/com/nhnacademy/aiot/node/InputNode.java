package com.nhnacademy.aiot.node;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;
import com.nhnacademy.aiot.port.PortType;

public abstract class InputNode extends Node {

    private final Map<Integer, Port> outPorts;

    protected InputNode(int totalOutputPorts) {
        outPorts = new HashMap<>();
        IntStream.range(0, totalOutputPorts).forEach(i -> outPorts.put(i, new Port(PortType.OUT)));
    }

    // 포트 넘버 지정
    public void connectOut(int portNum, Pipe pipe) {
        outPorts.get(portNum).add(pipe);
    }

    // 포트 넘버 지정
    protected void send(int portNum, Packet packet) {
        if (outPorts.containsKey(portNum)) {
            Port outPort = outPorts.get(portNum);
            outPort.put(packet);
        }
    }
}
