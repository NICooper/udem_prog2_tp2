package shared.models;

import java.util.List;
import java.util.regex.Pattern;

public abstract class CourseList {
    protected List<Course> courses;
    private String session;

    public abstract ModelResult<List<Course>> loadFilteredCourseList();

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

    public Course getCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }
}
