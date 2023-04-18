package shared.models;

import java.io.Serializable;

/**
 * Prendre un objet de la classe RegistrationForm et le sérialise.
 */
public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Constructeur de la classe RegistrationForme.
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
     * @return retourne le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Setter du prénom
     * @param prenom définit ou met à jour le prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Getter du nom
     * @return retourne le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter du nom
     * @param nom définit ou met à jour le nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter du courriel
     * @return retourne le courriel du client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter du courriel
     * @param email définit ou met à jour le courriel
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter du matricule
     * @return retourne le matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Setter du matricule
     * @param matricule définit ou met à jour le matricule
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Getter de l'objet Course
     * @return retourne l'objet Course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Setter de l'objet Course
     * @param course définit ou met à jour l'objet Course
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Transforme les informations pour la registration en String
     * indique au compilateur que la methode est réécrit
     * @return retourne un String avec toutes les informations nécessaires de la registration
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
