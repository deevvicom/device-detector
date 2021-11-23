package com.deevvi.device.detector.model.device;

import com.deevvi.device.detector.model.PatternBuilder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Model for mobile parser.
 */
public final class Mobile extends BasicDevice {

    private final String model;
    private final List<Model> models;

    /**
     * Constructor.
     */
    private Mobile(String rawRegex, String device, String model, String brand, List<Model> models) {

        super(rawRegex, device, brand);
        this.model = model;
        this.models = models;
    }

    public String getModel() {
        return model;
    }

    public List<Model> getModels() {
        return models;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String rawRegex;
        private String device;
        private String model;
        private String brand;
        private List<Model> models;

        public Builder withRawRegex(String rawRegex) {

            this.rawRegex = rawRegex;
            return this;
        }
        public Builder withDevice(String device) {

            this.device = device;
            return this;
        }

        public Builder withModel(String model) {

            this.model = model;
            return this;
        }

        public Builder withBrand(String brand) {

            this.brand = brand;
            return this;
        }

        public Builder withModels(Model model) {

            if (models == null) {

                models = Lists.newArrayList();
            }

            models.add(model);
            return this;
        }

        public Builder withModels(List<Model> models) {

            this.models = models;
            return this;
        }

        public Mobile build() {

            return new Mobile(rawRegex, device, model, brand, models);
        }
    }

    public static class Model implements PatternBuilder {

        private final Pattern pattern;
        private final String device;
        private final String model;
        private final String brand;

        public Model(String rawRegex, String device, String model, String brand) {

            Preconditions.checkNotNull(StringUtils.trimToNull(rawRegex), "Raw regex cannot be null or empty.");

            this.pattern = toPattern(rawRegex);
            this.device = device;
            this.model = model;
            this.brand = brand;
        }

        public Pattern getRegex() {
            return pattern;
        }

        public String getDevice() {
            return device;
        }

        public String getModel() {
            return model;
        }

        public String getBrand() {
            return brand;
        }
    }
}
