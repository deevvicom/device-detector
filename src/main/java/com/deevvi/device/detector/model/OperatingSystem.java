package com.deevvi.device.detector.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Model for operating system.
 */
public final class OperatingSystem {

    private final Pattern pattern;
    private final String name;
    private final String version;

    /**
     * Constructor.
     */
    private OperatingSystem(Pattern pattern, String name, String version) {

        Preconditions.checkNotNull(pattern, "Regex pattern cannot be null or empty.");
        Preconditions.checkNotNull(StringUtils.trimToNull(name), "Operating system name cannot be null or empty.");

        this.pattern = pattern;
        this.name = name;
        this.version = version;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private Pattern pattern;
        private String name;
        private String version;

        public Builder withPattern(Pattern pattern) {

            this.pattern = pattern;
            return this;
        }

        public Builder withName(String name) {

            this.name = name;
            return this;
        }

        public Builder withVersion(String version) {

            this.version = version;
            return this;
        }

        public OperatingSystem build() {

            return new OperatingSystem(pattern, name, version);
        }
    }
}
