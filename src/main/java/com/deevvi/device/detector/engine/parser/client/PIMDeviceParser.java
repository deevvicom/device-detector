package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.model.client.Client;
import com.deevvi.device.detector.model.client.PIMDevice;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                .withRawRegex(map.get(REGEX))
                .withVersion(map.get(VERSION))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        List<String> list = getClients().stream().map(Client::getRawRegex).collect(Collectors.toList());
        if (!preMatchOverall(list, userAgent)) {
            return Maps.newHashMap();
        }

        return super.parse(userAgent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/client/pims.yml";
    }
}
