package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.device.Device;
import com.deevvi.device.detector.model.device.PortableMediaPlayer;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.deevvi.device.detector.engine.parser.device.DeviceType.PORTABLE_MEDIA_PLAYER;

/**
 * Parser to validate if input is a portable media player.
 */
public final class PortableMediaPlayerParser extends DeviceParser implements Parser, MapLoader<PortableMediaPlayer> {

    /**
     * List with media players models loaded from file.
     */
    private final List<PortableMediaPlayer> mediaPlayers = streamToList();

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<? extends Device> getDevices() {
        return mediaPlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDeviceType() {
        return PORTABLE_MEDIA_PLAYER.getDeviceName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortableMediaPlayer toObject(String key, Object value) {
        Map map = (Map) value;
        Map<Pattern, String> models = Maps.newLinkedHashMap();
        if (map.containsKey(MODELS)) {
            ((List) map.get(MODELS)).forEach(obj -> {
                Map<String, String> modelEntry = (Map) obj;
                models.put(toPattern(modelEntry.get(REGEX)), modelEntry.get(MODEL));
            });
        }

        return new PortableMediaPlayer.Builder()
                .withDevice((String) map.get(DEVICE))
                .withPattern(toPattern((String) map.get(REGEX)))
                .withModel((String) map.getOrDefault(MODEL, EMPTY_STRING))
                .withBrand(key)
                .withModels(models)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/device/portable_media_player.yml";
    }
}
