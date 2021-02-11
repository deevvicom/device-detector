package com.deevvi.device.detector.model.exceptions;

/**
 * Runtime exception thrown to indicate an abnormal situation while running device detector.`
 */
public final class DeviceDetectorException extends RuntimeException {

    public DeviceDetectorException(String message) {
        super(message);
    }
}
