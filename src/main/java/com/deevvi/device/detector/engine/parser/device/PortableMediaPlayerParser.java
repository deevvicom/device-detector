package com.deevvi.device.detector.engine.parser.device;

import com.deevvi.device.detector.engine.loader.MapLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.model.Model;
import com.deevvi.device.detector.model.device.BasicDevice;
import com.deevvi.device.detector.model.device.Device;
import com.deevvi.device.detector.model.device.PortableMediaPlayer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Map<String, String> parse(String userAgent) {
        List<String> regexes = getDevices().stream().map(BasicDevice::getRawRegex).collect(Collectors.toList());

        if (preMatchOverall(regexes, userAgent)) {
            return super.parse(userAgent);
        }

        return Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortableMediaPlayer toObject(String key, Object value) {
        Map map = (Map) value;
        List<Model> models = Lists.newArrayList();
        if (map.containsKey(MODELS)) {
            ((List) map.get(MODELS)).forEach(obj -> {
                Map<String, String> modelEntry = (Map) obj;
                models.add(new Model(modelEntry.get(REGEX), modelEntry.get(MODEL)));
            });
        }

        return new PortableMediaPlayer.Builder()
                .withDevice((String) map.get(DEVICE))
                .withRawRegex((String) map.get(REGEX))
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
