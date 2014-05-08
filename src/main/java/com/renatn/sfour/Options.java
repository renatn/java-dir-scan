package com.renatn.sfour;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Renat Nasyrov <SBT-Nasyrov-RV@mail.ca.sbrf.ru>
 * Date: 08.05.2014
 * Time: 17:05
 */
public class Options {

    private final Map<String, String> options;
    private final String command;

    private Options(String command, Map<String, String> options) {
        this.command = command;
        this.options = options;
    }

    public static Options parse(String commandLine) throws IllegalArgumentException {
        String[] options = commandLine.trim().split(" ");
        if (options.length < 1) {
            throw new IllegalArgumentException("Invalid command format");
        }
        return new Options(options[0], parseOptions(options));
    }

    public String getCommand() {
        return command;
    }

    public String getStringOption(String name, boolean required) throws IllegalArgumentException {
        String value = options.get(name);
        if (value == null && required) {
            throw new IllegalArgumentException("Option <" + name + "> is required.");
        }
        return value;
    }

    public Integer getIntOption(String name, boolean required) throws IllegalArgumentException {
        String value = options.get(name);
        if (required && value == null) {
            throw new IllegalArgumentException("Option <" + name + "> is required.");
        }

        if (value == null) {
            return null;
        }

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Option  <" + name + "> must be integer value");
        }

    }

    public boolean getBooleanOption(String name, boolean required) throws IllegalArgumentException {
        String value = options.get(name);
        if (required && value == null) {
            throw new IllegalArgumentException("Option <" + name + "> is required.");
        }

        if (value != null) {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                return Boolean.valueOf(value);
            } else {
                throw new IllegalArgumentException("Invalid value for autoDelete:" + name + " (must be true or false)");
            }
        }
        return false;
    }


    private static Map<String, String> parseOptions(String[] options) {

        HashMap<String, String> result = new HashMap<String, String>();
        if (options.length < 3) { // First element is command, next must be pair key-value
            return result;
        }

        int i = 1;
        while (i < options.length) {
            String option = options[i++];
            if (option.startsWith("-") && option.length() > 1 && i < options.length) {
                String value = options[i++];
                result.put(option.substring(1), value);
            }
        }

        return result;
    }
}
