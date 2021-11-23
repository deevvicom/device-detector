package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.Model;
import com.deevvi.device.detector.model.device.Device;
import com.deevvi.device.detector.model.device.Notebook;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import static com.deevvi.device.detector.engine.parser.device.DeviceType.DESKTOP;

public final class NotebookParser extends DeviceParser implements Parser, MapLoader<Notebook> {

    /**
     * List with notebooks models loaded from file.
     */
    private final List<Notebook> notebooks = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<? extends Device> getDevices() {
        return notebooks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDeviceType() {
        return DESKTOP.getDeviceName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notebook toObject(String key, Object value) {
        Map map = (Map) value;
        List<Model> models = Lists.newArrayList();
        if (map.containsKey(MODELS)) {
            ((List) map.get(MODELS)).forEach(obj -> {
                Map modelEntry = (Map) obj;
                models.add(new Model(((String) modelEntry.get(REGEX)), (String) modelEntry.get(MODEL)));
            });
        }

        return new Notebook.Builder()
                .withDevice((String) map.get(DEVICE))
                .withBrand(key)
                .withRawRegex((String) map.get(REGEX))
                .withModel((String) map.getOrDefault(MODEL, EMPTY_STRING))
                .withModels(models)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/device/notebooks.yml";
    }
}
