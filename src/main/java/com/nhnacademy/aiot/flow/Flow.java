package com.nhnacademy.aiot.flow;

import java.util.HashMap;
import java.util.Map;

import com.nhnacademy.aiot.node.Node;

public class Flow {
    private final Map<String, Node> nodes;

    public Flow() {
        nodes = new HashMap<>();
    }

    public void add(String nodeId, Node node) {
        nodes.put(nodeId, node);
    }

    public void start(){
        nodes.values().forEach(Node::start);
    }
}
