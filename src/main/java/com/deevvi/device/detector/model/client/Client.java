package com.deevvi.device.detector.model.client;


import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Base model for any client.
 */
public class Client {

    /**
     * Client name.
     */
    private final String name;

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
    protected Client(String name, Pattern pattern, String version) {

        Preconditions.checkNotNull(StringUtils.trimToNull(name), "Name cannot be null or empty.");
        Preconditions.checkNotNull(pattern, "Regex pattern cannot be null.");

        this.name = name;
        this.pattern = pattern;
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
}
