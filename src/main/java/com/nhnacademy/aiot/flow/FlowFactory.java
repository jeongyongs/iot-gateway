package com.nhnacademy.aiot.flow;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.node.NodeProperty;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.setting.Setting;
import com.nhnacademy.aiot.util.FunctionFactory;
import com.nhnacademy.aiot.util.JSONFileReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowFactory {
    private static final String ID = "id";
    private static final String AN = "an";
    private static final String FUNC = "func";
    private static final String FLOW = "flow";
    private static final String NODES = "nodes";
    private static final String WIRES = "wires";
    private static final String CLASS = "class";
    private static final String BROKER = "broker";
    private static final String BROKERS = "brokers";
    private static final String MQTT_IN_NODE = "MQTTInNode";
    private static final String FUNCTION_NODE = "FunctionNode";
    private static final String CONNECT_IN_PORT = "connectInPort";
    private static final String CONNECT_OUT_PORT = "connectOutPort";
    private static final String CLASS_NAME = "com.nhnacademy.aiot.node.impl.";

    private FlowFactory() {
    }

    public static Flow getInstance(Setting setting) {
        Flow flow = new Flow();
        Map<String, Pipe> pipeMap = new HashMap<>();
        JSONArray nodes = JSONFileReader.read(setting.getString(FLOW)).getJSONArray(NODES);

        try {
            for (int i = 0; i < nodes.length(); i++) {
                NodeProperty nodeProperty = setProperty(i, nodes, setting);
                Node node = addNodetoFlow(nodeProperty, flow);

                String nodeId = nodeProperty.getString(ID);
                if (pipeMap.containsKey(nodeId)) {
                    connectIn(node, pipeMap, nodeId);
                }

                if (nodeProperty.has(WIRES)) {
                    connectOut(nodeProperty, node, pipeMap);
                }
            }
        } catch (ReflectiveOperationException e) {
            log.error("FlowFactory: ReflectiveOperationException 발생", e);
        }
        return flow;
    }

    private static NodeProperty setProperty(int i, JSONArray nodes, Setting setting) {
        JSONObject nodeObj = nodes.getJSONObject(i);
        NodeProperty nodeProperty = new NodeProperty(nodeObj.toString());

        if (nodeObj.has(BROKER)) {
            putBroker(nodeProperty, JSONFileReader.read(setting.getString(FLOW)).getJSONArray(BROKERS));
        }

        if (nodeObj.getString(CLASS).equalsIgnoreCase(MQTT_IN_NODE)) {
            nodeProperty.put(AN, setting.getString(AN));
        }
        if (nodeObj.getString(CLASS).equalsIgnoreCase(FUNCTION_NODE)) {
            nodeProperty.put(FUNC, FunctionFactory.getInstance(nodeObj.getString(FUNC)));
        }

        return nodeProperty;
    }

    private static Node addNodetoFlow(NodeProperty nodeProperty, Flow flow) throws ReflectiveOperationException {
        Node node = (Node) Class
                .forName(CLASS_NAME + nodeProperty.getString(CLASS))
                .getConstructor(NodeProperty.class)
                .newInstance(nodeProperty);
        flow.add(nodeProperty.getString(ID), node);
        return node;
    }

    private static void connectIn(Node node, Map<String, Pipe> pipeMap, String nodeId)
            throws ReflectiveOperationException {
        node.getClass().getMethod(CONNECT_IN_PORT, Pipe.class).invoke(node, pipeMap.get(nodeId));
    }

    private static void putBroker(NodeProperty nodeProperty, JSONArray brokers) {
        String brokerId = nodeProperty.getString(BROKER);
        for (int j = 0; j < brokers.length(); j++) {
            if (brokers.getJSONObject(j).getString(ID).equals(brokerId)) {
                nodeProperty.remove(BROKER);
                nodeProperty.put(BROKER, brokers.getJSONObject(j));
                return;
            }
        }
    }

    private static void connectOut(NodeProperty nodeProperty, Node node, Map<String, Pipe> pipeMap)
            throws ReflectiveOperationException {
        JSONArray ports = nodeProperty.getJSONArray(WIRES);
        for (int j = 0; j < ports.length(); j++) {
            JSONArray pipes = ports.getJSONArray(j);
            for (int k = 0; k < pipes.length(); k++) {
                Pipe pipe = new Pipe();
                node.getClass().getMethod(CONNECT_OUT_PORT, int.class, Pipe.class).invoke(node, j, pipe);
                pipeMap.put(pipes.getString(k), pipe);
            }
        }
    }
}
