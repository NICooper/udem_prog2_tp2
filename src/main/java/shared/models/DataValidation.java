package shared.models;

/**
 * Représente une réponse de validation d'une donnée. Les champs sont publics car c'est destiné à être consommé immédiatement
 * par le code qui reçoit cette validation et ne fait pas parti de l'état persistant de l'application.
 */
public class DataValidation {
    /**
     * True si la donnée validée est valide, false sinon.
     */
    public boolean isValid;
    /**
     * Un message qui dit pourquoi la donnée validée n'est pas valide.
     */
    public String validationMessage;

    /**
     * Crée un nouveau DataValidation où le validationMessage est le String vide.
     * @param isValid Boolean qui représente si une donnée est valide.
     */
    public DataValidation(boolean isValid) {
        this(isValid, "");
    }

    /**
     * Crée un nouveau DataValidation.
     * @param isValid Boolean qui représente si une donnée est valide.
     * @param validationMessage Un message de validation qui dit pourquoi la donnée validée n'est pas valide.
     */
    public DataValidation(boolean isValid, String validationMessage) {
        this.isValid = isValid;
        if (validationMessage == null) {
            validationMessage = "";
        }
        this.validationMessage = validationMessage;
    }

    /**
     * Sert à "concatener" plusieurs DataValidation en un DataValidation.
     * La nouvelle valeur de isValid est équivalente à une AND entre cet isValid et le isValid de otherValidation.
     * Le validationMessage de otherValidation est ajouté à ce validationMessage dans une nouvelle ligne si isValid de
     * otherValidation est false.
     * @param otherValidation Le DataValidation à ajouter à ce DataValidation.
     * @return Ce DataValidation.
     */
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
