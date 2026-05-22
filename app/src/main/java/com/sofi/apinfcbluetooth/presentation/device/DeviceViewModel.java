package com.sofi.apinfcbluetooth.presentation.device;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import com.sofi.apinfcbluetooth.R;
import com.sofi.apinfcbluetooth.application.device.service.DeviceErrorMapper;
import com.sofi.apinfcbluetooth.application.device.usecase.CheckNfcAvailabilityUseCase;
import com.sofi.apinfcbluetooth.application.device.usecase.ReadNfcTagUseCase;
import com.sofi.apinfcbluetooth.domain.device.model.DeviceAvailability;
import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.domain.device.port.AccessibilityBroadcaster;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;
import android.content.Context;

public class DeviceViewModel extends ViewModel {

    private final CheckNfcAvailabilityUseCase checkNfcAvailabilityUseCase;
    private final ReadNfcTagUseCase readNfcTagUseCase;
    private final DeviceErrorMapper errorMapper;

    private final AccessibilityBroadcaster accessibilityBroadcaster;

    private final DeviceUiState uiState = new DeviceUiState();

    private final Context context;

    public DeviceViewModel(
            Context context,
            CheckNfcAvailabilityUseCase checkNfcAvailabilityUseCase,
            ReadNfcTagUseCase readNfcTagUseCase,
            DeviceErrorMapper errorMapper,
            AccessibilityBroadcaster accessibilityBroadcaster
    ) {
        this.context = context.getApplicationContext();
        this.checkNfcAvailabilityUseCase = checkNfcAvailabilityUseCase;
        this.readNfcTagUseCase = readNfcTagUseCase;
        this.errorMapper = errorMapper;
        this.accessibilityBroadcaster = accessibilityBroadcaster;
        this.accessibilityBroadcaster.startServer();
    }

    public DeviceUiState getUiState() {
        return uiState;
    }

    public boolean isAccessibilityClientConnected() {

        return accessibilityBroadcaster
                .isClientConnected();
    }

    public void broadcastInitialScreen() {

        accessibilityBroadcaster.broadcast(
                context.getString(R.string.nfc_screen)
                        + context.getString(R.string.accessibility_button)
                        + context.getString(R.string.enable_button)
                        + context.getString(R.string.read_button)
        );
    }

    public void enableNfcReader() {
        DeviceAvailability availability = checkNfcAvailabilityUseCase.execute();

        switch (availability) {
            case READY:
                uiState.setErrorMessage(null);
                uiState.setNfcReadingEnabled(true);
                accessibilityBroadcaster.broadcast(
                        context.getString(R.string.nfc_reader_enabled)
                );
                break;
            case NOT_SUPPORTED:
                uiState.setNfcReadingEnabled(false);
                uiState.setErrorMessage(context.getString(R.string.nfc_not_supported));
                accessibilityBroadcaster.broadcast(
                        "Error. " + context.getString(R.string.nfc_not_supported)
                );
                break;
            case DISABLED:
                uiState.setNfcReadingEnabled(false);
                uiState.setErrorMessage(context.getString(R.string.nfc_disabled));
                accessibilityBroadcaster.broadcast(
                        "Error. " + context.getString(R.string.nfc_disabled)
                );
                break;
        }
    }

    public void simulateNfcRead(Runnable onStateChanged) {
        uiState.setLoading(true);
        uiState.setErrorMessage(null);
        accessibilityBroadcaster.broadcast(
                context.getString(R.string.reading_tag)
        );
        onStateChanged.run();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AppResult<NfcTagData> result = readNfcTagUseCase.execute(new Intent());

            uiState.setLoading(false);

            if (result instanceof AppResult.Success) {
                NfcTagData data = ((AppResult.Success<NfcTagData>) result).getData();
                uiState.setNfcTagData(data);
                uiState.setErrorMessage(null);
                accessibilityBroadcaster.broadcast(
                        buildAccessibilityMessage(data)
                );
            } else {
                AppResult.Failure<NfcTagData> failure = (AppResult.Failure<NfcTagData>) result;
                String error =
                        errorMapper.map(
                                failure.getError()
                        );
                uiState.setErrorMessage(error);
                accessibilityBroadcaster.broadcast(
                        context.getString(R.string.error_reading)
                                + error
                );
            }

            onStateChanged.run();
        }, 1800);
    }

    private String buildAccessibilityMessage(
            NfcTagData data
    ) {

        return
                R.string.reading_complete + ". "

                        + context.getString(R.string.tag_id)
                        + ": "
                        + data.getId() + ". "

                        + context.getString(R.string.technology)
                        + ": "
                        + data.getTechnology() + ". "

                        + context.getString(R.string.payload)
                        + ": "
                        + data.getPayload() + ". "

                        + context.getString(R.string.state)
                        + ": "
                        + data.getStatus() + ". "

                        + context.getString(R.string.ndef_compatible)
                        + ": "

                        + (
                        data.isNdefCompatible()
                                ? context.getString(R.string.yes)
                                : context.getString(R.string.no)
                );
    }

    @Override
    protected void onCleared() {

        super.onCleared();

        accessibilityBroadcaster.stopServer();
    }

}