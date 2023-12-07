package com.nhnacademy.aiot.node.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import com.nhnacademy.aiot.node.InOutPutNode;
import com.nhnacademy.aiot.node.NodeProperty;
import com.nhnacademy.aiot.port.Packet;

/**
 * 받은 페킷을 전달 받은 요구사항에 맞게 재구성하여
 * 다음 노드로 페킷을 전달 합니다.
 */
public class FunctionNode extends InOutPutNode {

    private static final String FUNC = "func";
    private static final String TOTAL_OUT_PORTS = "totalOutProts";

    private final Function<Packet, List<Packet>> function;

    /**
     * @param nodeProperty JSONObject 입력된 페킷 구성을 가져옵니다.
     */
    @SuppressWarnings("unchecked")
    public FunctionNode(NodeProperty nodeProperty) {
        super(nodeProperty.getInt(TOTAL_OUT_PORTS));
        function = (Function<Packet, List<Packet>>) nodeProperty.get(FUNC);
    }

    @Override
    protected void process() {
        // 페킷을 inPort에서 꺼내옵니다.
        Packet packet = receive();
        countReceivedPacket();

        // 새로운 페킷을 만듭니다.
        List<Packet> packetList = function.apply(packet);

        // 새롭게 만들어진 페킷을 올바른 outPort로 보넵니다.
        IntStream.range(0, packetList.size())
                .forEach(i -> send(i, packetList.get(i)));
        countSentPacket();
    }
}
