package com.deevvi.device.detector.engine.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;

public final class ConfigUtils {

    /**
     * Class logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigUtils.class);

    /**
     * Private constructor to avoid class init.
     */
    private ConfigUtils() {
    }

    /**
     * Load configurations from a configuration file into a list of strings
     *
     * @param filePath path to file
     * @return list with strings
     */
    public static List<String> fetchListFromFile(String filePath) {
        try {
            return ImmutableList.copyOf(readFileContent(filePath));
        } catch (IOException e) {
            LOG.error("Unable to read config from file {}", filePath, e);
            System.exit(1);
            return Lists.newArrayList();
        }
    }

    /**
     * Load configurations from a config file into a map.
     *
     * @param filePath path to file
     * @return map with configurations
     */
    public static Map<String, String> fetchMapFromFile(String filePath) {
        try {
            Map<String, String> map = Maps.newHashMap();
            readFileContent(filePath).forEach(line -> {
                List<String> splits = Splitter.on("=").splitToList(line);
                if (splits.size() == 2) {
                    map.put(splits.get(0), splits.get(1));
                }
            });
            return Collections.unmodifiableMap(map);
        } catch (IOException e) {
            LOG.error("Unable to read config from file {}", filePath, e);
            System.exit(1);
            return Maps.newHashMap();
        }
    }

    private static List<String> readFileContent(String filePath) throws IOException {
        return Splitter.on("\n").splitToList(IOUtils.resourceToString(filePath, UTF_8));
    }
}
