package com.deevvi.device.detector.engine.parser.device;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;

/**
 * Tests for {@link ConsoleParser} class.
 */
public class ConsoleParserTest {

    private final ConsoleParser parser = new ConsoleParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/device/test-consoles.yml")
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
                            Truth.assertThat(parseResult.get(getKeyFromResult(entry))).isEqualTo(entry.getValue());
                        });
                    }
                });
    }
}
