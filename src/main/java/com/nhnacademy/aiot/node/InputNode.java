package com.nhnacademy.aiot.node;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;
import com.nhnacademy.aiot.port.PortType;

/**
 * 이 클래스는 여러개의 출력 포트를 가지는 노드 클래스입니다.
 * <p>
 * 노드와 노드를 연결하기위한 메서드를 지원합니다.
 * <p>
 * 노드구성중 출력만을 가지는 노드를 구성할 수 있습니다.
 */
public abstract class InputNode extends Node {

    private final Map<Integer, Port> outPorts;

    protected InputNode(int totalOutputPorts) {
        outPorts = new HashMap<>();
        IntStream.range(0, totalOutputPorts)
                .forEach(i -> outPorts.put(i, new Port(PortType.OUT)));
    }

    /**
     * 지정된 포트에 출력 파이프를 연결하는 메서드입니다.
     * 
     * @param portNum 연결할 출력 포트 번호
     * @param pipe    연결할 출력 파이프
     */
    public void connectOutPort(int portNum, Pipe pipe) {
        // 이 노드의 portNumber번의 outPort에 잇는 pipe와 연결
        outPorts.get(portNum).add(pipe);
    }

    /**
     * 지정된 포트로 패킷을 전송하는 메서드입니다.
     *
     * @param portNum 전송할 포트 번호
     * @param packet  전송할 패킷
     */
    protected void send(int portNum, Packet packet) {
        if (outPorts.containsKey(portNum)) {
            Port ouPort = outPorts.get(portNum);
            ouPort.put(packet);
        }
    }
}
