package com.revhire.userservice.enums;

public enum ExperienceRequired {
    FRESHER("Fresher"),
    THREE_TO_FIVE_YEARS("3-5 years"),
    FIVE_TO_SEVEN_YEARS("5-7 years"),
    TEN_PLUS_YEARS("10+ years");

    private final String displayName;

    ExperienceRequired(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}