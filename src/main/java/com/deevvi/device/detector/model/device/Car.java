package com.deevvi.device.detector.model.device;


import java.util.Map;
import java.util.regex.Pattern;

/**
 * Model for cars parser.
 */
public final class Car extends BasicDeviceWithModels {

    /**
     * Constructor.
     */
    private Car(Pattern pattern, String device, String brand, Map<Pattern, String> models) {

        super(pattern, device, brand, models);
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private Pattern regex;
        private String device;
        private String brand;
        private Map<Pattern, String> models;

        public Builder withRegex(Pattern regex) {

            this.regex = regex;
            return this;
        }

        public Builder withDevice(String device) {

            this.device = device;
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

        public Car build() {

            return new Car(regex, device, brand, models);
        }
    }
}
