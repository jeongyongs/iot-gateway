package com.nhnacademy.aiot.flow;

import java.util.HashMap;
import java.util.Map;
import com.nhnacademy.aiot.node.Node;

/**
 * 플로우를 관리하는 클래스입니다.
 */
public class Flow {

    private final Map<String, Node> nodes;

    /**
     * 플로우를 생성합니다.
     */
    public Flow() {
        nodes = new HashMap<>();
    }

    /**
     * 플로우에 노드를 추가합니다.
     * 
     * @param key 노드의 아이디입니다.
     * @param node 추가할 노드입니다.
     */
    public void add(String key, Node node) {
        nodes.put(key, node);
    }

    /**
     * 플로우를 시작합니다.
     */
    public void start() {
        nodes.values().stream().forEach(Node::start);
    }
}
