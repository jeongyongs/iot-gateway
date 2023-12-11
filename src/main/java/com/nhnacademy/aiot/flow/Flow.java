package com.nhnacademy.aiot.flow;

import java.util.HashMap;
import java.util.Map;

import com.nhnacademy.aiot.node.Node;

public class Flow {
    private final Map<String, Node> nodes;

    public Flow() {
        nodes = new HashMap<>();
    }

    /**
     * flow에 node를 추가합니다.
     * 
     * @param nodeId 추가할 노드 아이디
     * @param node   추가할 노드
     */
    public void add(String nodeId, Node node) {
        nodes.put(nodeId, node);
    }

    /**
     * flow에 추가된 모든 노드를 시작합니다.
     */
    public void start() {
        nodes.values().forEach(Node::start);
    }
}
