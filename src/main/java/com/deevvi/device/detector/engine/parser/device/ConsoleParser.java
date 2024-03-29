package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.Model;
import com.deevvi.device.detector.model.device.Console;
import com.deevvi.device.detector.model.device.Device;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import static com.deevvi.device.detector.engine.parser.device.DeviceType.CONSOLE;

/**
 * Parser to validate if input is a console.
 */
public final class ConsoleParser extends DeviceParser implements Parser, MapLoader<Console> {

    /**
     * List with consoles models loaded from file.
     */
    private final List<Console> consoles = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<? extends Device> getDevices() {
        return consoles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDeviceType() {
        return CONSOLE.getDeviceName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Console toObject(String key, Object value) {

        Map map = (Map) value;
        List<Model> models = Lists.newArrayList();
        if (map.containsKey(MODELS)) {
            ((List) map.get(MODELS)).forEach(obj -> {
                Map modelEntry = (Map) obj;
                models.add(new Model((String) modelEntry.get(REGEX), (String) modelEntry.get(MODEL)));
            });
        }

        return new Console.Builder()
                .withDevice((String) map.get(DEVICE))
                .withRawRegex((String) map.get(REGEX))
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
        return "/regexes/device/consoles.yml";
    }
}
