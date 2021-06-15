package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.engine.utils.Tuple;
import com.deevvi.device.detector.model.device.DeviceInfo;
import com.deevvi.device.detector.model.device.Mobile;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;

import static com.deevvi.device.detector.engine.parser.Parser.checkContainsPlaceholder;

/**
 * Parser to validate if input is a mobile device.
 */
public final class MobileParser implements Parser, MapLoader<Mobile> {

    /**
     * List with mobiles models.
     */
    private final List<Mobile> mobiles = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        return mobiles.stream()
                .map(mobile -> new Tuple<>(mobile, mobile.getPattern().matcher(userAgent)))
                .filter(t -> t.getMatcher().find())
                .findFirst()
                .map(t -> buildResult(userAgent, t))
                .orElse(Maps.newHashMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/device/mobiles.yml";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mobile toObject(String key, Object value) {
        Map map = (Map) value;
        String regex = (String) map.get(REGEX);
        String device = (String) map.get(DEVICE);
        String model = EMPTY_STRING;
        if (map.containsKey(MODEL)) {
            model = (String) map.get(MODEL);
        }
        List<Mobile.Model> models = Lists.newArrayList();
        if (map.containsKey(MODELS)) {

            ((List) map.get(MODELS)).forEach(obj -> {

                Map modelMap = (Map) obj;
                String specificModel = null;
                if (modelMap.containsKey(MODEL)) {
                    specificModel = (String) modelMap.get(MODEL);
                }
                String specificBrand = null;
                if (modelMap.containsKey(BRAND)) {
                    specificBrand = (String) modelMap.get(BRAND);
                }
                String specificDevice = null;
                if (modelMap.containsKey(DEVICE)) {
                    specificDevice = (String) modelMap.get(DEVICE);
                }

                models.add(new Mobile.Model(
                        toPattern((String) (modelMap.get(REGEX))),
                        specificDevice,
                        specificModel,
                        specificBrand));
            });
        }

        return new Mobile.Builder()
                .withDevice(device)
                .withPattern(toPattern(regex))
                .withModel(model)
                .withBrand(key)
                .withModels(models)
                .build();
    }

    private Optional<String> buildModel(String model, Matcher matcher) {
        try {
            return buildByMatcher(matcher, model).map(val -> Parser.clear(val).trim());
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    private Optional<DeviceInfo> fetchInfo(Mobile mobile, String userAgent) {

        if (hasNoModels(mobile)) {
            return Optional.empty();
        } else {
            for (Mobile.Model entry : mobile.getModels()) {
                Matcher modelMatcher = entry.getRegex().matcher(userAgent);
                if (modelMatcher.find()) {
                    String localModel = mobile.getModel();
                    if (entry.getModel() != null) {
                        localModel = entry.getModel();
                    }
                    String localBrand = mobile.getBrand();
                    if (entry.getBrand() != null) {
                        localBrand = entry.getBrand();
                    }
                    String localDevice = mobile.getDevice();
                    if (entry.getDevice() != null) {
                        localDevice = entry.getDevice();
                    }

                    return Optional.of(buildDeviceInfo(modelMatcher, localModel, localBrand, localDevice));
                }
            }
        }

        return Optional.of(new DeviceInfo(mobile.getBrand(), mobile.getDevice(), ""));
    }

    private boolean hasNoModels(Mobile mobile) {
        return mobile.getModels() == null || mobile.getModels().isEmpty();
    }

    private DeviceInfo buildDeviceInfo(Matcher matcher, String model, String brand, String device) {

        int index = getStartIndex(model);
        if (index == 0) {

            return new DeviceInfo(brand, device, model);
        }
        String group = "$";
        while (model.contains(group + index)) {
            String current = matcher.group(index);
            if (current == null) {

                model = model.replace("$" + index, "");
            } else {

                model = model.replace("$" + index, current);
            }
            index++;
        }

        return new DeviceInfo(brand, device, model);
    }

    private Map<String, String> buildResult(String userAgent, Tuple<Mobile> t) {
        final AtomicReference<String> model = new AtomicReference();
        final AtomicReference<String> brand = new AtomicReference();
        final AtomicReference<String> deviceType = new AtomicReference();
        Optional<DeviceInfo> deviceInfo = fetchInfo(t.get(), userAgent);
        if (deviceInfo.isPresent()) {
            DeviceInfo info = deviceInfo.get();

            model.set(info.getModel());
            brand.set(info.getBrand());
            deviceType.set(info.getDevice());
        } else {
            deviceType.set(t.get().getDevice());
            brand.set(t.get().getBrand());

            if (brand.get().equalsIgnoreCase(UNKNOWN)) {
                brand.set(EMPTY_STRING);
            }
            model.set(buildModel(t.get().getModel(), t.getMatcher()).orElse(EMPTY_STRING));
        }

        return encodeResponse(deviceType.get(), model.get(), brand.get());
    }

    private Map<String, String> encodeResponse(String deviceType, String model, String brand) {

        if (checkContainsPlaceholder(deviceType) || checkContainsPlaceholder(model) || checkContainsPlaceholder(brand)) {
            return Maps.newHashMap();
        }

        Map<String, String> map = Maps.newHashMap();
        map.put(DEVICE_TYPE, StringUtils.trimToEmpty(deviceType));
        map.put(MODEL, StringUtils.trimToEmpty(model));
        map.put(BRAND, StringUtils.trimToEmpty(brand));

        return map;
    }
}
