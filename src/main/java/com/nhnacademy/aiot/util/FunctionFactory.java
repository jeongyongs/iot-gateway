package com.nhnacademy.aiot.util;

import java.util.List;
import java.util.function.Function;

import com.nhnacademy.aiot.port.Packet;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;

public class FunctionFactory {

    private FunctionFactory() {
    }

    @SuppressWarnings("unchecked")
    public static Function<Packet, List<Packet>> getInstance(String script) {
        GroovyShell shell = new GroovyShell();
        script = "import com.nhnacademy.aiot.port.Packet;" +
                "import org.json.JSONObject;" +
                "import java.util.List;" +
                "{ Packet packet -> " +
                script + "}";
        Closure<List<Packet>> closure = (Closure<List<Packet>>) shell.evaluate(script);
        return closure::call;
    }

}
