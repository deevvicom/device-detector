package com.deevvi.device.detector.model.device;

/**
 * Model for shell tv parser.
 */
public final class ShellTv extends BasicDevice {

    private final String model;

    /**
     * Constructor.
     */
    private ShellTv(String rawRegex, String device, String brand, String model) {

        super(rawRegex, device, brand);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String rawRegex;
        private String device;
        private String model;
        private String brand;

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

        public ShellTv build() {

            return new ShellTv(rawRegex, device, brand, model);
        }
    }
}
