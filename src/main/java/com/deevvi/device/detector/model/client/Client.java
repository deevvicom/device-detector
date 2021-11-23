package com.deevvi.device.detector.model.client;


import com.deevvi.device.detector.model.PatternBuilder;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Base model for any client.
 */
public class Client implements PatternBuilder {

    /**
     * Client name.
     */
    private final String name;

    /**
     * Raw regex;
     */
    private final String rawRegex;
    /**
     * Compiled regex used to detect a client.
     */
    private final Pattern pattern;

    /**
     * Client version.
     */
    private final String version;

    /**
     * Constructor.
     */
    protected Client(String name, String rawRegex, String version) {

        Preconditions.checkNotNull(StringUtils.trimToNull(name), "Name cannot be null or empty.");
        Preconditions.checkNotNull(StringUtils.trimToNull(rawRegex), "Raw regex cannot be null or empty.");

        this.name = name;
        this.rawRegex = rawRegex;
        this.pattern = toPattern(rawRegex);
        this.version = version;
    }

    /**
     * Get client name
     *
     * @return client name
     */
    public String getName() {

        return name;
    }

    /**
     * Get compiled regex.
     *
     * @return pattern
     */
    public Pattern getPattern() {

        return pattern;
    }

    /**
     * Get client version.
     *
     * @return client version
     */
    public String getVersion() {

        return version;
    }

    /**
     * Get raw regex.
     *
     * @return raw regex
     */
    public String getRawRegex() {
        return rawRegex;
    }
}
