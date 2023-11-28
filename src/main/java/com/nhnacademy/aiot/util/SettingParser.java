package com.nhnacademy.aiot.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import com.nhnacademy.aiot.setting.Setting;

public class SettingParser {
    private static final String DEFAULT_APPLICATION_NAME = "#/";
    private static final String DEFAULT_SENSOR_TYPE = "temperature";

    private SettingParser() {
    }

    public static Setting parse(String[] args) {
        Setting setting = new Setting();
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        options.addOption(null, "an", true, "Application Name");
        options.addOption("s", null, true, "Sensor Type");
        options.addOption("c", null, true, "Setting json file");

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("an")) {
                setting.put("an", commandLine.getOptionValue("an"));
            }
            if (commandLine.hasOption("s")) {
                setting.put("s", commandLine.getOptionValue("s"));
            }
            if (setting.isNull("an") || setting.isNull("s")) {
                fillNull(setting, commandLine.hasOption("c"));
            }
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("help", options);
        }
        return setting;
    }

    private static void fillNull(Setting setting, boolean hasOptionC) {
        if (!hasOptionC) {
            if (setting.isNull("an")) {
                setting.put("an", DEFAULT_APPLICATION_NAME);
            }
            if (setting.isNull("s")) {
                setting.put("an", DEFAULT_APPLICATION_NAME);
            }
        }
        else {
            
            if (setting.isNull("an")) {
                setting.put("an", DEFAULT_APPLICATION_NAME);
            }
            if (setting.isNull("s")) {
                setting.put("an", DEFAULT_APPLICATION_NAME);
            }
        }
    }

    public static void main(String[] args) {
        // SettingParser.parse(args);
        JSONObject o = new JSONObject();
        System.out.println(o.isNull("an"));
    }
}
