package com.deevvi.device.detector.engine.parser;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.model.PatternBuilder;
import com.deevvi.device.detector.model.VendorFragments;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Parser to determine the vendor fragments.
 */
public final class VendorFragmentsParser implements Parser, PatternBuilder, MapLoader<VendorFragments> {

    /**
     * List with vendor fragments models.
     */
    private final List<VendorFragments> vendorFragments = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        Map<String, String> map = Maps.newHashMap();
        vendorFragments.forEach(vf -> {
            vf.getPatterns().forEach(pattern -> {
                Matcher matcher = pattern.matcher(userAgent);
                if (matcher.find()) {
                    map.put(VENDOR, vf.getVendor());
                }
            });
        });

        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/vendor-fragments.yml";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VendorFragments toObject(String key, Object value) {

        List<Pattern> patterns = Lists.newArrayList();
        ((List<String>) value).forEach(val -> patterns.add(Pattern.compile(String.format(REGEX_PATTERN, (val + "[^a-z0-9]+").replaceAll("/", "\\\\" + "/")), CASE_INSENSITIVE)));

        return new VendorFragments.Builder()
                .withVendor(key)
                .withPatterns(patterns)
                .build();
    }
}
