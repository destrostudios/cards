package com.destrostudios.cards.shared.entities.templates;

public record EntityProxy(int entity) {

    // While template recording resolves entities that are passed as template inputs/outputs,
    // it doesn't cleanly support resolving entities that are included in string component values (It simply stores the resulting component value string)
    // Therefore, a prefix is attached in the toString method of the proxdy, in order to resolve it by string replacement later
    public static final String IN_TEXT_PREFIX = "<PROXY";
    public static final String IN_TEXT_SUFFIX = ">";

    @Override
    public String toString() {
        return IN_TEXT_PREFIX + entity + IN_TEXT_SUFFIX;
    }

    public static String resolveEntitiesInText(int[] proxiedEntities, String text) {
        for (int i = 0; i < proxiedEntities.length; i++) {
            text = text.replaceAll(IN_TEXT_PREFIX + i + IN_TEXT_SUFFIX, Integer.toString(proxiedEntities[i]));
        }
        return text;
    }
}
