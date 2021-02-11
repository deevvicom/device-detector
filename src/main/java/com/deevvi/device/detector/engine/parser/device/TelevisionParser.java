package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.device.Television;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.deevvi.device.detector.engine.parser.device.DeviceType.TV;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Parser to validate if input is a television.
 */
public final class TelevisionParser implements Parser, MapLoader<Television> {

    /**
     * Hbb pattern.
     */
    private static final Pattern HBB_TV_PATTERN = Pattern.compile(
            "(?:^|[^A-Z0-9\\-_]|[^A-Z0-9\\-]_|sprd-)(?:HbbTV/([1-9]{1}(?:\\.[0-9]{1}){1,2}))".replaceAll("/", "\\\\" + "/"),
            CASE_INSENSITIVE);

    /**
     * List with TVs models.
     */
    private final List<Television> televisions = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        Matcher hbbMatcher = HBB_TV_PATTERN.matcher(userAgent);
        if (!hbbMatcher.find()) {
            return Maps.newHashMap();
        }

        for (Television television : televisions) {
            Matcher matcher = television.getPattern().matcher(userAgent);
            if (matcher.find()) {

                return toMap(television.getBrand(), buildModel(matcher, television, userAgent));
            }
        }

        for (Television television : televisions) {
            if (!television.getModels().isEmpty()) {
                for (Map.Entry<Pattern, String> entry : television.getModels().entrySet()) {
                    Matcher modelMatcher = entry.getKey().matcher(userAgent);
                    if (modelMatcher.find()) {

                        return toMap(television.getBrand(), Optional.of(entry.getValue()));
                    }
                }
            }
        }

        Matcher matcher = HBB_TV_PATTERN.matcher(userAgent);
        Map<String, String> map = Maps.newHashMap();
        if (matcher.find()) {
            map.put(DEVICE_TYPE, TV.getDeviceName());
            if (matcher.groupCount() >= 1) {
                map.put(VERSION, matcher.group(1));
            }
        }

        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Television toObject(String key, Object value) {
        Map map = (Map) value;
        Map<Pattern, String> models = Maps.newLinkedHashMap();
        if (map.containsKey(MODELS)) {
            ((List) map.get(MODELS)).forEach(obj -> {
                Map modelEntry = (Map) obj;
                models.put(toPattern((String) modelEntry.get(REGEX)), (String) modelEntry.get(MODEL));
            });
        }

        return new Television.Builder()
                .withDevice((String) map.get(DEVICE))
                .withPattern(toPattern((String) map.get(REGEX)))
                .withModel((String) map.getOrDefault(MODEL, EMPTY_STRING))
                .withBrand(key)
                .withModels(models)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/device/televisions.yml";
    }

    private Optional<String> buildModel(Matcher matcher, Television television, String userAgent) {

        if (StringUtils.isNotBlank(television.getModel())) {

            return buildModelWithPattern(matcher, television.getModel());
        } else {
            if (!television.getModels().isEmpty()) {
                for (Map.Entry<Pattern, String> entry : television.getModels().entrySet()) {
                    Matcher modelMatcher = entry.getKey().matcher(userAgent);
                    if (modelMatcher.find()) {

                        return buildModelWithPattern(modelMatcher, entry.getValue());
                    }
                }
            }
        }

        return Optional.empty();
    }

    private Map<String, String> toMap(String brand, Optional<String> model) {

        Map<String, String> map = Maps.newHashMap();
        map.put(DEVICE_TYPE, TV.getDeviceName());
        model.ifPresent(val -> map.put(MODEL, val));
        if (StringUtils.isNotEmpty(brand)) {
            map.put(BRAND, brand);
        }

        return map;
    }
}
