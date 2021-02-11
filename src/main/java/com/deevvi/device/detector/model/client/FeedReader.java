package com.deevvi.device.detector.model.client;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Model for feed reader parser.
 */
public final class FeedReader extends Client {

    private final String url;
    private final String type;

    /**
     * Constructor.
     */
    private FeedReader(String name, Pattern pattern, String version, String url, String type) {

        super(name, pattern, version);
        Preconditions.checkNotNull(StringUtils.trimToNull(url), "Feed parser url cannot be null or empty.");
        Preconditions.checkNotNull(StringUtils.trimToNull(type), "Feed parser type cannot be null or empty.");

        this.url = url;
        this.type = type;
    }

    public String getUrl() {

        return url;
    }

    public String getType() {

        return type;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String name;
        private Pattern pattern;
        private String version;
        private String url;
        private String type;

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

        public Builder withUrl(String url) {

            this.url = url;
            return this;
        }

        public Builder withType(String type) {

            this.type = type;
            return this;
        }

        public FeedReader build() {

            return new FeedReader(name, pattern, version, url, type);
        }

    }
}
