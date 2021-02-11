package com.deevvi.device.detector.model.device;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Base model for any client.
 */
public class BasicDeviceWithModels extends BasicDevice {

    /**
     * Map with device models along with compiled regexes for that model.
     */
    private final Map<Pattern, String> models;

    /**
     * Constructor.
     */
    public BasicDeviceWithModels(Pattern pattern, String device, String brand, Map<Pattern, String> models) {

        super(pattern, device, brand);
        Preconditions.checkNotNull(models, "Models map cannot be null.");
        this.models = models;
    }

    /**
     * Get models.
     *
     * @return map with models
     */
    public Map<Pattern, String> getModels() {
        return models;
    }
}
