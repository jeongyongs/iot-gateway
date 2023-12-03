package com.nhnacademy.aiot.node;

import org.json.JSONObject;

public class NodeProperty extends JSONObject {

    public NodeProperty(JSONObject jsonObject) {
        jsonObject.keySet() //
                .stream() //
                .forEach(key -> this.put(key, jsonObject.get(key)));
    }
}
