package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;

import java.util.HashMap;
import java.util.Map;

/**
 * InOutPutNode는 입력 및 출력을 처리하는 노드의 추상 클래스입니다.
 */
public abstract class InOutPutNode extends Node {
    private final Port inPorts;
    private final Map<Integer, Port> outPorts;

    protected InOutPutNode() {
        super();
        inPorts = new Port();
        outPorts = new HashMap<>();
    }

    /**
     * 입력을 받아오는 메서드입니다.
     * 
     * @return 입력으로 받아온 패킷
     */
    protected Packet receive() {
        return inPorts.take();
    }

    /**
     * 지정된 포트로 패킷을 전송하는 메서드입니다.
     *
     * @param portNum 전송할 포트 번호
     * @param packet  전송할 패킷
     */
    protected void send(int portNum, Packet packet) {
        if (outPorts.containsKey(portNum)) {
            Port outPort = outPorts.get(portNum);
            outPort.put(packet);
        }
    }

    /**
     * 입력 파이프를 연결하는 메서드입니다.
     * 
     * @param pipe 연결할 입력 파이프
     */
    public void connectInPipe(Pipe pipe) {
        inPorts.add(pipe);
    }

    /**
     * 지정된 포트에 출력 파이프를 연결하는 메서드입니다.
     * 
     * @param portNum 연결할 출력 포트 번호
     * @param pipe    연결할 출력 파이프
     */
    public void connectOutPipe(int portNum, Pipe pipe) {
        Port outPort = outPorts.get(portNum);
        outPort.add(pipe);
    }
}
