package com.deevvi.device.detector.model.client;

/**
 * Model for mobile application parser.
 */
public class MobileApp extends Client {

    /**
     * Constructor.
     */
    private MobileApp(String name, String rawRegex, String version) {

       super(name, rawRegex, version);
    }

    /**
     * Builder class.
     */
    public static class Builder {
        private String name;
        private String rawRegex;
        private String version;

        public Builder() {
        }

        public Builder withName(String name) {

            this.name = name;
            return this;
        }

        public Builder withRawRegex(String rawRegex) {

            this.rawRegex = rawRegex;
            return this;
        }

        public Builder withVersion(String version) {

            this.version = version;
            return this;
        }


        public MobileApp build() {

            return new MobileApp(name, rawRegex, version);
        }
    }
}
