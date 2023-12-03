package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;
import com.nhnacademy.aiot.port.PortType;

public abstract class OutputNode extends Node {

    private Port inPort = new Port(PortType.IN);

    /**
     * port 존재여부 확인 후,
     * <p>
     * node에서 나가는 port번호에 pipe와 연결
     * 
     * @param portNum
     * @param pipe
     */
    public void connectIn(Pipe pipe) {
        inPort.add(pipe);
    }

    /**
     * port에 담긴 packet을 pipe에 넣기
     * 
     * @return
     */
    protected Packet receive() {
        return inPort.take();
    }
}
