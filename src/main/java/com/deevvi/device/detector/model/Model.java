package com.deevvi.device.detector.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Pojo for any device with models.
 */
public class Model implements PatternBuilder{

    /**
     * Raw regex;
     */
    private final String rawRegex;

    /**
     * Compiled regex user to determine a device.
     */
    private final Pattern pattern;

    /**
     * Model
     */
    private final String model;

    public Model(String rawRegex, String model) {

        Preconditions.checkNotNull(StringUtils.trimToNull(rawRegex), "Raw regex cannot be null or empty.");

        this.rawRegex = rawRegex;
        this.pattern = toPattern(rawRegex);
        this.model = model;
    }

    public String getRawRegex() {
        return rawRegex;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getModel() {
        return model;
    }
}
