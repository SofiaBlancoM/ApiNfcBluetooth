package com.sofi.apinfcbluetooth;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.sofi.apinfcbluetooth.app.DeviceModule;
import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.presentation.device.DeviceUiState;
import com.sofi.apinfcbluetooth.presentation.device.DeviceViewModel;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    private DeviceViewModel viewModel;

    private Button btnEnableReader;
    private Button btnReadMockTag;
    private Button btnAccessibilityMode;

    private LinearLayout loadingContainer;

    private TextView txtError;
    private TextView txtSubtitle;
    private TextView txtLoading;

    private ProgressBar progressBar;

    private TextInputEditText etTagId;
    private TextInputEditText etTechnology;
    private TextInputEditText etPayload;
    private TextInputEditText etStatus;
    private TextInputEditText etNdef;

    private boolean accessibilityMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = DeviceModule.provideDeviceViewModel(this);

        bindViews();
        setupListeners();
        render();
    }

    private void bindViews() {

        btnEnableReader = findViewById(R.id.btnEnableReader);
        btnReadMockTag = findViewById(R.id.btnReadMockTag);
        btnAccessibilityMode = findViewById(R.id.btnAccessibilityMode);

        loadingContainer = findViewById(R.id.loadingContainer);

        txtError = findViewById(R.id.txtError);
        txtSubtitle = findViewById(R.id.txtSubtitle);
        txtLoading = findViewById(R.id.txtLoading);

        progressBar = findViewById(R.id.progressBar);

        etTagId = findViewById(R.id.etTagId);
        etTechnology = findViewById(R.id.etTechnology);
        etPayload = findViewById(R.id.etPayload);
        etStatus = findViewById(R.id.etStatus);
        etNdef = findViewById(R.id.etNdef);
    }

    private void setupListeners() {

        btnAccessibilityMode.setOnClickListener(v -> {
            accessibilityMode = !accessibilityMode;
            applyAccessibilityMode();

            announce(getString(R.string.accessibility_mode) +
                    (accessibilityMode ? getString(R.string.enabled) : getString(R.string.disabled)));
        });

        btnEnableReader.setOnClickListener(v -> {

            viewModel.enableNfcReader();

            announce(getString(R.string.nfc_reader_enabled));
            vibrate();

            btnReadMockTag.requestFocus();

            render();
        });

        btnReadMockTag.setOnClickListener(v -> {

            if (!viewModel.getUiState().isNfcReadingEnabled()) {

                announce(getString(R.string.cannot_read_tag));
                return;
            }

            viewModel.simulateNfcRead(() -> {

                announce(getString(R.string.reading_complete));
                vibrate();

                render();
            });
        });
    }

    private void render() {

        DeviceUiState state = viewModel.getUiState();

        boolean loading = state.isLoading();

        btnReadMockTag.setEnabled(state.isNfcReadingEnabled() && !loading);
        btnEnableReader.setEnabled(!loading);

        loadingContainer.setVisibility(loading ? View.VISIBLE : View.GONE);

        btnReadMockTag.setContentDescription(
                state.isNfcReadingEnabled()
                        ? getString(R.string.nfc_reader_button)
                        : getString(R.string.button_disabled)
        );

        if (!state.isNfcReadingEnabled() && accessibilityMode) {
            btnReadMockTag.setText(R.string.enable_nfc_first);
        } else {
            btnReadMockTag.setText(R.string.read_tag_nfc);
        }

        String error = state.getErrorMessage();

        if (error != null && !error.isEmpty()) {

            txtError.setText(error);
            txtError.setContentDescription(getString(R.string.error) + error);

            if (accessibilityMode) {
                announce(getString(R.string.error_detected) + error);
                vibrate();
            }

        } else {
            txtError.setText("");
        }

        NfcTagData tagData = state.getNfcTagData();

        if (tagData == null) {
            clearForm();
            return;
        }

        etTagId.setText(tagData.getId());
        etTechnology.setText(tagData.getTechnology());
        etPayload.setText(tagData.getPayload());
        etStatus.setText(tagData.getStatus());
        etNdef.setText(tagData.isNdefCompatible() ? getString(R.string.yes) : getString(R.string.no));

        etTagId.setContentDescription(getString(R.string.nfc_id));
        etTechnology.setContentDescription(getString(R.string.nfc_technology));
        etPayload.setContentDescription(getString(R.string.nfc_content));
        etStatus.setContentDescription(getString(R.string.state));
        etNdef.setContentDescription(getString(R.string.ndef_compatibility));
    }

    private void clearForm() {

        etTagId.setText("");
        etTechnology.setText("");
        etPayload.setText("");
        etStatus.setText("");
        etNdef.setText("");
    }

    private void applyAccessibilityMode() {

        float scale = getResources().getDisplayMetrics().density;

        View root = findViewById(android.R.id.content);

        if (accessibilityMode) {

            btnAccessibilityMode.setText(R.string.disable_accessibility);

            root.setBackgroundColor(Color.WHITE);

            txtSubtitle.setTextColor(Color.BLACK);
            txtLoading.setTextColor(Color.BLACK);
            txtError.setTextColor(Color.RED);

            txtSubtitle.setTextSize(18);
            txtLoading.setTextSize(18);
            txtError.setTextSize(18);

            int minHeight = (int) (64 * scale);

            btnEnableReader.setMinHeight(minHeight);
            btnReadMockTag.setMinHeight(minHeight);
            btnAccessibilityMode.setMinHeight(minHeight);

            btnEnableReader.setTextSize(18);
            btnReadMockTag.setTextSize(18);
            btnAccessibilityMode.setTextSize(18);

            etTagId.setTextSize(18);
            etTechnology.setTextSize(18);
            etPayload.setTextSize(18);
            etStatus.setTextSize(18);
            etNdef.setTextSize(18);

            txtLoading.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
            txtError.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);

        } else {

            btnAccessibilityMode.setText(R.string.enable_accessibility);

            root.setBackgroundColor(Color.parseColor("#F5F7FA"));

            txtSubtitle.setTextColor(Color.parseColor("#6B7280"));
            txtLoading.setTextColor(Color.parseColor("#374151"));
            txtError.setTextColor(Color.parseColor("#B91C1C"));

            txtSubtitle.setTextSize(14);
            txtLoading.setTextSize(14);
            txtError.setTextSize(14);

            btnEnableReader.setMinHeight(0);
            btnReadMockTag.setMinHeight(0);
            btnAccessibilityMode.setMinHeight(0);

            btnEnableReader.setTextSize(14);
            btnReadMockTag.setTextSize(14);
            btnAccessibilityMode.setTextSize(14);

            etTagId.setTextSize(14);
            etTechnology.setTextSize(14);
            etPayload.setTextSize(14);
            etStatus.setTextSize(14);
            etNdef.setTextSize(14);

            txtLoading.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_NONE);
            txtError.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_NONE);
        }
    }

    private void announce(String message) {
        View view = findViewById(android.R.id.content);
        view.announceForAccessibility(message);
    }

    private void vibrate() {

        Vibrator vibrator =
                (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null && vibrator.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                        VibrationEffect.createOneShot(
                                80,
                                VibrationEffect.DEFAULT_AMPLITUDE
                        )
                );
            }
        }
    }

}