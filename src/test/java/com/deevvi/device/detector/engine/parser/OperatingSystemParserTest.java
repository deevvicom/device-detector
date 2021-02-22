package com.deevvi.device.detector.engine.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.extractValue;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;

/**
 * Tests for {@link OperatingSystemParser} class.
 */
public class OperatingSystemParserTest {

    private OperatingSystemParser parser = new OperatingSystemParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/test-operating-system.yml")
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");
                    System.out.println(index.getAndIncrement() + " test --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (parseResult.isEmpty()) {
                        Assert.fail("Unable to parseResult " + userAgent);
                    }

                    Map<String, String> clientMap = (Map) map.get("os");
                    System.out.println(parseResult);
                    clientMap.entrySet()
                            .stream()
                            .forEach(entry -> {
                                String value = extractValue(entry.getValue());
                                if (StringUtils.isNotBlank(value)) {
                                    Assert.assertEquals(parseResult.get(getKeyFromResult(entry)), value);
                                }
                            });
                });
    }

    @Test
    public void testAllFilesOSSection() throws IOException {

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
    }

    private void testWithFile(String filePath) throws IOException {
        System.out.println("Test OS parser with file: " + filePath);
        final AtomicInteger index = new AtomicInteger(1);
        loadRawArray(filePath)
                .stream()
                .filter(obj -> (obj instanceof Map))
                .forEach(obj -> {
                    Map map = (Map) obj;
                    if (map.containsKey("os") && map.get("os") instanceof Map) {
                        String userAgent = (String) map.get("user_agent");
                        Map<String, String> osMap = (Map) map.get("os");
                        System.out.println(index.getAndIncrement() + " --> " + userAgent);
                        Map<String, String> res = parser.parse(userAgent);
                        if (!res.isEmpty()) {
                            System.out.println(res);
                            osMap.entrySet()
                                    .stream()
                                    .map(entryObject -> (Map.Entry) entryObject)
                                    .forEach(entry -> {
                                        String value = extractValue(entry.getValue());
                                        if (StringUtils.isNotBlank(value)) {
                                            Assert.assertEquals(res.get(getKeyFromResult(entry)), value);
                                        }
                                    });
                        }
                    }
                });
    }
}
