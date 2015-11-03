package ru.mail.track.message;

/**
 * Created by aliakseisemchankau on 2.11.15.
 */
public class Result {

    private boolean status = true;
    private String errorMsg = "";

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
        return "status: " + status + "\n" +
                "errorMsg: " + errorMsg + "\n";
    }

}
