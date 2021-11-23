package com.deevvi.device.detector.engine.parser;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.model.Bot;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Parser to determine if the input is a bot.
 */
public final class BotParser implements Parser, ListLoader<Bot> {

    /**
     * List with bots models.
     */
    private final List<Bot> bots = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        return bots.stream()
                .filter(bot -> bot.getPattern().matcher(userAgent).find())
                .findFirst()
                .map(this::encodeResponse).orElse(Maps.newHashMap());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bot toObject(Object rawObject) {
        Map map = (Map) rawObject;
        String name = (String) map.get(NAME);
        String category = (String) map.get(CATEGORY);
        String url = (String) map.get(URL);
        String producerUrl = null;
        String producerName = null;

        if (map.containsKey(PRODUCER)) {

            Map<String, String> producerMap = (Map) map.get(PRODUCER);
            producerName = producerMap.get(NAME);
            producerUrl = producerMap.get(URL);
        }

        return new Bot.Builder()
                .withRawRegex((String) map.get(REGEX))
                .withName(name)
                .withCategory(category)
                .withUrl(url)
                .withProducerName(producerName)
                .withProducerUrl(producerUrl)
                .build();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/bots.yml";
    }

    private Map<String, String> encodeResponse(Bot bot) {

        Map<String, String> map = Maps.newHashMap();
        map.put(NAME, bot.getName());
        if (StringUtils.isNotEmpty(bot.getCategory())) {
            map.put(CATEGORY, bot.getCategory());
        }
        if (StringUtils.isNotEmpty(bot.getUrl())) {
            map.put(URL, bot.getUrl());
        }
        if (StringUtils.isNotEmpty(bot.getProducerName())) {
            map.put(PRODUCER_NAME, bot.getProducerName());
        }
        if (StringUtils.isNotEmpty(bot.getProducerUrl())) {
            map.put(PRODUCER_URL, bot.getProducerUrl());
        }

        return map;
    }
}
