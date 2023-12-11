package com.nhnacademy.aiot.util;

import java.util.List;
import java.util.function.Function;

import com.nhnacademy.aiot.port.Packet;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;

public class FunctionFactory {

    private FunctionFactory() {
    }

    /**
     * string인 script를 실행하는 Function 인스턴스를 생성합니다.
     * 
     * @param script functionNode가 실행할 코드
     * @return script를 실행할 수 있는 Function<Packet, List<Packet>> 인스턴스
     */
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
