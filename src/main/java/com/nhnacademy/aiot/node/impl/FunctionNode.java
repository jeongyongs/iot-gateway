package com.nhnacademy.aiot.node.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import com.nhnacademy.aiot.node.InOutPutNode;
import com.nhnacademy.aiot.port.Packet;

/**
 * 받은 페킷을 전달 받은 요구사항에 맞게 재구성하여
 * 다음 노드로 페킷을 전달 합니다.
 */
public class FunctionNode extends InOutPutNode {
    private final Function<Packet, List<Packet>> function;

    /**
     * @param function 페킷 구성 요구 사항
     */
    public FunctionNode(Function<Packet, List<Packet>> function) {
        super();
        this.function = function;
    }

    @Override
    protected void process() {
        Packet packet = receive();
        List<Packet> packetList = function.apply(packet);
        IntStream.range(0, packetList.size())
                .forEach(i -> send(i, packetList.get(i)));
    }
}
