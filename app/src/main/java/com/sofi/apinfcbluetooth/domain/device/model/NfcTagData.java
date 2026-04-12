package com.sofi.apinfcbluetooth.domain.device.model;

public class NfcTagData {

    private final String id;
    private final String technology;
    private final String payload;
    private final String readAt;
    private final boolean ndefCompatible;
    private final int estimatedSizeBytes;
    private final String status;
    private final String notes;

    public NfcTagData(
            String id,
            String technology,
            String payload,
            String readAt,
            boolean ndefCompatible,
            int estimatedSizeBytes,
            String status,
            String notes
    ) {
        this.id = id;
        this.technology = technology;
        this.payload = payload;
        this.readAt = readAt;
        this.ndefCompatible = ndefCompatible;
        this.estimatedSizeBytes = estimatedSizeBytes;
        this.status = status;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getTechnology() {
        return technology;
    }

    public String getPayload() {
        return payload;
    }

    public String getReadAt() {
        return readAt;
    }

    public boolean isNdefCompatible() {
        return ndefCompatible;
    }

    public int getEstimatedSizeBytes() {
        return estimatedSizeBytes;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }
}