package client.models;

import models.Course;
import models.RegistrationForm;

import java.util.regex.Pattern;

public abstract class CourseRegistration {
    protected RegistrationForm form;
    private final boolean[] validationTracker = {false, false, false, false, false};

    public CourseRegistration() {
        this(new RegistrationForm("", "", "", "", new Course("", "", "")));
    }

    public CourseRegistration(RegistrationForm form) {
        this.form = form;
    }

    public abstract ModelResult<RegistrationForm> register();

    public boolean isFullyValid() {
        for (boolean validation : validationTracker) {
            if (!validation) {
                return false;
            }
        }
        return true;
    }

    public DataValidation setRegistrationForm(RegistrationForm form) {
        return new DataValidation(true)
                .addValidation(this.setFirstName(form.getPrenom()))
                .addValidation(this.setLastName(form.getNom()))
                .addValidation(this.setEmail(form.getEmail()))
                .addValidation(this.setMatricule(form.getMatricule()))
                .addValidation(this.setCourse(form.getCourse()));
//                .addValidation(this.setCourseCode(form.getCourse().getCode()))
//                .addValidation(this.setCourseName(form.getCourse().getName()))
//                .addValidation(this.setCourseSession(form.getCourse().getSession()));
    }

    public DataValidation setFirstName(String firstName) {
        firstName = firstName.strip();
        if (Pattern.compile("(?U).*\\w.*").matcher(firstName).matches()) {
            this.form.setPrenom(firstName);
            validationTracker[0] = true;
            return new DataValidation(true);
        }
        else {
            validationTracker[0] = false;
            return new DataValidation(false, "Un prénom valide doit contenir au moins une lettre.");
        }
    }

    public DataValidation setLastName(String lastName) {
        lastName = lastName.strip();
        if (Pattern.compile("(?U).*\\w.*").matcher(lastName).matches()) {
            this.form.setNom(lastName);
            validationTracker[1] = true;
            return new DataValidation(true);
        } else {
            validationTracker[1] = false;
            return new DataValidation(false, "Un nom valide doit contenir au moins une lettre.");
        }
    }

    public DataValidation setEmail(String email) {
        email = email.strip();
        if (Pattern.compile("(?U)[^@]+@[^@]+\\.[^@]+").matcher(email).matches()) {
            this.form.setEmail(email);
            validationTracker[2] = true;
            return new DataValidation(true);
        } else {
            validationTracker[2] = false;
            return new DataValidation(false, "Une addresse courriel valide doit être de format abc@xyz.com.");
        }
    }

    public DataValidation setMatricule(String matricule) {
        matricule = matricule.strip();
        if (Pattern.compile("\\d{6,}").matcher(matricule).matches()) {
            this.form.setMatricule(matricule);
            validationTracker[3] = true;
            return new DataValidation(true);
        } else {
            validationTracker[3] = false;
            return new DataValidation(false, "Un matricule valide doit contenir 6 ou plus chiffres seulement.");
        }
    }

    public DataValidation setCourse(Course course) {
        if (course != null && course.getCode() != null && course.getName() != null && course.getSession() != null) {
            this.form.setCourse(course);
            validationTracker[4] = true;
            return new DataValidation(true);
        } else {
            validationTracker[4] = false;
            return new DataValidation(false, "Cours non-valide.");
        }
    }

//    public DataValidation setCourseCode(String code) {
//        code = code.strip();
//        if (Pattern.compile(".+").matcher(code).matches()) {
//            this.form.getCourse().setCode(code);
//            validationTracker[4] = true;
//            return new DataValidation(true);
//        } else {
//            validationTracker[4] = false;
//            return new DataValidation(false, "Un matricule valide doit contenir au moins un caractère.");
//        }
//    }
//
//    public DataValidation setCourseName(String name) {
//        name = name.strip();
//        if (Pattern.compile(".+").matcher(name).matches()) {
//            this.form.getCourse().setName(name);
//            validationTracker[5] = true;
//            return new DataValidation(true);
//        } else {
//            validationTracker[5] = false;
//            return new DataValidation(false, "Un matricule valide doit contenir 6 ou plus chiffres seulement.");
//        }
//    }
//
//    public DataValidation setCourseSession(String session) {
//        if (Pattern.compile(Sessions.getRegexString()).matcher(session).matches()) {
//            this.form.getCourse().setSession(session);
//            validationTracker[6] = true;
//            return new DataValidation(true);
//        }
//        else {
//            validationTracker[6] = false;
//            return new DataValidation(false, "Le nom de la session doit être Automne, Hiver ou Ete.");
//        }
//    }

    public String getFirstName() {
        return this.form.getPrenom();
    }

    public String getLastName() {
        return this.form.getNom();
    }

    public String getEmail() {
        return this.form.getEmail();
    }

    public String getMatricule() {
        return this.form.getMatricule();
    }

    public Course getCourse() {
        return this.form.getCourse();
    }

    public String getCourseCode() {
        return this.form.getCourse().getCode();
    }

    public String getCourseName() {
        return this.form.getCourse().getName();
    }

    public String getCourseSession() {
        return this.form.getCourse().getSession();
    }
}
