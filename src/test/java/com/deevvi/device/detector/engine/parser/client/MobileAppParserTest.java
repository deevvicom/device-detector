package com.deevvi.device.detector.engine.parser.client;

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
 * Tests for {@link MobileAppParser} class.
 */
public class MobileAppParserTest {

    private final MobileAppParser parser = new MobileAppParser();

    @Test
    public void testComplete() throws IOException {


        final AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/client/test-mobile_app.yml")
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");

                    System.out.println(index.getAndIncrement() + " --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (parseResult.isEmpty()) {
                        Assert.fail("Unable to parse " + userAgent);
                    }
                    System.out.println(parseResult);
                    ((Map) map.get("client"))
                            .entrySet()
                            .forEach(entryObject -> {
                                Map.Entry entry = (Map.Entry) entryObject;
                                if (StringUtils.isNotBlank((String) entry.getValue())) {
                                    Assert.assertEquals(parseResult.get(getKeyFromResult(entry)), entry.getValue());
                                }
                            });
                });
    }

    @Test
    public void testAllFilesMobileAppSection() throws IOException {

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
        AtomicInteger index = new AtomicInteger(1);
        loadRawArray(filePath)
                .stream()
                .filter(obj -> (obj instanceof Map))
                .forEach(obj -> {
                    Map map = (Map) obj;
                    if (map.containsKey("client") && map.get("client") instanceof Map) {
                        String userAgent = (String) map.get("user_agent");
                        Map<String, String> clientMap = (Map) map.get("client");
                        if (clientMap.containsKey("type") && clientMap.get("type").equals("mobile app")) {
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
                                                Assert.assertEquals(parseResult.get(getKeyFromResult(entry)), value);
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
