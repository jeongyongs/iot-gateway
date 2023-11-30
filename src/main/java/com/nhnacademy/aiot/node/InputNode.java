package com.nhnacademy.aiot.node;

import java.util.Map;
import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;

public abstract class InputNode extends Node {

    protected Map<Integer, Port> outPorts;
    protected Port inPort;

    // 디폴트 포트넘버 0
    public void connectOutPort(Pipe pipe) {
        outPorts.get(0).add(pipe);
    }

    // 포트 넘버 지정
    public void connectOutPort(int portNumber, Pipe pipe) {
        // 이 노드의 portNumber번의 outPort에 잇는 pipe와 연결
        outPorts.get(portNumber).add(pipe);
    }

    public void connectInputPort(Pipe pipe) {
        // 이 노드의 inputPort예 잇는 pipe와 연결
        inPort.add(pipe);
    }

    // 디폴트 포트넘버 0
    protected void send(Packet packet) {
        outPorts.get(0).put(packet);
    }

    // 포트 넘버 지정
    protected void send(int portNumber, Packet packet) {
        outPorts.get(portNumber).put(packet);
    }

    protected Packet receive() {
        return inPort.take();
    }

}
