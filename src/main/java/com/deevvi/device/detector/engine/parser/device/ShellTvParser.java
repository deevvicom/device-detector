package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.device.ShellTv;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.deevvi.device.detector.engine.parser.device.DeviceType.TV;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

public final class ShellTvParser implements Parser, MapLoader<ShellTv> {

    private final Pattern SHELL_TV_PATTERN = Pattern.compile("[a-z]+[ _]Shell[ _]\\w{6}", CASE_INSENSITIVE);

    /**
     * List with notebooks models loaded from file.
     */
    private final List<ShellTv> shellTvs = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public ShellTv toObject(String key, Object value) {
        Map map = (Map) value;

        return new ShellTv.Builder()
                .withDevice((String) map.get(DEVICE))
                .withBrand(key)
                .withPattern(toPattern((String) map.get(REGEX)))
                .withModel((String) map.getOrDefault(MODEL, EMPTY_STRING))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/device/shell_tv.yml";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        if (!isShellTV(userAgent)) {
            return Maps.newHashMap();
        }

        return shellTvs
                .stream()
                .filter(shellTv -> shellTv.getPattern().matcher(userAgent).find())
                .findFirst()
                .map(shellTv -> toMap(shellTv.getBrand(), shellTv.getModel()))
                .orElse(new ImmutableMap.Builder<String, String>().put(DEVICE_TYPE, TV.getDeviceName()).build());
    }

    private boolean isShellTV(String userAgent) {
        return SHELL_TV_PATTERN.matcher(userAgent).find();
    }

    private Map<String, String> toMap(String brand, String model) {

        Map<String, String> map = Maps.newHashMap();
        map.put(DEVICE_TYPE, TV.getDeviceName());
        map.put(MODEL, model);
        if (StringUtils.isNotEmpty(brand)) {
            map.put(BRAND, brand);
        }

        return map;
    }
}
