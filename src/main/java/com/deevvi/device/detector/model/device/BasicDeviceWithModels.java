package com.deevvi.device.detector.model.device;

import com.deevvi.device.detector.model.Model;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Base model for any client.
 */
public class BasicDeviceWithModels extends BasicDevice {

    /**
     * Map with device models along with compiled regexes for that model.
     */
    private final List<Model> models;

    /**
     * Constructor.
     */
    public BasicDeviceWithModels(String rawRegex, String device, String brand, List<Model> models) {

        super(rawRegex, device, brand);
        Preconditions.checkNotNull(models, "Models map cannot be null.");
        this.models = models;
    }

    /**
     * Get models.
     *
     * @return map with models
     */
    public List<Model> getModels() {
        return models;
    }
}
