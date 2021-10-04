package com.deevvi.device.detector.testsutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;

/**
 * Helper class with various methods used for parsers tests.
 */
public class ParserTestUtils {

    /**
     * Class logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ParserTestUtils.class);

    /**
     * Jackson JSON handler.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * YAML reader.
     */
    private static final Yaml YAML = new Yaml();

    private static Map<Integer, String> deviceTypeMap = new ImmutableMap.Builder<Integer, String>()
            .put(0, "desktop")
            .put(1, "smartphone")
            .put(2, "tablet")
            .put(3, "feature phone")
            .put(4, "console")
            .put(5, "tv")
            .put(6, "car browser")
            .put(7, "smart display")
            .put(8, "camera")
            .put(9, "portable media player")
            .put(10, "phablet")
            .put(11, "smart speaker")
            .put(12, "wearable")
            .build();

    /**
     * Private constructor to avoid class init.
     */
    private ParserTestUtils() {
    }

    public static List loadRawArray(String filePath) throws IOException {

        return YAML.load(IOUtils.resourceToString(filePath, UTF_8));
    }

    public static String extractValue(String json, String key) {

        try {
            JsonNode node = mapper.readTree(json);
            if (node.has(key)) {
                return node.get(key).asText();
            }
        } catch (IOException e) {
            LOG.warn("Exception on parsing JSON ", e);
        }
        return null;
    }

    public static String extractValue(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Integer || value instanceof Double) {
            return String.valueOf(value);
        }

        return null;
    }

    public static String getDeviceType(int encoding) {
        return deviceTypeMap.getOrDefault(encoding, "");
    }

    public static String getKeyFromResult(Map.Entry entry) {
        String key;
        if (entry.getKey().equals("type")) {
            key = "deviceType";
        } else if (entry.getKey().equals("engine_version")) {
            key = "engineVersion";
        } else if (entry.getKey().equals("short_name")) {
            key = "shortName";
        } else if (entry.getKey().equals("brand")) {
            key = "brand";
        } else if (entry.getKey().equals("family")) {
            key = "osFamily";
        } else {
            key = (String) entry.getKey();
        }
        return key;
    }
}
