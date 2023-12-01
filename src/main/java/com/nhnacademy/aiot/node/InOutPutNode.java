package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 이 클래스는 여러 개의 출력 포트를 추가할 수 있는 입력-출력 노드를 나타냅니다.
 * <p>
 * 생성자에 아무 인자도 제공되지 않을 경우, 노드는 하나의 입력 포트와 하나의 출력 포트(인덱스 0)로 초기화됩니다. 사용자는 필요에 따라 추가적인 출력 포트를 추가할 수
 * 있습니다.
 */
public abstract class InOutPutNode extends Node {
    private final Port inPort;
    private final Map<Integer, Port> outPorts;
    private static final int DEFAULT_PORTNUM = 0;

    /**
     * 하나의 입력 포트와 하나의 출력 포트(인덱스 0)로 노드를 초기화하는 기본 생성자입니다.
     */
    protected InOutPutNode() {
        this(DEFAULT_PORTNUM);
    }

    /**
     * 출력 포트의 총 개수를 지정할 수 있는 생성자입니다. 기본 생성자를 기반으로 하여 지정된 개수만큼 출력 포트를 추가합니다.
     *
     * @param totalOutputPorts 추가할 총 출력 포트의 개수입니다.
     */
    protected InOutPutNode(int totalOutputPorts) {
        super();
        inPort = new Port(PortType.IN);
        outPorts = new HashMap<>();
        IntStream.range(0, totalOutputPorts)
                .forEach(i -> outPorts.put(i, new Port(PortType.OUT)));
    }

    /**
     * 입력을 받아오는 메서드입니다.
     * 
     * @return 입력으로 받아온 패킷
     */
    protected Packet receive() {
        return inPort.take();
    }

    /**
     * 지정된 포트로 패킷을 전송하는 메서드입니다.
     *
     * @param portNum 전송할 포트 번호
     * @param packet 전송할 패킷
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
        inPort.add(pipe);
    }

    /**
     * 지정된 포트에 출력 파이프를 연결하는 메서드입니다.
     * 
     * @param portNum 연결할 출력 포트 번호
     * @param pipe 연결할 출력 파이프
     */
    public void connectOutPipe(int portNum, Pipe pipe) {
        outPorts.get(portNum).add(pipe);
    }
}
