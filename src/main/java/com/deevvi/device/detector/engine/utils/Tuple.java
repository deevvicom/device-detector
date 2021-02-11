package com.deevvi.device.detector.engine.utils;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;

/**
 * Pojo class that keeps related info loaded from file about client or device or OS and a regex matcher.
 */
public final class Tuple<T> {

    /**
     * Client info.
     */
    private final T t;

    /**
     * Regex matcher
     */
    private final Matcher matcher;

    /**
     * Constructor.
     */
    public Tuple(T t, Matcher matcher) {

        Preconditions.checkNotNull(t, "Client cannot be null.");
        Preconditions.checkNotNull(matcher, "Matcher cannot be null.");

        this.t = t;
        this.matcher = matcher;
    }

    /**
     * Get client.
     *
     * @return client
     */
    public T get() {
        return t;
    }

    /**
     * Get matcher.
     *
     * @return matcher
     */
    public Matcher getMatcher() {
        return matcher;
    }
}
