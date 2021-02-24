package com.deevvi.device.detector.engine.parser.api;

import com.deevvi.device.detector.engine.api.DeviceDetectorParser;
import com.deevvi.device.detector.engine.api.DeviceDetectorResult;
import com.deevvi.device.detector.testsutils.ParserTestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link DeviceDetectorParser} class.
 */
public class DeviceDetectorParserTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final DeviceDetectorParser deviceDetectorParser = DeviceDetectorParser.getClient();

    @Test
    public void testRunSingleFile() throws IOException {

        runTest("/complete/single-test-complete.yml");
       /// runTest("/complete/test-complete-unknown.yml");
    }

    @Test
    public void testComplete() throws IOException {

        runTest("/complete/test-complete-bots.yml");
        runTest("/complete/test-complete-camera.yml");
        runTest("/complete/test-complete-car_browser.yml");
        runTest("/complete/test-complete-console.yml");
        runTest("/complete/test-complete-desktop.yml");

        runTest("/complete/test-complete-feature_phone.yml");
        runTest("/complete/test-complete-feed_reader.yml");
        runTest("/complete/test-complete-mediaplayer.yml");
        runTest("/complete/test-complete-mobile_apps.yml");
        runTest("/complete/test-complete-phablet.yml");
        runTest("/complete/test-complete-portable_media_player.yml");
        runTest("/complete/test-complete-smart_display.yml");
        runTest("/complete/test-complete-smart_speaker.yml");

        runTest("/complete/test-complete-smartphone.yml");
        runTest("/complete/test-complete-smartphone-1.yml");
        runTest("/complete/test-complete-smartphone-2.yml");
        runTest("/complete/test-complete-smartphone-3.yml");
        runTest("/complete/test-complete-smartphone-4.yml");
        runTest("/complete/test-complete-smartphone-5.yml");
        runTest("/complete/test-complete-smartphone-6.yml");
        runTest("/complete/test-complete-smartphone-7.yml");
        runTest("/complete/test-complete-smartphone-8.yml");
        runTest("/complete/test-complete-smartphone-9.yml");
        runTest("/complete/test-complete-smartphone-10.yml");
        runTest("/complete/test-complete-smartphone-11.yml");
        runTest("/complete/test-complete-smartphone-12.yml");
        runTest("/complete/test-complete-smartphone-13.yml");
        runTest("/complete/test-complete-smartphone-14.yml");
        runTest("/complete/test-complete-smartphone-15.yml");
        runTest("/complete/test-complete-smartphone-16.yml");
        runTest("/complete/test-complete-smartphone-17.yml");
        runTest("/complete/test-complete-smartphone-18.yml");
        runTest("/complete/test-complete-smartphone-19.yml");
        runTest("/complete/test-complete-smartphone-20.yml");
        runTest("/complete/test-complete-smartphone-21.yml");

        runTest("/complete/test-complete-tablet.yml");
        runTest("/complete/test-complete-tablet-1.yml");
        runTest("/complete/test-complete-tablet-2.yml");
        runTest("/complete/test-complete-tablet-3.yml");
        runTest("/complete/test-complete-tablet-4.yml");

        runTest("/complete/test-complete-tv.yml");
        runTest("/complete/test-complete-unknown.yml");
        runTest("/complete/test-complete-wearable.yml");
    }

    private void runTest(String path) throws IOException {

        List<String> categories = ImmutableList.of("os", "client", "device");
        AtomicInteger index = new AtomicInteger(1);
        loadRawArray(path)
                .stream()
                .filter(item -> item instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");

                    System.out.println("Test no: " + index.getAndIncrement() + " --> " + userAgent);
                    DeviceDetectorResult result = deviceDetectorParser.parse(userAgent);
                    if (!result.found()) {
                        fail("Unable to parse user agent: " + userAgent);
                    }
                    String json = result.toJSON();
                    System.out.println(result.toMap());
                    System.out.println(json);
                    for (String category : categories) {

                        if (map.containsKey(category) && map.get(category) instanceof Map) {
                            Map clientMap = (Map) map.get(category);
                            for (Object entryObject : clientMap.entrySet()) {

                                Map.Entry entry = (Map.Entry) entryObject;
                                if (StringUtils.isNotBlank(ParserTestUtils.extractValue(entry.getValue()))) {
                                    String key = ParserTestUtils.getKeyFromResult(entry);
                                    System.out.println(" + " + category + "." + key);
                                    String expectedValue = extractValue(json, category, key);
                                    if (entry.getValue() != null && !entry.getValue().equals("null")) {
                                        assertThat(ParserTestUtils.extractValue(entry.getValue())).ignoringCase().isEqualTo(expectedValue);
                                    }
                                }
                            }
                        }
                    }
                    if (map.containsKey("os_family")) {
                        System.out.println(" + os_family ");
                        String expectedValue = extractValue(json, "os", "osFamily");

                        if (!map.get("os_family").equals("Unknown")) {
                            assertThat(map.get("os_family")).isEqualTo(expectedValue);
                        }
                    }

                    if (map.containsKey("browser_family")) {

                        System.out.println(" + browser_family");
                        String expectedValue = extractValue(json, "client", "browserFamily");
                        if (!map.get("browser_family").equals("Unknown")) {
                            assertThat((String) map.get("browser_family")).ignoringCase().isEqualTo(expectedValue);
                        }
                    }
                });
    }

    private String extractValue(String json, String category, String key) {
        try {

            JsonNode node = mapper.readTree(json);
            JsonNode categoryNode = node.get(category);
            if (categoryNode == null) {
                return null;
            }
            if (categoryNode.has(key)) {
                return categoryNode.get(key).asText().trim();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
