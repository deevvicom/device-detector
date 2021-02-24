package com.deevvi.device.detector.engine.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.deevvi.device.detector.testsutils.ParserTestUtils.loadRawArray;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link VendorFragmentsParser} class.
 */
public class VendorFragmentsParserTest {

    private final VendorFragmentsParser parser = new VendorFragmentsParser();

    @Test
    public void testComplete() throws IOException {

        AtomicInteger index = new AtomicInteger(1);
        loadRawArray("/parser/test-vendorfragments.yml")
                .stream()
                .filter(obj -> obj instanceof Map)
                .forEach(obj -> {
                    Map map = (Map) obj;
                    String userAgent = (String) map.get("useragent");

                    System.out.println(index.getAndIncrement() + " test --> " + userAgent);
                    Map<String, String> parseResult = parser.parse(userAgent);
                    if (parseResult.isEmpty()) {
                        fail("Unable to parseResult " + userAgent);
                    }
                    System.out.println(parseResult);

                    assertThat( (String) map.get("vendor")).isEqualTo(parseResult.get("vendor"));
                });
    }
}
