package com.deevvi.device.detector.model.device;

import com.deevvi.device.detector.model.Model;
import com.deevvi.device.detector.model.client.BrowserEngine;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Model for camera parser.
 */
public final class Notebook extends Device {

    /**
     * Constructor.
     */
    private Notebook(String rawRegex, String device, String model, String brand,  List<Model> models) {

        super(rawRegex, device,model, brand, models);
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

        public Notebook build() {

            return new Notebook(rawRegex, device, model, brand, models);
        }
    }
}