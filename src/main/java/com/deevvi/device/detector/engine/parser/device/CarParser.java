package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.Model;
import com.deevvi.device.detector.model.device.Car;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

import static com.deevvi.device.detector.engine.parser.device.DeviceType.CAR_BROWSER;

/**
 * Parser to validate if input is a car browser.
 */
public final class CarParser implements Parser, MapLoader<Car> {

    /**
     * List with cars models loaded from file.
     */
    private final List<Car> cars = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        return cars
                .stream()
                .filter(car -> car.getPattern().matcher(userAgent).find())
                .findFirst()
                .map(car -> toMap(buildModel(car, userAgent), car.getBrand()))
                .orElse(Maps.newHashMap());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Car toObject(String key, Object value) {

        Map map = (Map) value;
        List<Model> models = Lists.newArrayList();
        if (map.containsKey(MODELS)) {
            ((List) map.get(MODELS)).forEach(obj -> {
                Map modelEntry = (Map) obj;
                models.add(new Model ((String) modelEntry.get(REGEX), (String) modelEntry.get(MODEL)));
            });
        }

        return new Car.Builder()
                .withDevice((String) map.get(DEVICE))
                .withRegex(((String) map.get(REGEX)))
                .withBrand(key)
                .withModels(models)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/device/cars.yml";
    }

    private Optional<String> buildModel(Car car, String userAgent) {

        for(Model model: car.getModels()){
            Matcher modelMatcher = model.getPattern().matcher(userAgent);
            if (modelMatcher.find()) {
                return Optional.of(model.getModel());
            }
        }

        return Optional.empty();
    }

    private Map<String, String> toMap(Optional<String> model, String brand) {

        Map<String, String> map = Maps.newHashMap();
        map.put(DEVICE_TYPE, CAR_BROWSER.getDeviceName());
        model.ifPresent(val -> map.put(MODEL, val));
        if (StringUtils.isNotEmpty(brand)) {
            map.put(BRAND, brand);
        }

        return map;
    }
}
