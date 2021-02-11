package com.deevvi.device.detector.engine.parser.device;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

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
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        System.out.println(" --> " + userAgent);
        System.out.println(result);
        Assert.assertEquals("Philips", result.get("brand"));
        Assert.assertEquals("NetTV Series", result.get("model"));
    }
}
