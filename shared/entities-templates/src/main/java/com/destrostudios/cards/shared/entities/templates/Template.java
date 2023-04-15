package com.destrostudios.cards.shared.entities.templates;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public class Template {

    private static final String SEPARATOR_INPUT_START = "(";
    private static final String SEPARATOR_INPUT_END = ")";
    private static final String SEPARATOR_OUTPUT_START = "{";
    private static final String SEPARATOR_OUTPUT_END = "}";
    private static final String SEPARATOR_PARAMETERS = ",";
    private static final String SEPARATOR_PARAMETER_NAME_VALUE = "=";

    private String name;
    private Map<String, Object> input;
    private Map<String, Object> output;

    public int getIntegerInput(String key) {
        return Integer.parseInt(getStringInput(key));
    }

    public String getStringInput(String key) {
        return input.get(key).toString();
    }

    public String getAsResolvedText() {
        String text = name;
        if (input.size() > 0) {
            text += SEPARATOR_INPUT_START + getAsResolvedText(input) + SEPARATOR_INPUT_END;
        }
        if (output.size() > 0) {
            text += SEPARATOR_OUTPUT_START + getAsResolvedText(output) + SEPARATOR_OUTPUT_END;
        }
        return text;
    }

    private static String getAsResolvedText(Map<String, Object> values) {
        String text = "";
        int i = 0;
        for (Map.Entry<String, Object> parameterEntry : values.entrySet()) {
            if (i > 0) {
                text += SEPARATOR_PARAMETERS;
            }
            text += parameterEntry.getKey() + SEPARATOR_PARAMETER_NAME_VALUE + parameterEntry.getValue().toString();
            i++;
        }
        return text;
    }

    // Utility

    public static Template parse(String template) {
        return parse(template, Function.identity(), Function.identity(), value -> value);
    }

    public static Template parse(String template, Function<String, String> getImplicitParameterName, Function<String, String> modifyValueKey, Function<String, Object> modifyValueValue) {
        String name = template;
        int inputStart = name.indexOf(SEPARATOR_INPUT_START);
        if (inputStart != -1) {
            name = name.substring(0, inputStart);
        } else {
            int outputStart = name.indexOf(SEPARATOR_OUTPUT_START);
            if (outputStart != -1) {
                name = name.substring(0, outputStart);
            }
        }
        name = modifyValueValue.apply(name).toString();
        Map<String, Object> input = parseInOrOutputs(template, SEPARATOR_INPUT_START, SEPARATOR_INPUT_END, getImplicitParameterName, modifyValueKey, modifyValueValue);
        Map<String, Object> output = parseInOrOutputs(template, SEPARATOR_OUTPUT_START, SEPARATOR_OUTPUT_END, getImplicitParameterName, modifyValueKey, value -> value);
        return new Template(name, input, output);
    }

    private static Map<String, Object> parseInOrOutputs(String text, String separatorStart, String separatorEnd, Function<String, String> getImplicitParameterName, Function<String, String> modifyValueKey, Function<String, Object> modifyValueValue) {
        HashMap<String, Object> inOrOutputs = new HashMap<>();
        while (text.contains(separatorStart)) {
            int indexStart = text.indexOf(separatorStart);
            int indexEnd = text.indexOf(separatorEnd);
            String[] valueTexts = text.substring(indexStart + 1, indexEnd).split(SEPARATOR_PARAMETERS);
            text = text.substring(0, indexStart) + text.substring(indexEnd + 1);
            for (String valueText : valueTexts) {
                String name;
                Object value;
                if (valueText.contains(SEPARATOR_PARAMETER_NAME_VALUE)) {
                    String[] parameterParts = valueText.split(SEPARATOR_PARAMETER_NAME_VALUE);
                    name = modifyValueKey.apply(parameterParts[0]);
                    value = modifyValueValue.apply(parameterParts[1]);
                } else {
                    name = getImplicitParameterName.apply(valueText);
                    value = modifyValueValue.apply(valueText);
                }
                inOrOutputs.put(name, value);
            }
        }
        return inOrOutputs;
    }
}
