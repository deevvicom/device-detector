package com.deevvi.device.detector.model.client;

import java.util.regex.Pattern;

/**
 * Model for personal information manager parser.
 */
public final class PIMDevice extends Client{

    /**
     * Constructor.
     */
    private PIMDevice(String name, Pattern pattern, String version) {

        super(name, pattern, version);
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String name;
        private Pattern pattern;
        private String version;

        public Builder() {
        }

        public Builder withName(String name) {

            this.name = name;
            return this;
        }

       public Builder withPattern(Pattern pattern) {

            this.pattern = pattern;
            return this;
        }

        public Builder withVersion(String version) {

            this.version = version;
            return this;
        }


        public PIMDevice build() {

            return new PIMDevice(name, pattern, version);
        }
    }
}
