package com.deevvi.device.detector.model.device;

import java.util.regex.Pattern;

/**
 * Model for shell tv parser.
 */
public final class ShellTv extends BasicDevice {

    private final String model;

    /**
     * Constructor.
     */
    private ShellTv(Pattern pattern, String device, String brand, String model) {

        super(pattern, device, brand);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private Pattern pattern;
        private String device;
        private String model;
        private String brand;

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

        public ShellTv build() {

            return new ShellTv(pattern, device, brand, model);
        }
    }
}
