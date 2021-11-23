package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.model.client.Client;
import com.deevvi.device.detector.model.client.MobileApp;

import java.util.List;
import java.util.Map;

/**
 * Parser to validate if input is a valid mobile application.
 */
public final class MobileAppParser extends ClientParser implements ListLoader<MobileApp> {

    /**
     * String constants.
     */
    public static final String MOBILE_APP = "mobile app";

    /**
     * List with mobile apps models.
     */
    private final List<MobileApp> mobileApps = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Client> getClients() {
        return mobileApps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMediaType() {
        return MOBILE_APP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MobileApp toObject(Object rawObject) {

        Map<String, Object> map = (Map) rawObject;
        return new MobileApp.Builder()
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
        return "/regexes/client/mobile_apps.yml";
    }
}

