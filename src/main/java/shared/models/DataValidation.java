package shared.models;

public class DataValidation {
    public boolean isValid;
    public String validationMessage;

    public DataValidation(boolean isValid) {
        this(isValid, "");
    }

    public DataValidation(boolean isValid, String validationMessage) {
        this.isValid = isValid;
        if (validationMessage == null) {
            validationMessage = "";
        }
        this.validationMessage = validationMessage;
    }

    public DataValidation addValidation(DataValidation otherValidation) {
        if (!otherValidation.isValid) {
            this.isValid = false;

            if (!this.validationMessage.isEmpty()) {
                this.validationMessage += "\n";
            }
            this.validationMessage += otherValidation.validationMessage;
        }

        return this;
    }
}
