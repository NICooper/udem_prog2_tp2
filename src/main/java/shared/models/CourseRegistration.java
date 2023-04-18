package shared.models;

/**
 * Une représentation de l'enregistrement d'une inscription à un cours
 * Les sous-classes doivent définir comment sauvegarder les données d'inscription.
 */
public abstract class CourseRegistration {
    protected ValidatedRegistrationForm validatedForm;

    /**
     * Construit un nouveau CourseRegistration.
     */
    public CourseRegistration() {
        this(new ValidatedRegistrationForm());
    }

    /**
     * Construit un nouveau CourseRegistration avec le ValidatedRegistrationForm donné.
     * @param form Un objet RegistrationForm.
     */
    public CourseRegistration(ValidatedRegistrationForm form) {
        this.validatedForm = form;
    }

    /**
     * Sauvegarde le formulaire d'inscription qui a été donné à CourseRegistration.
     * @return Un ModelResult qui contient le formulaire sauvegardé et des informations sur le succès de l'opération de
     * sauvegarde.
     */
    public abstract ModelResult<RegistrationForm> register();

    /**
     * Retourne le formulaire validé stocké par CourseRegistration.
     * @return Le ValidatedRegistrationForm qui appartient à cet object.
     */
    public ValidatedRegistrationForm getValidatedForm() {
        return this.validatedForm;
    }
}
