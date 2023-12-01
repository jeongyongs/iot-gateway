package com.nhnacademy.aiot.node.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import com.nhnacademy.aiot.node.InOutPutNode;
import com.nhnacademy.aiot.port.Packet;

/**
 * 받은 페킷을 전달 받은 요구사항에 맞게 재구성하여 다음 노드로 페킷을 전달 합니다.
 */
public class FunctionNode extends InOutPutNode {
    private final Function<Packet, List<Packet>> function;

    /**
     * @param function 페킷 구성 요구 사항
     */
    public FunctionNode(int portNum, Function<Packet, List<Packet>> function) {
        super(portNum);
        this.function = function;
    }

    @Override
    protected void process() {
        // 페킷을 inPort에서 꺼내옵니다.
        Packet packet = receive();
        countReceivedPacket();

        // 새로운 페킷을 만듭니다.
        List<Packet> packetList = function.apply(packet);

        // 새롭게 만들어진 페킷을 올바른 outPort로 보넵니다.
        IntStream.range(0, packetList.size()).forEach(i -> send(i, packetList.get(i)));
        countSentPacket();
    }
}
