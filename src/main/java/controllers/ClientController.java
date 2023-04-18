package controllers;

import shared.models.CourseList;
import shared.models.CourseRegistration;
import shared.models.DataValidation;
import shared.models.ModelResult;
import shared.models.Course;
import shared.models.RegistrationForm;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Un controlleur pour les clients du système d'inscription. Permet de facilement changer de vue ou de modèle sans avoir
 * à changer autres choses.
 */
public class ClientController {
    private CourseList courseList;
    private CourseRegistration courseReg;
    private Consumer<Boolean> formStatusCallback;

    /**
     * Crée un nouveau ClientController avec les modèles nécessaires donnés en paramètre.
     * @param courseList Un objet du modèle qui extends CourseList. Sert à charger la List de Course.
     * @param courseReg Un objet du modèle qui extends CourseRegistration. Sert à enregistrer les données d'inscription.
     */
    public ClientController(CourseList courseList, CourseRegistration courseReg) {
        this.courseList = courseList;
        this.courseReg = courseReg;
    }

    /**
     * Passe session au modèle approprié et met à jour la vue en appelant la callback donnée.
     * @param session La session pour laquelle la List de Course sera filtrée.
     * @param callback Fonction, fournie par une vue, qui reçoit la session tel que, possiblement, modifié par le modèle et
     *                 un DataValidation qui contient des informations sur la validité de la session fournie selon le modèle.
     */
    public void setSession(String session, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseList.setSessionFilter(session);
        callback.accept(this.courseList.getSessionFilter(), validation);
    }

    /**
     * Demande au modèle de charger la List de Course. Appelle la callback donnée avec le résultat.
     * @param callback Fonction, fournie par une vue, qui reçoit un ModelResult<Course>. Ceci contient le résultat
     *                 de l'opération demandée au modèle.
     */
    public void loadCourseList(Consumer<ModelResult<List<Course>>> callback) {
        callback.accept(this.courseList.loadFilteredCourseList());
    }

    /**
     * Passe firstName au modèle approprié et met à jour la vue en appelant la callback donnée.
     * @param firstName Le prénom de l'utilisateur.
     * @param callback Fonction, fournie par une vue, qui reçoit le prénom tel que, possiblement, modifié par le modèle et
     *                 un DataValidation qui contient des informations sur la validité du prénom fourni selon le modèle.
     */
    public void setFirstName(String firstName, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setFirstName(firstName);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getFirstName(), validation);
    }

    /**
     * Passe lastName au modèle approprié et met à jour la vue en appelant la callback donnée.
     * @param lastName Le nom de l'utilisateur.
     * @param callback Fonction, fournie par une vue, qui reçoit le nom tel que, possiblement, modifié par le modèle et
     *                 un DataValidation qui contient des informations sur la validité du nom fourni selon le modèle.
     */
    public void setLastName(String lastName, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setLastName(lastName);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getLastName(), validation);
    }

    /**
     * Passe email au modèle approprié et met à jour la vue en appelant la callback donnée.
     * @param email L'adresse courriel de l'utilisateur.
     * @param callback Fonction, fournie par une vue, qui reçoit l'adresse courriel tel que, possiblement, modifiée par
     *                 le modèle et un DataValidation qui contient des informations sur la validité de l'adresse courriel
     *                 fournie selon le modèle.
     */
    public void setEmail(String email, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setEmail(email);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getEmail(), validation);
    }

    /**
     * Passe matricule au modèle approprié et met à jour la vue en appelant la callback donnée.
     * @param matricule Le matricule de l'utilisateur.
     * @param callback Fonction, fournie par une vue, qui reçoit le matricule tel que, possiblement, modifié par le modèle et
     *                 un DataValidation qui contient des informations sur la validité du matricule fourni selon le modèle.
     */
    public void setMatricule(String matricule, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setMatricule(matricule);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getMatricule(), validation);
    }

    /**
     * Trouve le Course qui correspond au code puis passe ce course au modèle approprié et met à jour la vue en appelant
     * la callback donnée.
     * @param code Le code d'un Course.
     * @param callback Fonction, fournie par une vue, qui reçoit le Course tel que, possiblement, modifié par le modèle et
     *                 un DataValidation qui contient des informations sur la validité du Course fourni selon le modèle.
     */
    public void setCourseByCode(String code, BiConsumer<Course, DataValidation> callback) {
        Course course = courseList.getCourseByCode(code);
        if (course != null) {
            DataValidation validation = this.courseReg.getValidatedForm().setCourse(course);
            updateFormStatus();
            callback.accept(this.courseReg.getValidatedForm().getCourse(), validation);
        }
        else {
            callback.accept(null, new DataValidation(false, "Le code fourni n'est pas associé à un cours dans cette session."));
        }
    }

    /**
     * Passe course au modèle approprié et met à jour la vue en appelant la callback donnée.
     * @param course Le Course auquel l'utilisateur veut s'inscrire.
     * @param callback Fonction, fournie par une vue, qui reçoit le Course tel que, possiblement, modifié par le modèle et
     *                 un DataValidation qui contient des informations sur la validité du Course fourni selon le modèle.
     */
    public void setCourse(Course course, BiConsumer<Course, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setCourse(course);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getCourse(), validation);
    }

    /**
     * Appelle la callback formStatusCallback enregistrée et y passe un booléen selon la validité des données d'inscription.
     */
    public void updateFormStatus() {
        if (this.formStatusCallback != null) {
            this.formStatusCallback.accept(this.courseReg.getValidatedForm().isFullyValid());
        }
    }

    /**
     * Enregistre une callback qui sera appelée chaque fois qu'une donnée d'inscription a été validée.
     * @param callback Fonction, fournie par une vue, qui reçoit un Boolean comme paramètre. Ce Boolean représente la
     *                 validité des données d'inscription. Elle sera true si toutes les données sont valides, false sinon.
     */
    public void registerFormStatusCallback(Consumer<Boolean> callback) {
        this.formStatusCallback = callback;
    }

//    public void isFormComplete(Consumer<Boolean> callback) {
//        callback.accept(this.courseReg.getValidatedForm().isFullyValid());
//    }

    /**
     * Demande au modèle d'enregistrer les données d'inscription. Appelle la callback donnée avec le résultat.
     * @param callback Fonction, fournie par une vue, qui reçoit un ModelResult<RegistrationForm>. Ceci contient le résultat
     *                 de l'opération demandée au modèle.
     */
    public void registerToCourse(Consumer<ModelResult<RegistrationForm>> callback) {
        callback.accept(this.courseReg.register());
    }
}
