package shared.models;

import java.io.Serializable;

/**
 * Classe qui represente un cours.
 */
public class Course implements Serializable {

    private String name;
    private String code;
    private String session;

    /**
     * Constructeur de la classe Course
     * @param name nom du cours
     * @param code code du cours
     * @param session session dans laquelle que le cours est donnée
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Getter du nom
     * @return le nom du cours
     */
    public String getName() {
        return name;
    }

    /**
     * Setter du nom
     * @param name nouvelle valeur du nom du cours
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter du code
     * @return le code du cours
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter du code
     * @param code nouvelle valeur du code du cours
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter de la session
     * @return la session du cours
     */
    public String getSession() {
        return session;
    }

    /**
     * Setter de la session
     * @param session nouvelle valeur de session
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Transforme les informations du cours en String
     * @return un String avec toutes les informations du cours
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
