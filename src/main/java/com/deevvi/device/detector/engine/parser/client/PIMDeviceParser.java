package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.model.client.Client;
import com.deevvi.device.detector.model.client.PIMDevice;

import java.util.List;
import java.util.Map;

/**
 * Parser to validate if input is a pim application.
 */
public final class PIMDeviceParser extends ClientParser implements ListLoader<PIMDevice> {

    /**
     * String constants.
     */
    public static final String PIM = "pim";

    /**
     * List with PIM device models.
     */
    private final List<PIMDevice> pimDevices = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Client> getClients() {
        return pimDevices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMediaType() {
        return PIM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PIMDevice toObject(Object rawObject) {

        Map<String, String> map = (Map) rawObject;
        return new PIMDevice.Builder()
                .withName(map.get(NAME))
                .withPattern(toPattern(map.get(REGEX)))
                .withVersion(map.get(VERSION))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/client/pims.yml";
    }
}
