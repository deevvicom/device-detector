package com.deevvi.device.detector.model.client;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Model for browser parser.
 */
public final class Browser extends Client {

    /**
     * Map with browser engines.
     */
    private final Map<String, String> engines;

    /**
     * Constructor.
     */
    private Browser(String name, Pattern pattern, String version, Map<String, String> engines) {

        super(name, pattern, version);
        Preconditions.checkNotNull(engines, "Engines map cannot be null or empty.");
        this.engines = engines;
    }

    /**
     * Get browser engines.
     *
     * @return browser engines.
     */
    public Map<String, String> getEngines() {

        return engines;
    }

    /**
     * Builder class.
     */
    public static class Builder {
        private String name;
        private Pattern pattern;
        private String version;
        private Map<String, String> engine;

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

        public Builder withEngine(Map<String, String> engine) {

            this.engine = engine;
            return this;
        }

        public Browser build() {

            return new Browser(name, pattern, version, engine);
        }
    }
}
