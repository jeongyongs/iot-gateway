package com.nhnacademy.aiot.node;

import java.util.HashMap;
import java.util.Map;
import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;
import com.nhnacademy.aiot.port.PortType;

public abstract class InputNode extends Node {

    private static final int DEFAULT_PORTNUM = 0;
    private Map<Integer, Port> outPorts = new HashMap<>();
    private Port inPort = new Port(PortType.IN);


    // 디폴트 포트넘버 0
    public void connectOutPort(Pipe pipe) {
        connectOutPort(DEFAULT_PORTNUM, pipe);
    }

    // 포트 넘버 지정
    public void connectOutPort(int portNumber, Pipe pipe) {
        // 이 노드의 portNumber번의 outPort에 잇는 pipe와 연결
        outPorts.get(portNumber).add(pipe);
    }

    // 디폴트 포트넘버 0
    protected void send(Packet packet) {
        send(DEFAULT_PORTNUM, packet);
    }

    // 포트 넘버 지정
    protected void send(int portNumber, Packet packet) {
        outPorts.get(portNumber).put(packet);
    }

}
