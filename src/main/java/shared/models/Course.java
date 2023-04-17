package shared.models;

import java.io.Serializable;

/**
 * Prendre un objet de la classe Course et le sérialise.
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
     * @return retourne le nom du cours
     */
    public String getName() {
        return name;
    }

    /**
     * Setter du nom
     * @param name définit ou met à jour le nom du cours
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter du code
     * @return retournr le code du cours
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter du code
     * @param code deéfinit ou met à jour le code du cours
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter de la session
     * @return retourne la session du cours
     */
    public String getSession() {
        return session;
    }

    /**
     * Setter de la session
     * @param session définit ou met à jour la session
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Transforme les informations du cours en String
     * indique au compilateur que la methode est réécrit
     * @return retourne un String avec toutes les informations du cours
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
