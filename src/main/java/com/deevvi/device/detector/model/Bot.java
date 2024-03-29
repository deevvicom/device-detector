package com.deevvi.device.detector.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Model for bots.
 */
public final class Bot implements PatternBuilder {

    private final Pattern pattern;
    private final String name;
    private final String category;
    private final String url;
    private final String producerName;
    private final String producerUrl;

    /**
     * Constructor.
     */
    private Bot( String rawRegex, String name, String category, String url, String producerName, String producerUrl) {

        Preconditions.checkNotNull(StringUtils.trimToNull(rawRegex), "Raw regex cannot be null or empty.");
        Preconditions.checkNotNull(StringUtils.trimToNull(name), "Operating system name cannot be null or empty.");

        this.pattern = toPattern(rawRegex);
        this.name = name;
        this.category = category;
        this.url = url;
        this.producerName = producerName;
        this.producerUrl = producerUrl;
    }

    public Pattern getPattern() {

        return pattern;
    }

    public String getName() {

        return name;
    }

    public String getCategory() {

        return category;
    }

    public String getUrl() {

        return url;
    }

    public String getProducerName() {

        return producerName;
    }

    public String getProducerUrl() {

        return producerUrl;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String rawRegex;
        private String name;
        private String category;
        private String url;
        private String producerName;
        private String producerUrl;

        public Builder withRawRegex(String rawRegex) {

            this.rawRegex = rawRegex;
            return this;
        }

        public Builder withName(String name) {

            this.name = name;
            return this;
        }

        public Builder withCategory(String category) {

            this.category = category;
            return this;
        }

        public Builder withUrl(String url) {

            this.url = url;
            return this;
        }

        public Builder withProducerName(String producerName) {

            this.producerName = producerName;
            return this;
        }

        public Builder withProducerUrl(String producerUrl) {

            this.producerUrl = producerUrl;
            return this;
        }

        public Bot build() {

            return new Bot(rawRegex, name, category, url, producerName, producerUrl);
        }
    }
}
