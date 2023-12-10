package com.nhnacademy.aiot.flow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.nhnacademy.aiot.setting.Setting;
import com.nhnacademy.aiot.util.SettingLoader;

class FlowFactoryTest {
    @Test
    void FlowFactoryTest() {
        Setting setting = SettingLoader
                .load(new String[] { "-c", "./src/test/java/com/nhnacademy/aiot/util/test2.json" });

        assertEquals("application/#", setting.getString("an"));
        
        Flow flow = FlowFactory.getInstance(setting);
        flow.start();
    }
}
