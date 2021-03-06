package com.deevvi.device.detector.engine.parser.device;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.getKeyFromResult;
import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;

/**
 * Tests for {@link CameraParser} class.
 */
public class CameraParserTest {

    private final CameraParser parser = new CameraParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/device/test-camera.yml")
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("user_agent");
                    System.out.println(index.getAndIncrement() + " --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (!parseResult.isEmpty()) {
                        System.out.println(parseResult);
                        ((Map) map.get("device")).entrySet().forEach(entryObject -> {
                            Map.Entry entry = (Map.Entry) entryObject;
                            Truth.assertThat(parseResult.get(getKeyFromResult(entry))).isEqualTo(entry.getValue());
                        });
                    }
                });
    }
}
