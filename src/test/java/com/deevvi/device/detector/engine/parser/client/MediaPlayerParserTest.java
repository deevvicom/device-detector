package com.deevvi.device.detector.engine.parser.client;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;

/**
 * Tests for {@link MediaPlayerParser} class.
 */
public class MediaPlayerParserTest {

    private final MediaPlayerParser parser = new MediaPlayerParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/client/test-media-player.yml")
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
                    ((Map) map.get("client")).entrySet().forEach(entryObject -> {
                        Map.Entry entry = (Map.Entry) entryObject;
                        if (StringUtils.isNotBlank((String) entry.getValue())) {
                            Assert.assertEquals(parseResult.get(getKeyFromResult(entry)), entry.getValue());
                        }
                    });
                });
    }

}
