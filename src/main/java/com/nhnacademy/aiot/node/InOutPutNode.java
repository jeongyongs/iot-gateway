package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;

import java.util.Map;
import java.util.Optional;

public abstract class InOutPutNode extends Node {
    private Port inPort;
    private Map<Integer, Port> outPorts;

    protected Optional<Packet> receive() {
        return inPort.take().or(Optional::empty);
    }

    protected void send(int portNum, Packet packet) {
        Port outPort = outPorts.get(portNum);
        if (outPort != null) {
            outPort.put(packet);
        }
    }

    public void connectInPipe(Pipe pipe) {
        inPort.add(pipe);
    }

    public void connectOutPipe(int portNum, Pipe pipe) {
        Port outPort = outPorts.get(portNum);
        if (outPort != null) {
            outPort.add(pipe);
        }
    }
}
