package com.deevvi.device.detector.model.client;

import java.util.regex.Pattern;

/**
 * Model for mobile application parser.
 */
public class MobileApp extends Client {

    /**
     * Constructor.
     */
    private MobileApp(String name, Pattern pattern, String version) {

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


        public MobileApp build() {

            return new MobileApp(name, pattern, version);
        }
    }
}
