package com.nhnacademy.aiot.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nhnacademy.aiot.setting.Setting;

/**
 * commnad line에서 application name과 sensor type을 분석하는 클래스입니다.
 */
public class SettingLoader {
    private static final String FLOW = "flow";
    private static final String DEFAULT_APPLICATION_NAME = "#/";
    private static final String DEFAULT_SENSOR_TYPE = "temperature";
    private static final String APPLICATION_NAME_OPTION = "an";
    private static final String SENSOR_TYPE_OPTION = "s";
    private static final String CONFIGURATION_OPTION = "c";

    private SettingLoader() {
    }

    /**
     * command line을 분석합니다.
     * 
     * @param args main에서 입력한 String 배열
     * @return application name은 <code>"an"</code>, sensor type은 JSONArray로
     *         <code>"s"</code>라는 key에 담은 <code>JSONObject</code>
     */
    public static Setting load(String[] args) {
        Setting setting = new Setting();
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        options.addOption(null, APPLICATION_NAME_OPTION, true, "Application Name");
        options.addOption("s", null, true, "Sensor Type");
        options.addOption("c", null, true, "Setting json file");

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption(APPLICATION_NAME_OPTION)) {
                setting.put(APPLICATION_NAME_OPTION, commandLine.getOptionValue(APPLICATION_NAME_OPTION));
            }
            if (commandLine.hasOption(SENSOR_TYPE_OPTION)) {
                setting.put(SENSOR_TYPE_OPTION,
                        new JSONArray(commandLine.getOptionValue(SENSOR_TYPE_OPTION).split(",")));
            }
            if (setting.isNull(APPLICATION_NAME_OPTION) || setting.isNull(SENSOR_TYPE_OPTION)) {
                fillNull(setting, commandLine);
            }
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("help", options);
        }
        return setting;
    }

    /**
     * args에서 입력하지 않은 옵션을 기본값이나 설정 파일의 옵션으로 설정합니다.
     * 
     * @param setting     수정할 JSONObject
     * @param commandLine 읽어야 할 commandLine
     */
    private static void fillNull(Setting setting, CommandLine commandLine) {
        // 설정 파일이 있을 때
        if (commandLine.hasOption(CONFIGURATION_OPTION)) {
            JSONObject settingJSON = JSONFileReader.read(commandLine.getOptionValue(CONFIGURATION_OPTION));
            if (setting.isNull(APPLICATION_NAME_OPTION)) {
                setting.put(APPLICATION_NAME_OPTION, settingJSON.getString(APPLICATION_NAME_OPTION));
            }
            if (setting.isNull(SENSOR_TYPE_OPTION)) {
                setting.put(SENSOR_TYPE_OPTION, settingJSON.getJSONArray(SENSOR_TYPE_OPTION));
            }
            if (settingJSON.isNull(FLOW)) {
                throw new IllegalArgumentException();
            }
            setting.put(FLOW, settingJSON.getString(FLOW));
            return;
        }
        // 설정 파일이 없을 때 (기본값으로 설정)
        if (setting.isNull(APPLICATION_NAME_OPTION)) {
            setting.put(APPLICATION_NAME_OPTION, DEFAULT_APPLICATION_NAME);
        }
        if (setting.isNull(SENSOR_TYPE_OPTION)) {
            setting.put(SENSOR_TYPE_OPTION, new JSONArray().put(DEFAULT_SENSOR_TYPE));
        }

    }
}
