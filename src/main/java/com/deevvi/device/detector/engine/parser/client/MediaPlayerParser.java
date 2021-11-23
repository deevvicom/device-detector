package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.model.client.Client;
import com.deevvi.device.detector.model.client.MediaPlayer;

import java.util.List;
import java.util.Map;

/**
 * Parser to validate if input is a valid media player.
 */
public final class MediaPlayerParser extends ClientParser implements ListLoader<MediaPlayer> {

    /**
     * String constants.
     */
    private static final String MEDIAPLAYER = "mediaplayer";

    /**
     * List with media players models.
     */
    private final List<MediaPlayer> mediaPlayers = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Client> getClients() {
        return mediaPlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMediaType() {
        return MEDIAPLAYER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediaPlayer toObject(Object rawObject) {

        Map<String, Object> map = (Map) rawObject;
        return new MediaPlayer.Builder()
                .withName((String) map.get(NAME))
                .withRawRegex((String) map.get(REGEX))
                .withVersion((String) map.get(VERSION))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/client/mediaplayers.yml";
    }
}
