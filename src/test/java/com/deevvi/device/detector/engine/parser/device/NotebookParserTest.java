package com.deevvi.device.detector.engine.parser.device;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;

/**
 * Tests for {@link NotebookParser} class.
 */
public class NotebookParserTest {

    private final NotebookParser parser = new NotebookParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/device/test-notebook.yml")
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    HashMap map = (HashMap) obj;
                    String userAgent = (String) map.get("user_agent");
                    System.out.println(index.getAndIncrement() + " --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (!parseResult.isEmpty()) {
                        Map clientMap = (Map) map.get("device");
                        System.out.println(parseResult);
                        clientMap.entrySet().forEach(entryObject -> {
                            Map.Entry entry = (Map.Entry) entryObject;
                            if (entry.getKey().equals("type")) {
                                String value = parseResult.get("deviceType");
                                Assert.assertEquals(value, entry.getValue());
                            } else if (StringUtils.isNotBlank((String) entry.getValue())) {
                                Assert.assertEquals(parseResult.get(getKeyFromResult(entry)), entry.getValue());
                            }
                        });
                    }
                });
    }
}
