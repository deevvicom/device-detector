package com.deevvi.device.detector.model.client;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Model for browser engine parser.
 */
public final class BrowserEngine {

    private final String name;
    private final Pattern pattern;

    /**
     * Constructor.
     */
    private BrowserEngine(String name, Pattern pattern) {

        Preconditions.checkNotNull(StringUtils.trimToNull(name), "Name cannot be null or empty.");
        Preconditions.checkNotNull(pattern, "Pattern cannot be null.");

        this.name = name;
        this.pattern = pattern;
    }

    public String getName() {

        return name;
    }

    public Pattern getPattern() {

        return pattern;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String name;
        private Pattern pattern;

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

        public BrowserEngine build() {

            return new BrowserEngine(name, pattern);
        }
    }
}
