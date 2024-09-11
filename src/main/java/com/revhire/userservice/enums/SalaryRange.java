package com.revhire.userservice.enums;

public enum SalaryRange {
    ONE_TO_THREE_LPA("1-3 LPA"),
    THREE_TO_FIVE_LPA("3-5 LPA"),
    FIVE_TO_SEVEN_LPA("5-7 LPA"),
    SEVEN_TO_TEN_LPA("7-10 LPA"),
    TWENTY_FIVE_PLUS_LPA("25+ LPA");

    private final String displayName;

    SalaryRange(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}