package com.sofi.apinfcbluetooth.domain.device.port;

public interface AccessibilityBroadcaster {

    void startServer();

    void stopServer();

    void broadcast(String message);

    boolean isClientConnected();
}
