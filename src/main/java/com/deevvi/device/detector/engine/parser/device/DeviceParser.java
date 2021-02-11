package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.device.Device;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Device parser.
 */
abstract class DeviceParser implements Parser {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        for (Device device : getDevices()) {
            Matcher matcher = device.getPattern().matcher(userAgent);
            if (matcher.find()) {

                return toMap(buildModel(matcher, device, userAgent), Optional.ofNullable(device.getBrand()));
            } else if (!device.getModels().isEmpty()) {
                for (Map.Entry<Pattern, String> entry : device.getModels().entrySet()) {
                    Matcher modelMatcher = entry.getKey().matcher(userAgent);
                    if (modelMatcher.find()) {
                        return toMap(Optional.of(entry.getValue()), Optional.ofNullable(device.getBrand()));
                    }
                }
            }
        }

        return Maps.newHashMap();
    }

    /**
     * Get list with devices loaded from file.
     *
     * @return list with devices
     */
    protected abstract List<? extends Device> getDevices();

    /**
     * Get device type
     *
     * @return device type.
     */
    protected abstract String getDeviceType();

    private Optional<String> buildModel(Matcher matcher, Device device, String userAgent) {

        if (StringUtils.isNotBlank(device.getModel())) {

            return buildModelWithPattern(matcher, device.getModel());
        } else {

            return buildFromModels(device, userAgent);
        }
    }

    private Optional<String> buildFromModels(Device device, String userAgent) {

        for (Map.Entry<Pattern, String> entry : device.getModels().entrySet()) {
            Matcher modelMatcher = entry.getKey().matcher(userAgent);
            if (modelMatcher.find()) {

                return buildModelWithPattern(modelMatcher, entry.getValue());
            }
        }

        return Optional.empty();
    }

    private Map<String, String> toMap(Optional<String> model, Optional<String> brand) {

        Map<String, String> map = Maps.newHashMap();
        map.put(DEVICE_TYPE, getDeviceType());
        model.ifPresent(val -> map.put(MODEL, val));
        brand.ifPresent(val -> map.put(BRAND, val));

        return map;
    }
}