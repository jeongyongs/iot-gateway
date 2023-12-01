package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;
import com.nhnacademy.aiot.port.PortType;

public abstract class InputNode extends Node {

    private final Port outPorts = new Port(PortType.OUT);

    // 포트 넘버 지정
    public void connectOutPort(Pipe pipe) {
        // 이 노드의 portNumber번의 outPort에 잇는 pipe와 연결

        outPorts.add(pipe);
    }

    // 포트 넘버 지정
    protected void send(Packet packet) {
        outPorts.put(packet);
    }
}
