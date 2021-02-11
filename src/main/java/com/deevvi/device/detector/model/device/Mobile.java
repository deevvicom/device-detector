package com.deevvi.device.detector.model.device;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
    private Mobile(Pattern pattern, String device, String model, String brand, List<Model> models) {

        super(pattern, device, brand);
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

        private Pattern pattern;
        private String device;
        private String model;
        private String brand;
        private List<Model> models;

        public Builder withPattern(Pattern pattern) {

            this.pattern = pattern;
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

            return new Mobile(pattern, device, model, brand, models);
        }
    }

    public static class Model {

        private final Pattern pattern;
        private final String device;
        private final String model;
        private final String brand;

        public Model(Pattern pattern, String device, String model, String brand) {

            Preconditions.checkNotNull(pattern, "Regex pattern cannot be null.");

            this.pattern = pattern;
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
