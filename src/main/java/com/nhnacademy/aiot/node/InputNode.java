package com.nhnacademy.aiot.node;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;

public abstract class InputNode extends Node {

    private Map<Integer, Port> outPorts;
    private Port inPort;

    protected void connectOutPort(int portNumber, Pipe pipe) {

        // 이 노드의 portNumber번의 outPort에 잇는 pipe와 연결
        outPorts.get(portNumber).add(pipe);

    }

    protected void connectInputPort(Pipe pipe) {

        // 이 노드의 inputPort예 잇는 pipe와 연결
        inPort.add(pipe);
    }

    protected void send(int portNumber, Packet packet) {
        outPorts.get(portNumber).put(packet);
    }

    protected Packet receive() {
        return inPort.get();
    }


    // port class
    // public boolean put(Packet packet) {

    // for (Pipe Pipe : Pipes) {
    // sendPipe.put(packet);
    // }

    // return true;

    // }
}


