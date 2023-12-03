package com.nhnacademy.aiot.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import com.nhnacademy.aiot.setting.Setting;

class SettingLoaderTest {
        @Test
        void ArgsTest() {
                Setting setting = SettingLoader
                                .load(new String[] {"--an", "data/", "-s", "humidity,temperatrue"});

                assertAll(() -> assertEquals("data/", setting.getString("an")),
                                () -> assertEquals("humidity", setting.getJSONArray("s").get(0)),
                                () -> assertEquals("temperatrue",
                                                setting.getJSONArray("s").get(1)));
        }

        @Test
        void ArgsWithNullTest() {
                Setting setting = SettingLoader.load(new String[] {});

                assertAll(() -> assertEquals("#/", setting.getString("an")),
                                () -> assertEquals("temperature",
                                                setting.getJSONArray("s").get(0)));
        }

        @Test
        void SettingJSONTest() {
                Setting setting = SettingLoader
                                .load(new String[] {"--an", "data/", "-c", "settings.json"});

                assertAll(
                        () -> assertTrue(setting.has("flow")),
                        () -> assertEquals("data/", setting.getString("an")),
                        () -> assertEquals("pressure", setting.getJSONArray("s").get(1))
                );
        }
}
