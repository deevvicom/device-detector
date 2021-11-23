package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.model.client.Client;
import com.deevvi.device.detector.model.client.FeedReader;

import java.util.List;
import java.util.Map;

/**
 * Parser to validate if input is a valid feed reader.
 */
public final class FeedReaderParser extends ClientParser implements ListLoader<FeedReader> {

    /**
     * String constants.
     */
    private static final String FEED_READER = "feed reader";

    /**
     * List of feed readers.
     */
    private final List<FeedReader> feedReaders = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Client> getClients() {
        return feedReaders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMediaType() {
        return FEED_READER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeedReader toObject(Object rawObject) {

        Map<String, Object> map = (Map) rawObject;
        return new FeedReader.Builder()
                .withName((String) map.get(NAME))
                .withType((String) map.get(TYPE))
                .withUrl((String) map.get(URL))
                .withVersion((String) map.get(VERSION))
                .withRawRegex((String) map.get(REGEX))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/client/feed_readers.yml";
    }
}
