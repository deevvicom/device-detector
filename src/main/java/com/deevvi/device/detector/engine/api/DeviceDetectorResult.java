package com.deevvi.device.detector.engine.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.deevvi.device.detector.engine.parser.Parser.DEVICE_TYPE;
import static com.deevvi.device.detector.engine.parser.Parser.EMPTY_STRING;
import static com.deevvi.device.detector.engine.parser.Parser.SHORT_NAME;
import static com.deevvi.device.detector.engine.parser.Parser.TYPE;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.CAMERA;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.FEATURE_PHONE;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.PHABLET;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.PORTABLE_MEDIA_PLAYER;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.SMARTPHONE;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.TABLET;
import static com.deevvi.device.detector.engine.utils.ConfigUtils.fetchListFromFile;

/**
 * The result of parsing an user agent.
 */
public final class DeviceDetectorResult {

    /**
     * Class logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DeviceDetectorResult.class);

    /**
     * Collections with configs loaded from external files.
     */
    private static final List<String> MOBILE_ONLY_BROWSER = fetchListFromFile("/configs/mobileOnlyBrowsers");

    /**
     * Jackson JSON handler.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * String constants.
     */
    private static final String BOT_MAP = "botMap";
    private static final String OS_DETAILS = "osDetails";
    private static final String CLIENT_DETAILS = "clientDetails";
    private static final String DEVICE_DETAILS = "deviceDetails";
    private static final String OS = "os";
    private static final String CLIENT = "client";
    private static final String DEVICE = "device";

    /**
     * Collection with mobile devices.
     */
    private static final Set<String> MOBILE_DEVICE_TYPES = ImmutableSet.of(FEATURE_PHONE.getDeviceName(),
            SMARTPHONE.getDeviceName(),
            TABLET.getDeviceName(),
            PHABLET.getDeviceName(),
            CAMERA.getDeviceName(),
            PORTABLE_MEDIA_PLAYER.getDeviceName());

    /**
     * Map with parsing results.
     */
    private final Map<String, Map<String, String>> map;

    /**
     * Constructor.
     */
    private DeviceDetectorResult() {

        map = Maps.newHashMap();
    }

    /**
     * Static factory for empty result.
     *
     * @return empty device detector result
     */
    static DeviceDetectorResult fromEmptyResult() {

        return new DeviceDetectorResult();
    }

    /**
     * Static factory for building bot result.
     *
     * @return bot result
     */
    static DeviceDetectorResult fromBotResult(Map<String, String> botMap) {

        DeviceDetectorResult result = new DeviceDetectorResult();
        result.map.put(BOT_MAP, ImmutableMap.copyOf(botMap));
        return result;
    }

    /**
     * Static factory for building device result.
     *
     * @return device result
     */
    static DeviceDetectorResult fromDevice(Map<String, String> osDetails, Map<String, String> clientDetails, Map<String, String> deviceDetails) {

        DeviceDetectorResult result = new DeviceDetectorResult();
        result.map.put(OS_DETAILS, ImmutableMap.copyOf(osDetails));
        result.map.put(CLIENT_DETAILS, ImmutableMap.copyOf(clientDetails));
        result.map.put(DEVICE_DETAILS, ImmutableMap.copyOf(deviceDetails));

        return result;
    }

    /**
     * Handler to encode result as json.
     *
     * @return String json
     */
    public String toJSON() {
        if (map.containsKey(BOT_MAP)) {
            return encodeBotAsJson();
        }
        return encodeAsJson();
    }

    /**
     * Handler to encode result as a map.
     *
     * @return result, encoded as map
     */
    public Map<String, String> toMap() {
        if (map.containsKey(BOT_MAP)) {
            return encodeBotAsMap();
        }
        return encodeAsMap();
    }

    /**
     * Checks if any result found
     *
     * @return <code>true</code> if result found, <code>false</code> otherwise.
     */
    public boolean found() {
        return !map.isEmpty();
    }

    /**
     * Checks if result found is bot.
     *
     * @return <code>true</code> if result found is bot, <code>false</code> otherwise.
     */
    public boolean isBot() {
        return map.containsKey(BOT_MAP);
    }


    /**
     * Checks if result found is a mobile device.
     *
     * @return <code>true</code> if result found is a mobile device, <code>false</code> otherwise
     */
    public boolean isMobileDevice() {

        return (map.containsKey(DEVICE_DETAILS)
                && MOBILE_DEVICE_TYPES.contains(map.getOrDefault(DEVICE_DETAILS, Maps.newHashMap()).getOrDefault(DEVICE_TYPE, EMPTY_STRING)))
                || MOBILE_ONLY_BROWSER.contains(map.getOrDefault(CLIENT_DETAILS, Maps.newHashMap()).getOrDefault(SHORT_NAME, EMPTY_STRING));
    }

    private Map<String, String> encodeBotAsMap() {
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put(TYPE, "bot");
        resultMap.putAll(map.get(BOT_MAP));

        return ImmutableMap.copyOf(resultMap);
    }

    private Map<String, String> encodeAsMap() {
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.putAll(encodeAsResultMap(map.get(OS_DETAILS), "os"));
        resultMap.putAll(encodeAsResultMap(map.get(CLIENT_DETAILS), "client"));
        resultMap.putAll(encodeAsResultMap(map.get(DEVICE_DETAILS), "device"));
        return ImmutableMap.copyOf(resultMap);
    }

    private Map<String, String> encodeAsResultMap(Map<String, String> inputMap, String prefix) {
        Map<String, String> map = Maps.newHashMap();
        inputMap.forEach((k, v) -> {
            if (StringUtils.isNoneEmpty(v)) {
                map.put(String.format("%s.%s", prefix, k), v);
            }
        });
        return map;
    }

    private String encodeAsJson() {

        ObjectNode responseNode = mapper.createObjectNode();
        if (!map.get(OS_DETAILS).isEmpty()) {
            responseNode.set(OS, createNode(map.get(OS_DETAILS)));
        }

        if (!map.get(CLIENT_DETAILS).isEmpty()) {
            responseNode.set(CLIENT, createNode(map.get(CLIENT_DETAILS)));
        }

        if (!map.get(DEVICE_DETAILS).isEmpty()) {
            responseNode.set(DEVICE, createNode(map.get(DEVICE_DETAILS)));
        }

        return encodeToJson(responseNode);
    }

    private ObjectNode createNode(Map<String, String> map) {

        ObjectNode node = mapper.createObjectNode();
        map.forEach((key, value) -> node.set(key, new TextNode(value)));

        return node;
    }

    private String encodeBotAsJson() {

        return encodeToJson(createNode(map.get(BOT_MAP)));
    }

    private String encodeToJson(JsonNode node) {

        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            LOG.error("Exception on building JSON ", e);
            throw new IllegalArgumentException("Unable to create JSON.");
        }
    }
}
