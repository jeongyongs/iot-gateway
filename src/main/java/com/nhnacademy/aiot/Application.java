package com.nhnacademy.aiot;

import com.nhnacademy.aiot.flow.Flow;
import com.nhnacademy.aiot.flow.FlowFactory;
import com.nhnacademy.aiot.setting.Setting;
import com.nhnacademy.aiot.util.SettingLoader;

/**
 * 게이트웨이 소프트웨어입니다.
 */
public class Application {

    private Application() {}

    /**
     * 게이트웨이를 시작합니다.
     * 
     * @param args 커맨드라인 인자입니다.
     */
    public static void run(String[] args) {
        Setting setting = SettingLoader.load(new String[] {"-c","./src/main/resources/setting.json"});
        Flow flow = FlowFactory.getInstance(setting);
        flow.start();
    }
}
