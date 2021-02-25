package com.deevvi.device.detector.engine.parser;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Model for parsing an input to generate gadget details.
 */
public interface Parser {

    /**
     * Parse the input and return the response as a JSON.
     *
     * @param userAgent input user agent
     * @return response, as a map, where key is the property name and value is property value
     */
    Map<String, String> parse(String userAgent);

    /**
     * Check if version from model is valid.
     *
     * @param version version from model
     * @return true if is valid, false othwerwise
     */
    default boolean hasValidVersion(String version) {
        return StringUtils.isNotBlank(version);
    }

    /**
     * Extract version from matched user agent
     *
     * @param matcher matcher
     * @param version model version
     * @return version value
     */
    default Optional<String> buildVersion(Matcher matcher, String version) {
        if (!hasValidVersion(version)) {
            return Optional.empty();
        }

        int index = getStartIndex(version);
        String builtVersion = version;
        String group = "$";
        String regex_group = "\\$";

        while (builtVersion.contains(group + index)) {
            String current = matcher.group(index);
            if (current == null) {
                if (builtVersion.equals(version)) {
                    return Optional.empty();
                } else {
                    current = EMPTY_STRING;
                }
            }

            builtVersion = builtVersion.replaceAll(regex_group + index, current);
            index++;
        }

        if (builtVersion.endsWith(".")) {
            return Optional.of(builtVersion.substring(0, builtVersion.length() - 1));
        }

        return Optional.of(builtVersion.trim().replaceAll("_", "."));
    }

    default int getStartIndex(String version) {
        int index = 0;

        if (version.contains("$")) {
            do {
                index++;
            } while (!version.contains("$" + index));
        }

        return index;
    }

    default boolean startWithPattern(String model) {
        return model.startsWith("$");
    }

    default Optional<String> buildModelWithPattern(Matcher matcher, String model) {

        int index = getStartIndex(model);
        if (index == 0) {
            return Optional.of(clear(model));
        }
        String group = "$";
        String regex_group = "\\$";
        while (model.contains(group + index)) {
            String current = matcher.group(index);
            if (current == null) {
                return Optional.empty();
            }
            model = model.replaceAll(regex_group + index, current);
            index++;
        }

        return Optional.of(clear(model.trim()));
    }

    default Optional<String> buildByMatcher(Matcher matcher, String item) {
        if (item == null) {
            return Optional.empty();
        }

        if (!item.contains("$")) {
            return Optional.of(item.trim());
        }

        for (int i = 1; i <= 3; i++) {
            if (!item.contains("$" + i)) {
                continue;
            }

            String group = matcher.group(i);
            String replacement = Optional.ofNullable(group).orElse("");
            item = item.replace("$" + i, replacement);
        }
        return Optional.of(item.trim());
    }

    static boolean checkContainsPlaceholder(String input) {
        return StringUtils.isNotEmpty(input) && TEMPLATE_PATTERN.matcher(input).find();
    }

    static String clear(String raw) {
        if (raw == null) {
            return null;
        }
        String newString = raw.replaceAll("_", " ");
        if (newString.endsWith(" TD")) {
            return newString.replaceAll(" TD", "");
        }
        return newString;
    }

    default String capitalize(String raw) {
        return StringUtils.isEmpty(raw) ? raw : Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }

    Pattern TEMPLATE_PATTERN = Pattern.compile("\\$[0-9]", CASE_INSENSITIVE);
    String DEFAULT = "default";
    String DEVICE_TYPE = "deviceType";
    String BROWSER = "browser";
    String CATEGORY = "category";
    String PRODUCER_NAME = "producerName";
    String NAME = "name";
    String URL = "url";
    String PRODUCER_URL = "producerUrl";
    String MODEL = "model";
    String BRAND = "brand";
    String VERSION = "version";
    String PLATFORM = "platform";
    String OS_FAMILY = "osFamily";
    String ENGINE = "engine";
    String ENGINE_VERSION = "engineVersion";
    String ENGINE_NAME = "engineName";
    String SHORT_NAME = "shortName";
    String BROWSER_FAMILY = "browserFamily";
    String UNKNOWN = "Unknown";
    String VENDOR = "vendor";
    String EMPTY_STRING = "";
    String REGEX = "regex";
    String DEVICE = "device";
    String VERSIONS = "versions";
    String MODELS = "models";
    String TYPE = "type";
    String PRODUCER = "producer";


}
