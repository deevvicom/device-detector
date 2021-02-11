package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.engine.utils.ConfigUtils;
import com.deevvi.device.detector.model.client.BrowserEngine;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Parser to validate if input is a valid browser engine.
 */
public final class BrowserEngineParser implements Parser, ListLoader<BrowserEngine> {

    /**
     * List with browser engines.
     */
    private final List<String> availableEngines = ConfigUtils.fetchListFromFile("/configs/browserEngines.txt");

    /**
     * List of browsers engines.
     */
    private final List<BrowserEngine> browserEngines = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        return browserEngines.stream()
                .filter(engine -> engine.getPattern().matcher(userAgent).find() && availableEngines.contains(engine.getName()))
                .findFirst()
                .map(val -> ImmutableMap.of(ENGINE_NAME, val.getName())).orElse(ImmutableMap.of());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrowserEngine toObject(Object rawObject) {

        Map<String, String> map = (Map) rawObject;
        return new BrowserEngine.Builder()
                .withName(map.get(NAME))
                .withPattern(toPattern(map.get(REGEX)))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/client/browser_engine.yml";
    }
}
