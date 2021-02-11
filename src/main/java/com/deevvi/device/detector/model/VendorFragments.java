package com.deevvi.device.detector.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Model for vendor fragments.
 */
public final class VendorFragments {

    private final String vendor;
    private final List<Pattern> patterns;

    /**
     * Constructor.
     */
    private VendorFragments(String vendor, List<Pattern> patterns) {

        Preconditions.checkNotNull(StringUtils.trimToNull(vendor), "Vendor cannot be null or empty.");
        Preconditions.checkNotNull(patterns, "Pattern list cannot be null.");

        this.vendor = vendor;
        this.patterns = patterns;
    }

    public String getVendor() {

        return vendor;
    }

    public List<Pattern> getPatterns() {

        return patterns;
    }

    /**
     * Builder class.
     */
    public static class Builder {

        private String vendor;
        private List<Pattern> patterns;

        public Builder withVendor(String vendor) {

            this.vendor = vendor;
            return this;
        }

        public Builder withPatterns(List<Pattern> patterns) {

            this.patterns = patterns;
            return this;
        }

        public VendorFragments build() {

            return new VendorFragments(vendor, patterns);
        }
    }
}
