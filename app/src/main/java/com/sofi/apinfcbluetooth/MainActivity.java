package com.sofi.apinfcbluetooth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.sofi.apinfcbluetooth.app.DeviceModule;
import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.presentation.device.DeviceUiState;
import com.sofi.apinfcbluetooth.presentation.device.DeviceViewModel;

public class MainActivity extends AppCompatActivity {

    private DeviceViewModel viewModel;

    private Button btnEnableReader;
    private Button btnReadMockTag;

    private LinearLayout loadingContainer;

    private TextView txtError;

    private TextInputEditText etTagId;
    private TextInputEditText etTechnology;
    private TextInputEditText etPayload;
    private TextInputEditText etStatus;
    private TextInputEditText etNdef;
    private TextInputEditText etSize;
    private TextInputEditText etReadAt;
    private TextInputEditText etNotes;

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

        loadingContainer = findViewById(R.id.loadingContainer);

        txtError = findViewById(R.id.txtError);

        etTagId = findViewById(R.id.etTagId);
        etTechnology = findViewById(R.id.etTechnology);
        etPayload = findViewById(R.id.etPayload);
        etStatus = findViewById(R.id.etStatus);
        etNdef = findViewById(R.id.etNdef);
        etSize = findViewById(R.id.etSize);
        etReadAt = findViewById(R.id.etReadAt);
        etNotes = findViewById(R.id.etNotes);
    }

    private void setupListeners() {

        btnEnableReader.setOnClickListener(v -> {

            viewModel.enableNfcReader();

            announceAccessibility("Lector NFC activado");

            render();
        });

        btnReadMockTag.setOnClickListener(v ->
                viewModel.simulateNfcRead(() -> {

                    announceAccessibility("Lectura NFC completada");

                    render();
                })
        );
    }

    private void render() {

        DeviceUiState state = viewModel.getUiState();

        boolean loading = state.isLoading();

        btnReadMockTag.setEnabled(
                state.isNfcReadingEnabled() && !loading
        );

        btnEnableReader.setEnabled(!loading);

        loadingContainer.setVisibility(
                loading ? View.VISIBLE : View.GONE
        );

        updateAccessibilityDescriptions(state);

        String error = state.getErrorMessage();

        if (error != null && !error.isEmpty()) {

            txtError.setText("Error: " + error);

            announceAccessibility("Error detectado. " + error);

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

        etNdef.setText(
                tagData.isNdefCompatible()
                        ? "Sí"
                        : "No"
        );

        etSize.setText(
                tagData.getEstimatedSizeBytes() + " bytes"
        );

        etReadAt.setText(tagData.getReadAt());

        etNotes.setText(tagData.getNotes());
    }

    private void clearForm() {

        etTagId.setText("");
        etTechnology.setText("");
        etPayload.setText("");
        etStatus.setText("");
        etNdef.setText("");
        etSize.setText("");
        etReadAt.setText("");
        etNotes.setText("");
    }

    private void announceAccessibility(String message) {

        View rootView = findViewById(android.R.id.content);

        rootView.announceForAccessibility(message);
    }

    private void updateAccessibilityDescriptions(DeviceUiState state) {

        if (state.isNfcReadingEnabled()) {

            btnReadMockTag.setContentDescription(
                    "Leer tag NFC"
            );

        } else {

            btnReadMockTag.setContentDescription(
                    "Debe activar el lector NFC antes de leer un tag"
            );
        }
    }
}