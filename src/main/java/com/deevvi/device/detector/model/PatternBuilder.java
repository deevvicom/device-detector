package com.deevvi.device.detector.model;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public interface PatternBuilder {

    /**
     * Template used for building patterns from a regex.
     */
    String REGEX_PATTERN = "(?:^|[^A-Z0-9\\-_]|[^A-Z0-9\\-]_|sprd-)(?:%s)";;

    /**
     * Convert a string to a compiled pattern, using the {@link this.REGEX_PATTERN} format.
     *
     * @param rawRegex regex, as string
     * @return pattern
     */
    default Pattern toPattern(String rawRegex) {

        return Pattern.compile(String.format(REGEX_PATTERN, rawRegex.replaceAll("/", "\\" + "/")), CASE_INSENSITIVE);
    }
}
