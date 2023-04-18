package shared.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Sous-classe de CourseList. Représente le cas où le fichier des inscriptions est stocké localement et qu'il faut donc
 * faire un append sur ce fichier pour ajouter les données d'inscription d'un utilisateur.
 */
public class LocalCourseRegistration extends CourseRegistration {
    private final String fileName;

    /**
     * Crée un nouveau LocalCourseRegistration avec comme paramètre le fichier auquel cet objet va ajouter les données
     * d'inscription.
     * @param fileName Le chemin vers le fichier qui contient les inscriptions.
     */
    public LocalCourseRegistration(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Appende les données du RegistrationForm à la fin du fichier spécifié.
     * Les données seront écritent sous le format tsv:
     * session  code    matricule   prenom  nom email
     * @return Un ModelResult qui contient le RegistrationForm, l'état de la tentative de écriture de fichier, et
     * éventuellement un message d'erreur.
     */
    @Override
    public ModelResult<RegistrationForm> register() {
        ModelResult<RegistrationForm> result = new ModelResult<>();

        if (this.validatedForm.isFullyValid()) {
            String registrationText = this.validatedForm.form.getCourse().getSession() + "\t"
                    + this.validatedForm.form.getCourse().getCode() + "\t"
                    + this.validatedForm.form.getMatricule() + "\t"
                    + this.validatedForm.form.getPrenom() + "\t"
                    + this.validatedForm.form.getNom() + "\t"
                    + this.validatedForm.form.getEmail() + "\n";
            try (FileWriter fw = new FileWriter(this.fileName, true)) {
                BufferedWriter writer = new BufferedWriter(fw);
                writer.append(registrationText);
                writer.close();
                result.data = this.validatedForm.form;
                result.success = true;
                result.message = "Félicitations! Inscription réussie de "
                        + this.validatedForm.form.getPrenom() + " au cours "
                        + this.validatedForm.form.getCourse().getCode() + ".";
            } catch (IOException e) {
                result.success = false;
                result.message = "Erreur à la sauvegarde des données d'inscription.";
            }
        }
        else {
            result.success = false;
            result.message = "Les données d'inscription fournies ne sont pas valides.";
        }

        return result;
    }
}
