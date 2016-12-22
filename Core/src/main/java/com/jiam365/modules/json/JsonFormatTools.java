package com.jiam365.modules.json;

public class JsonFormatTools {
    private static String SPACE = "    ";

    public static String formatJson(String json) {
        StringBuilder result = new StringBuilder();

        int length = json.length();
        int number = 0;
        for (int i = 0; i < length; i++) {
            char key = json.charAt(i);
            if ((key == '[') || (key == '{')) {
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append('\n');
                    result.append(indent(number));
                }
                result.append(key);
                result.append('\n');

                number++;
                result.append(indent(number));
            } else if ((key == ']') || (key == '}')) {
                result.append('\n');

                number--;
                result.append(indent(number));

                result.append(key);
                if ((i + 1 < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }
            } else if (key == ',') {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
            } else {
                result.append(key);
            }
        }
        return result.toString();
    }

    private static String indent(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }
}
