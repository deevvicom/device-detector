package com.deevvi.device.detector.engine.loader;

import com.deevvi.device.detector.model.exceptions.DeviceDetectorException;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Process YAML file content to a list of {@link T} objects.
 */
public interface ListLoader<T> extends Loader {

    /**
     * Converts the YAML file content into a list of objects
     *
     * @return list of {@link T} objects.
     */
    default List<T> streamToList() {

        Object rawObject = loadFromFile();
        List<T> list = Lists.newArrayList();
        if (!(rawObject instanceof List)) {
            throw new DeviceDetectorException("Unable to convert input to list.");
        }
        ((List) rawObject).forEach(object -> list.add(toObject(object)));
        return list;
    }

    /**
     * Converts the raw object to a T
     *
     * @param rawObject raw object read from file
     * @return raw object converted to T
     */
     T toObject(Object rawObject);
}