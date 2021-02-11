package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.model.client.Client;
import com.deevvi.device.detector.model.client.Library;

import java.util.List;
import java.util.Map;

/**
 * Parser to validate if input is a valid library reader.
 */
public final class LibraryParser extends ClientParser implements ListLoader<Library> {

    /**
     * String constants.
     */
    public static final String LIBRARY = "library";

    /**
     * List with libraries models.
     */
    private final List<Library> libraries = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Client> getClients() {
        return libraries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMediaType() {
        return LIBRARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Library toObject(Object rawObject) {

        Map<String, Object> map = (Map) rawObject;
        return new Library.Builder()
                .withName((String) map.get(NAME))
                .withPattern(toPattern((String) map.get(REGEX)))
                .withVersion((String) map.get(VERSION))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/client/libraries.yml";
    }
}
