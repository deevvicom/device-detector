package com.deevvi.device.detector.model.device;


import com.deevvi.device.detector.model.Model;

import java.util.List;

/**
 * Model for cars parser.
 */
public final class Car extends BasicDeviceWithModels {

    /**
     * Constructor.
     */
    private Car(String rawRegex, String device, String brand, List<Model> models) {

        super(rawRegex, device, brand, models);
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String rawRegex;
        private String device;
        private String brand;
        private List<Model> models;

        public Builder withRegex(String rawRegex) {

            this.rawRegex = rawRegex;
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

        public Builder withModels(List<Model> models) {

            this.models = models;
            return this;
        }

        public Car build() {

            return new Car(rawRegex, device, brand, models);
        }
    }
}
