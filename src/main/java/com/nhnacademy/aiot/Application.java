package com.nhnacademy.aiot;

import com.nhnacademy.aiot.flow.Flow;
import com.nhnacademy.aiot.setting.Setting;
import com.nhnacademy.aiot.util.SettingParser;

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
        Setting setting = SettingParser.parse(args);
        Flow flow = null;
        flow.start();
    }
}
