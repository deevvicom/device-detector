package com.deevvi.device.detector.engine.parser.client;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.extractValue;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link BrowserParser} class.
 */
public class BrowserParserTest {

    private final BrowserParser parser = new BrowserParser(new BrowserEngineParser());

    @Test
    public void testNullLoader() {

        //call
        assertThrows(NullPointerException.class, () -> new BrowserParser(null));
    }

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/client/test-browsers.yml")
                .stream()
                .filter(obj -> (obj instanceof Map))
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");
                    System.out.println(index.getAndIncrement() + " --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (parseResult.isEmpty()) {
                        Assertions.fail("Unable to parse " + userAgent);
                    }
                    System.out.println(parseResult);
                    if (map.containsKey("client") && map.get("client") instanceof Map) {
                        Map<String, String> clientMap = (Map) map.get("client");
                        clientMap.entrySet()
                                .stream()
                                .map(entryObject -> (Map.Entry) entryObject)
                                .forEach(entry -> {
                                    String value = extractValue(entry.getValue());
                                    if (StringUtils.isNotBlank(value)) {
                                        assertThat(value).isEqualTo(parseResult.get(getKeyFromResult(entry)));
                                    }
                                });
                    }
                });

    }

    //Single test, useful for debugging
    @Test
    public void testSingleTest() throws IOException {

        Object current = loadRawArray("/parser/client/test-single-test.yml");
        Map map = (Map) ((List) current).get(0);
        String userAgent = (String) map.get("user_agent");

        System.out.println("Single test --> " + userAgent);
        Map<String, String> json = parser.parse(userAgent);
        Map clientMap = (Map) map.get("client");
        System.out.println(json);
        for (Object entryObject : clientMap.entrySet()) {

            Map.Entry entry = (Map.Entry) entryObject;
            String value = extractValue(entry.getValue());
            if (StringUtils.isNotBlank(value)) {
                assertThat(value).isEqualTo(json.get(getKeyFromResult(entry)));
            }
        }

    }

    @Test
    public void testAllFilesBrowserSection() throws IOException {

        testWithFile("/complete/test-complete-bots.yml");
        testWithFile("/complete/test-complete-camera.yml");
        testWithFile("/complete/test-complete-car_browser.yml");
        testWithFile("/complete/test-complete-console.yml");
        testWithFile("/complete/test-complete-desktop.yml");
        testWithFile("/complete/test-complete-feature_phone.yml");
        testWithFile("/complete/test-complete-feed_reader.yml");
        testWithFile("/complete/test-complete-mediaplayer.yml");
        testWithFile("/complete/test-complete-mobile_apps.yml");
        testWithFile("/complete/test-complete-phablet.yml");
        testWithFile("/complete/test-complete-portable_media_player.yml");
        testWithFile("/complete/test-complete-smart_display.yml");
        testWithFile("/complete/test-complete-smart_speaker.yml");
        testWithFile("/complete/test-complete-smartphone.yml");
        testWithFile("/complete/test-complete-smartphone-1.yml");
        testWithFile("/complete/test-complete-smartphone-2.yml");
        testWithFile("/complete/test-complete-smartphone-3.yml");
        testWithFile("/complete/test-complete-smartphone-4.yml");
        testWithFile("/complete/test-complete-smartphone-5.yml");
        testWithFile("/complete/test-complete-smartphone-6.yml");
        testWithFile("/complete/test-complete-smartphone-7.yml");
        testWithFile("/complete/test-complete-smartphone-8.yml");
        testWithFile("/complete/test-complete-smartphone-9.yml");
        testWithFile("/complete/test-complete-smartphone-10.yml");
        testWithFile("/complete/test-complete-smartphone-11.yml");
        testWithFile("/complete/test-complete-smartphone-12.yml");
        testWithFile("/complete/test-complete-smartphone-13.yml");
        testWithFile("/complete/test-complete-smartphone-14.yml");
        testWithFile("/complete/test-complete-tablet.yml");
        testWithFile("/complete/test-complete-tablet-1.yml");
        testWithFile("/complete/test-complete-tablet-2.yml");
        testWithFile("/complete/test-complete-tablet-3.yml");
        testWithFile("/complete/test-complete-tv.yml");
        testWithFile("/complete/test-complete-unknown.yml");
    }

    private void testWithFile(String filePath) throws IOException {
        System.out.println("Test browser parser with file: " + filePath);
        final AtomicInteger index = new AtomicInteger(1);
        loadRawArray(filePath)
                .stream()
                .filter(obj -> (obj instanceof Map))
                .forEach(obj -> {
                    Map map = (Map) obj;
                    if (map.containsKey("client") && map.get("client") instanceof Map) {
                        String userAgent = (String) map.get("user_agent");
                        Map<String, String> clientMap = (Map) map.get("client");
                        if (clientMap.containsKey("type") && clientMap.get("type").equals("browser")) {
                            System.out.println(index.getAndIncrement() + " --> " + userAgent);
                            Map<String, String> parseResult = parser.parse(userAgent);
                            if (!parseResult.isEmpty()) {
                                System.out.println(parseResult);
                                clientMap.entrySet()
                                        .stream()
                                        .map(entryObject -> (Map.Entry) entryObject)
                                        .forEach(entry -> {
                                            String value = extractValue(entry.getValue());
                                            if (StringUtils.isNotBlank(value)) {
                                                assertThat(parseResult.get(getKeyFromResult(entry))).isEqualTo(value);
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
