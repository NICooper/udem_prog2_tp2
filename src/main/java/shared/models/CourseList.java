package shared.models;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Une représentation d'accès à une liste de Course avec un filtre sur la session.
 * Les sous-classes doivent définir comment accéder à liste de courses.
 */
public abstract class CourseList {
    /**
     * La List de Course récupérée. Peut être égale à null si la loadFilteredCourseList() n'a pas encore été appelé.
     */
    protected List<Course> courses;

    private String session;

    /**
     * Charge la liste de cours et les filtre selon la session choisie.
     * @return Un ModelResult avec la liste de course et des informations sur le succès de l'opération de chargement.
     */
    public abstract ModelResult<List<Course>> loadFilteredCourseList();

    /**
     * Ajoute un String qui servira de filtre de session sur la liste de Course.
     * Le String de session doit être valide. Le isValid du DataValidation retourné sera false si la session n'est pas
     * valide.
     * @param session Un String qui représente le nom d'une session.
     * @return DataValidation indiquant si le String de session était valide et un message de validation si non.
     */
    public DataValidation setSessionFilter(String session) {
        if (Pattern.compile(Sessions.getRegexString()).matcher(session).matches()) {
            this.session = session;
            return new DataValidation(true);
        }
        else {
            return new DataValidation(false, "Le nom de la session doit être Automne, Hiver ou Ete.");
        }
    }

    /**
     * Retourne le String de la session qui sera utilisé pour filtrer les cours.
     * @return le String du nom de la session.
     */
    public String getSessionFilter() {
        return this.session;
    }

    /**
     * Cherche les Course chargés en mémoire pour en trouver un qui a le même code que le paramètre code.
     * @param code le code de cours.
     * @return Un Course si code correspond à un Course, null sinon.
     */
    public Course getCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }
}
