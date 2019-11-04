package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

public enum MessageCategory {
    
    POST_CONFIRM_MESSAGE,
    RATE_MESSAGE,
    SYSTEM_MESSAGE,
    USER_MESSAGE,
    VACANT_CONFIRM_MESSAGE;

    private String label;

    static {

        POST_CONFIRM_MESSAGE.label = "postConfirmMessage";
        RATE_MESSAGE.label = "rateMessage";
        SYSTEM_MESSAGE.label = "systemMessage";
        USER_MESSAGE.label = "userMessage";
        VACANT_CONFIRM_MESSAGE.label = "vacantConfirmMessage";
    }

    public String getLabel() {
        return label;
    }
}
