package com.example.facebookinteration.constant.enums;

public enum PageStatus {
    ACTIVE(1),
    INACTIVE(0),
    SYNCING(2),
    FAILED(3);

    private final int value;

    PageStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
