package com.deevvi.device.detector.model.client;

import com.deevvi.device.detector.model.PatternBuilder;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Model for browser engine parser.
 */
public final class BrowserEngine implements PatternBuilder {

    private final String name;
    private final String rawRegex;
    private final Pattern pattern;

    /**
     * Constructor.
     */
    private BrowserEngine(String name, String rawRegex) {

        Preconditions.checkNotNull(StringUtils.trimToNull(name), "Name cannot be null or empty.");
        Preconditions.checkNotNull(StringUtils.trimToNull(rawRegex), "Raw regex cannot be null or empty.");

        this.name = name;
        this.rawRegex = rawRegex;
        this.pattern = toPattern(rawRegex);
    }

    public String getName() {

        return name;
    }

    public Pattern getPattern() {

        return pattern;
    }

    public String getRawRegex() {
        return rawRegex;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String name;
        private String rawRegex;

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

        public BrowserEngine build() {

            return new BrowserEngine(name, rawRegex);
        }
    }
}
