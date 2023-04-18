package shared.models;

import java.io.Serializable;

/**
 * Classe qui représente les données d'un formulaire d'inscription.
 */
public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Constructeur de la classe RegistrationForm.
     * @param prenom Prénom du client
     * @param nom Nom du client
     * @param email Courriel du client
     * @param matricule Matricule du client
     * @param course Cours sélectionné par le client pour la registration
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Getter du prénom
     * @return le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Setter du prénom
     * @param prenom nouveau prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Getter du nom
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter du nom
     * @param nom nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter du courriel
     * @return le courriel du client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter du courriel
     * @param email nouveau courriel
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter du matricule
     * @return le matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Setter du matricule
     * @param matricule nouveau matricule
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Getter de l'objet Course
     * @return l'objet Course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Setter de l'objet Course
     * @param course nouvelle objet Course
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Transforme les informations de la RegistrationForm en String
     * @return un String avec toutes les informations de la registration form
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
