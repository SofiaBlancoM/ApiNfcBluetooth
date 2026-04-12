package com.sofi.apinfcbluetooth.infrastructure.device.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;

import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.domain.device.port.NfcTagReader;
import com.sofi.apinfcbluetooth.domain.device.result.AppError;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AndroidNfcTagReader implements NfcTagReader {

    private static final String READ_STATUS_SUCCESS = "READ_SUCCESS";
    private static final String DEFAULT_NOTES = "Lectura realizada desde dispositivo físico";

    @Override
    public AppResult<NfcTagData> read(Intent intent) {
        try {
            if (intent == null) {
                return new AppResult.Failure<>(AppError.NFC_INVALID_INTENT);
            }

            String action = intent.getAction();
            boolean isNfcAction =
                    NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
                            NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                            NfcAdapter.ACTION_TECH_DISCOVERED.equals(action);

            if (!isNfcAction) {
                return new AppResult.Failure<>(AppError.NFC_INVALID_INTENT);
            }

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null) {
                return new AppResult.Failure<>(AppError.NFC_INVALID_INTENT);
            }

            String tagId = toHex(tag.getId());
            String payload = extractPayload(intent);

            if (payload == null || payload.trim().isEmpty()) {
                return new AppResult.Failure<>(AppError.NFC_EMPTY_PAYLOAD);
            }

            String technology = extractTechnology(tag);
            boolean ndefCompatible = isNdefCompatible(intent);
            int estimatedSizeBytes = estimatePayloadSize(payload);
            String readAt = buildReadTimestamp();

            NfcTagData tagData = new NfcTagData(
                    tagId,
                    technology,
                    payload.trim(),
                    readAt,
                    ndefCompatible,
                    estimatedSizeBytes,
                    READ_STATUS_SUCCESS,
                    DEFAULT_NOTES
            );

            return new AppResult.Success<>(tagData);

        } catch (Exception ex) {
            return new AppResult.Failure<>(AppError.UNKNOWN);
        }
    }

    private String extractPayload(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages == null || rawMessages.length == 0) {
            return "";
        }

        List<String> values = new ArrayList<>();

        for (Parcelable raw : rawMessages) {
            if (!(raw instanceof NdefMessage)) {
                continue;
            }

            NdefMessage message = (NdefMessage) raw;
            for (NdefRecord record : message.getRecords()) {
                String value = parseRecord(record);
                if (value != null && !value.isEmpty()) {
                    values.add(value);
                }
            }
        }

        return String.join("\n", values);
    }

    private String parseRecord(NdefRecord record) {
        try {
            if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                    Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
                return parseTextRecord(record.getPayload());
            }

            byte[] payload = record.getPayload();
            return new String(payload, StandardCharsets.UTF_8).trim();
        } catch (Exception ex) {
            return null;
        }
    }

    private String parseTextRecord(byte[] payload) {
        if (payload == null || payload.length == 0) {
            return "";
        }

        int status = payload[0] & 0xFF;
        int languageCodeLength = status & 0x3F;
        boolean utf16 = (status & 0x80) != 0;

        Charset encoding = utf16 ? StandardCharsets.UTF_16 : StandardCharsets.UTF_8;
        int textStart = 1 + languageCodeLength;

        if (textStart >= payload.length) {
            return "";
        }

        return new String(payload, textStart, payload.length - textStart, encoding).trim();
    }

    private String extractTechnology(Tag tag) {
        String[] techList = tag.getTechList();

        if (techList == null || techList.length == 0) {
            return "UNKNOWN";
        }

        List<String> simplifiedTechs = new ArrayList<>();

        for (String tech : techList) {
            if (tech == null || tech.trim().isEmpty()) {
                continue;
            }

            int lastDotIndex = tech.lastIndexOf('.');
            String simplified = lastDotIndex >= 0
                    ? tech.substring(lastDotIndex + 1)
                    : tech;

            simplifiedTechs.add(simplified);
        }

        return String.join(" / ", simplifiedTechs);
    }

    private boolean isNdefCompatible(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        return rawMessages != null && rawMessages.length > 0;
    }

    private int estimatePayloadSize(String payload) {
        return payload == null ? 0 : payload.getBytes(StandardCharsets.UTF_8).length;
    }

    private String buildReadTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    private String toHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (byte value : bytes) {
            builder.append(String.format("%02X", value));
        }
        return builder.toString();
    }
}