package com.deevvi.device.detector.model.client;

/**
 * Model for media player parser.
 */
public final class MediaPlayer extends Client {

    /**
     * Constructor.
     */
    private MediaPlayer(String name, String rawRegex, String version) {

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


        public MediaPlayer build() {

            return new MediaPlayer(name, rawRegex, version);
        }
    }
}
