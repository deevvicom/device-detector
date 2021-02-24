package com.deevvi.device.detector.engine.parser.device;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.extractValue;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;
import static com.google.common.truth.Truth.assertThat;

/**
 * Tests for {@link MobileParser} class.
 */
public class MobileParserTest {

    private final MobileParser parser = new MobileParser();

    @Test
    public void testDeviceSmartDisplay() throws IOException {

        runTest("/complete/test-complete-smart_display.yml");
    }

    @Test
    public void testDevicePhablet() throws IOException {

        runTest("/complete/test-complete-phablet.yml");
    }

    @Test
    public void testDeviceFeaturePhone() throws IOException {

        runTest("/complete/test-complete-feature_phone.yml");
    }

    @Test
    public void testDeviceSmartSpeaker() throws IOException {

        runTest("/complete/test-complete-smart_speaker.yml");
    }

    private void runTest(String path) throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray(path)
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");
                    System.out.println(index.getAndIncrement() + " --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (!parseResult.isEmpty()) {
                        System.out.println(parseResult);
                        Map clientMap = (Map) map.get("device");
                        clientMap.entrySet().forEach(entryObject -> {
                            Map.Entry entry = (Map.Entry) entryObject;
                            String value = extractValue(entry.getValue());
                            if (StringUtils.isNotBlank(value)) {
                                String parsedValue = parseResult.get(getKeyFromResult(entry));
                                if (!parsedValue.equals("feature phone") && !value.equals("smartphone")) {
                                    assertThat(parsedValue).isEqualTo(value);
                                }
                            }
                        });
                    }
                });
    }
}
