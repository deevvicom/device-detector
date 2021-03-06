package com.deevvi.device.detector.engine.parser.client;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link FeedReaderParser} test.
 */
public class FeedReaderParserTest {

    private final FeedReaderParser parser = new FeedReaderParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/client/test-feed_reader.yml")
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");
                    System.out.println(index.getAndIncrement() + " --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (parseResult.isEmpty()) {
                        fail("Unable to parse " + userAgent);
                    }
                    System.out.println(parseResult);
                    Map clientMap = (Map) map.get("client");
                    for (Object entryObject : clientMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) entryObject;
                        if (StringUtils.isNotBlank((String) entry.getValue())) {
                            assertThat(parseResult.get(getKeyFromResult(entry))).isEqualTo(entry.getValue());
                        }
                    }
                });
    }
}
