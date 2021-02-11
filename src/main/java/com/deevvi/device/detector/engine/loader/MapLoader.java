package com.deevvi.device.detector.engine.loader;

import com.deevvi.device.detector.model.exceptions.DeviceDetectorException;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public interface MapLoader<T> extends Loader {

    /**
     * Converts the YAML file content into a list of objects
     *
     * @return list of {@link T} objects.
     */
    default List<T> streamToList() {

        Object rawObject = loadFromFile();
        List<T> list = Lists.newArrayList();
        if (!(rawObject instanceof Map)) {
            throw new DeviceDetectorException("Unable to convert input to list.");
        }
        ((Map) rawObject).forEach((key, val) -> list.add(toObject((key instanceof String ? (String) key : String.valueOf(key)), val)));
        return list;
    }

    /**
     * Converts the raw object to a T
     *
     * @param key   key of map entry read from file.
     * @param value plain value of object read from file.
     * @return raw object converted to T
     */
    T toObject(String key, Object value);
}
