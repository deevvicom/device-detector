package com.deevvi.device.detector.engine.parser.device;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Tests for {@link TelevisionParser} class.
 */
public class TelevisionParserTest {

    private final TelevisionParser parser = new TelevisionParser();

    @Test
    public void testHBBParser() {

        //call
        String userAgent = "Opera/9.80 (Linux mips ; U; HbbTV/1.1.1 (; Philips; ; ; ; ) CE-HTML/1.0 NETTV/3.2.1; en) Presto/2.6.33 Version/10.70";
        Map<String, String> result = parser.parse(userAgent);

        //verify
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        System.out.println(" --> " + userAgent);
        System.out.println(result);
        assertThat(result.get("brand")).isEqualTo("Philips");
        assertThat(result.get("model")).isEqualTo("NetTV Series");
    }
}
