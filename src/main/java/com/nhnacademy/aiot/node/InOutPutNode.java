package com.nhnacademy.aiot.node;

import java.util.Map;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Port;

public abstract class InOutPutNode extends Node {
    private Port inPorts;
    private Map<Integer, Port> outPorts;

    InOutPutNode(int outPort) {
        
    }

    InOutPutNode() {
        super();
    }

    protected void recieve() {
        
    }

    protected void send(int inPort, Port port) {
        inPorts = port.
    }
}
