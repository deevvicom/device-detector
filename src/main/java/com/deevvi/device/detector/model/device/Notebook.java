package com.deevvi.device.detector.model.device;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Model for camera parser.
 */
public final class Notebook extends Device {

    /**
     * Constructor.
     */
    private Notebook(Pattern pattern, String device, String model, String brand, Map<Pattern, String> models) {

        super(pattern, device,model, brand, models);
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private Pattern pattern;
        private String device;
        private String model;
        private String brand;
        private Map<Pattern, String> models;

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

        public Builder withModels(Map<Pattern, String> models) {

            this.models = models;
            return this;
        }

        public Notebook build() {

            return new Notebook(pattern, device, model, brand, models);
        }
    }
}