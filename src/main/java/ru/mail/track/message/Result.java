package ru.mail.track.message;

/**
 * Created by aliakseisemchankau on 2.11.15.
 */
public class Result {

    private boolean status = true;
    private String errorMsg = "";
    private String textMSG = "";

    public void setTextMSG(String textMSG) {
        this.textMSG = textMSG;
    }

    public String getTextMSG() {

        return textMSG;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "{ status: " + status + ", " +
                "errorMsg: " + errorMsg + ", " +
                "textMsg: " + textMSG + " }";
    }

    public Result(final boolean status, final String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
    }

}
