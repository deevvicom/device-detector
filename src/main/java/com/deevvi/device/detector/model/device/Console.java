package com.deevvi.device.detector.model.device;

import com.deevvi.device.detector.model.Model;

import java.util.List;

/**
 * Model for console parser.
 */
public final class Console extends Device {

    /**
     * Constructor.
     */
    private Console(String rawRegex, String device, String model, String brand, List<Model> models) {

        super(rawRegex, device, model, brand, models);
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

        public Builder withModels(List<Model> models) {

            this.models = models;
            return this;
        }

        public Console build() {

            return new Console(rawRegex, device, model, brand, models);
        }
    }
}
