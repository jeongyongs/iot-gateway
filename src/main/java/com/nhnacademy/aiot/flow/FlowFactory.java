package com.nhnacademy.aiot.flow;

import com.nhnacademy.aiot.node.impl.FunctionNode;
import com.nhnacademy.aiot.setting.Setting;

public class FlowFactory {

    private FlowFactory() {}

    public static Flow getInstance(Setting setting) {
        Flow flow = new Flow();

        flow.add("test", new FunctionNode(null));

        return flow;
    }

}
