package com.nhnacademy.aiot.node;

import java.util.Map;
import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;

public abstract class InOutputNode extends Node {
    // port(pipe) > pipe_map, port_
    // inports / send_pipe
    // outports / receive_pipe
    // connect
    private Port inPorts;
    private Map<Integer, Port> outPorts;

    protected InOutputNode() {
        super();
    }

    /**
     * port 존재여부 확인 후,
     * <p>
     * node에서 나가는 port번호에 pipe와 연결
     * 
     * @param portNum
     * @param pipe
     */
    public void connectOutPort(int portNum, Pipe pipe) {
        if (inPorts != null) {
            outPorts.get(portNum).add(pipe);
        }
    }

    /**
     * Overroding
     * <p>
     * portNum 입력 X > 자동 0번
     */
    public void connectOutPort(Pipe pipe) {
        // outPorts.get(0).add(pipe);
        connectOutPort(0, pipe);
    }

    /**
     * port가 1개니까 들어오는 pipe만
     * 
     * @param pipe
     */
    public void connectInPort(Pipe pipe) {
        inPorts.add(pipe);
    }

    /**
     * outports의 pipe number를 지정해줘서 packet 전송
     * <p>
     * portNum 입력 X > 자동 0번
     * 
     * @param pipeNum
     * @param packet
     */
    protected void send(int portNum, Packet packet) {
        outPorts.get(portNum).put(packet);
    }

    /**
     * portNum이 입력되지 않은 경우, 0으로 주고 packet 전송
     * 
     * @param packet
     */
    protected void send(Packet packet) {
        send(0, packet);
    }

    /**
     * port에 담긴 packet을 pipe에 넣기
     * 
     * @return
     */
    protected Packet receive() {
        return inPorts.take();
    }
}
