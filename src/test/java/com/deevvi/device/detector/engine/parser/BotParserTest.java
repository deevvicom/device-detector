package com.deevvi.device.detector.engine.parser;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link BotParser} class.
 */
public class BotParserTest {

    private final BotParser parser = new BotParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/complete/test-complete-bots.yml")
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");

                    System.out.println(index.getAndIncrement() + " --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (parseResult.isEmpty()) {
                        fail("Unable to parseResult " + userAgent);
                    }
                    System.out.println(parseResult);
                    Map clientMap = (Map) map.get("bot");
                    clientMap.entrySet().forEach(entryObject -> {
                        Map.Entry entry = (Map.Entry) entryObject;
                        if ((entry.getValue() instanceof String)
                                && StringUtils.isNotBlank((String) entry.getValue())) {
                            String value = parseResult.get(fetchKey(entry));
                            assertThat(entry.getValue()).isEqualTo(value);
                        } else if ((entry.getValue() instanceof Map) && entry.getKey().equals("producer")) {
                            Map producerMap = (Map) entry.getValue();
                            ImmutableList.of("producerName", "producerUrl").forEach(field -> {
                                if (producerMap.containsKey(field) && StringUtils.isNotEmpty((String) producerMap.get(field))) {
                                    assertThat(parseResult.get(field)).isEqualTo(producerMap.get(field));
                                }
                            });
                        }
                    });
                });
    }

    private String fetchKey(Map.Entry entry) {
        String key;
        if (entry.getKey().equals("type")) {
            key = "deviceType";
        } else if (entry.getKey().equals("engine_version")) {
            key = "engineVersion";
        } else if (entry.getKey().equals("short_name")) {
            key = "shortName";
        } else {
            key = (String) entry.getKey();
        }
        return key;
    }
}
