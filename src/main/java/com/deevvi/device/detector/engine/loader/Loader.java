package com.deevvi.device.detector.engine.loader;


import com.deevvi.device.detector.model.exceptions.DeviceDetectorException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.regex.Pattern;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

public interface Loader {

    /**
     * Template used for building patterns from a regex.
     */
    String REGEX_PATTERN = "(?:^|[^A-Z0-9\\-_]|[^A-Z0-9\\-]_|sprd-)(?:%s)";

    /**
     * Class logger.
     */
    Logger LOG = LoggerFactory.getLogger(Loader.class);

    /**
     * YAML file parser.
     */
    Yaml YAML = new Yaml();

    /**
     * Return the path to config file from where to load specs.
     *
     * @return path to specs file.
     */
    String getFilePath();

    /**
     * Load YAML content from file.
     *
     * @return YAML file content
     */
    default Object loadFromFile() {

        String filePath = getFilePath();
        try {
            LOG.info("Reading configuration from file {}", getFilePath());
            return YAML.load(IOUtils.resourceToString(getFilePath(), UTF_8));
        } catch (Exception e) {
            throw new DeviceDetectorException(String.format("Unable to read config %s file. Invalid file handler.", filePath));
        }
    }

    /**
     * Convert a string to a compiled pattern, using the {@link Loader.REGEX_PATTERN} format.
     *
     * @param rawRegex regex, as string
     * @return pattern
     */
    default Pattern toPattern(String rawRegex) {

        return Pattern.compile(String.format(REGEX_PATTERN, rawRegex.replaceAll("/", "\\\\" + "/")), CASE_INSENSITIVE);
    }
}
