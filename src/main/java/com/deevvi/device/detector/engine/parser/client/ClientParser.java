package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.engine.utils.Tuple;
import com.deevvi.device.detector.model.client.Client;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Parser facade specific for client section.
 */
abstract class ClientParser implements Parser {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        return getClients().stream().map(client -> new Tuple<>(client, client.getPattern().matcher(userAgent)))
                .filter(t -> t.getMatcher().find())
                .findFirst()
                .map(t -> toMap(buildByMatcher(t.getMatcher(),t.get().getName()), buildVersion(t.getMatcher(), t.get().getVersion())))
                .orElse(Maps.newHashMap());
    }

    /**
     * Return the list of clients read from file.
     *
     * @return list with clients
     */
    abstract List<? extends Client> getClients();

    /**`
     * Get media type to be serialized to output map
     *
     * @return media type
     */
    abstract String getMediaType();

    /**
     * Encode parsing response to a map with values.
     *
     * @param name    client name
     * @param version client version
     * @return map with parsing results.
     */
    private Map<String, String> toMap(Optional<String> name, Optional<String> version) {

        Map<String, String> map = Maps.newHashMap();
        map.put(DEVICE_TYPE, getMediaType());
        name.ifPresent( val -> map.put(NAME, val));
        version.ifPresent( val -> map.put(VERSION, val));

        return map;
    }
}
