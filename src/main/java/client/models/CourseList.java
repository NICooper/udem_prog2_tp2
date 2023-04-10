package client.models;

import models.Course;

import java.util.List;
import java.util.regex.Pattern;

public abstract class CourseList {
    private List<Course> courses;
    private String session;

    public abstract ModelResult<List<Course>> loadFilteredCourseList();

    public CourseList() {

    }

    public DataValidation setSessionFilter(String session) {
        if (Pattern.compile(Sessions.getRegexString()).matcher(session).matches()) {
            this.session = session;
            return new DataValidation(true);
        }
        else {
            return new DataValidation(false, "Le nom de la session doit être Automne, Hiver ou Ete.");
        }
    }

    public String getSessionFilter() {
        return session;
    }
}
