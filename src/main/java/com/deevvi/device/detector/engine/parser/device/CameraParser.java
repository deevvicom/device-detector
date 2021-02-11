package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.model.device.Camera;
import com.deevvi.device.detector.model.device.Device;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.deevvi.device.detector.engine.parser.device.DeviceType.CAMERA;

/**
 * Parser to validate if input is a camera engine.
 */
public final class CameraParser extends DeviceParser implements MapLoader<Camera> {

    /**
     * List with cameras models loaded from file.
     */
    private final List<Camera> cameras = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public Camera toObject(String key, Object value) {

        Map map = (Map) value;
        Map<Pattern, String> models = Maps.newLinkedHashMap();
        if (map.containsKey(MODELS)) {
            ((List) map.get(MODELS)).forEach(obj -> {
                Map modelEntry = (Map) obj;
                models.put(toPattern((String) modelEntry.get(REGEX)), (String) modelEntry.get(MODEL));
            });
        }

        return new Camera.Builder()
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
        return "/regexes/device/cameras.yml";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<? extends Device> getDevices() {
        return cameras;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDeviceType() {
        return CAMERA.getDeviceName();
    }
}
