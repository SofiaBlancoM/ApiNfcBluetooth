package com.sofi.apinfcbluetooth.infrastructure.device.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.sofi.apinfcbluetooth.domain.device.port.AccessibilityBroadcaster;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BluetoothAccessibilityServer implements AccessibilityBroadcaster {

    private static final String TAG =
            "BluetoothAccessibility";

    private static final String SERVICE_NAME =
            "NFCAccessibilityService";

    private static final UUID SERVICE_UUID =
            UUID.fromString(
                    "00001101-0000-1000-8000-00805F9B34FB"
            );

    private final BluetoothAdapter bluetoothAdapter;

    private BluetoothServerSocket serverSocket;

    private BluetoothSocket connectedSocket;

    private Thread serverThread;

    public BluetoothAccessibilityServer() {

        bluetoothAdapter =
                BluetoothAdapter.getDefaultAdapter();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void startServer() {

        if (bluetoothAdapter == null) {

            Log.e(TAG,
                    "Bluetooth no soportado");

            return;
        }

        if (!bluetoothAdapter.isEnabled()) {

            Log.e(TAG,
                    "Bluetooth desactivado");

            return;
        }

        serverThread = new Thread(() -> {

            try {

                serverSocket =
                        bluetoothAdapter
                                .listenUsingRfcommWithServiceRecord(
                                        SERVICE_NAME,
                                        SERVICE_UUID
                                );

                Log.d(TAG,
                        "Servidor Bluetooth iniciado");

                while (!Thread.currentThread().isInterrupted()) {

                    Log.d(TAG,
                            "Esperando conexión...");

                    connectedSocket =
                            serverSocket.accept();

                    if (connectedSocket != null) {

                        Log.d(TAG,
                                "Cliente conectado: "
                                        + connectedSocket
                                        .getRemoteDevice()
                                        .getName());
                    }
                }

            } catch (Exception ex) {

                Log.e(TAG,
                        "Error servidor Bluetooth",
                        ex);
            }
        });

        serverThread.start();
    }

    @Override
    public void stopServer() {

        try {

            if (connectedSocket != null) {
                connectedSocket.close();
            }

            if (serverSocket != null) {
                serverSocket.close();
            }

            if (serverThread != null) {
                serverThread.interrupt();
            }

        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean isClientConnected() {

        return connectedSocket != null
                && connectedSocket.isConnected();
    }

    @Override
    public void broadcast(String message) {

        if (!isClientConnected()) {

            Log.d(TAG,
                    "No hay cliente conectado");

            return;
        }

        try {

            OutputStream outputStream =
                    connectedSocket.getOutputStream();

            outputStream.write(
                    message.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            outputStream.flush();

            Log.d(TAG,
                    "Mensaje enviado:\n" + message);

        } catch (IOException ex) {

            Log.e(TAG,
                    "Error enviando mensaje",
                    ex);
        }
    }
}
