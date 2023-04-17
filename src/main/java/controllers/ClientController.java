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

public class ClientController {
    CourseList courseList;
    CourseRegistration courseReg;
    Consumer<Boolean> formStatusCallback;

    public ClientController(CourseList courseList, CourseRegistration courseReg) {
        this.courseList = courseList;
        this.courseReg = courseReg;
    }

    public void setSession(String session, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseList.setSessionFilter(session);
        callback.accept(this.courseList.getSessionFilter(), validation);
    }

    public void loadCourseList(Consumer<ModelResult<List<Course>>> callback) {
        callback.accept(this.courseList.loadFilteredCourseList());
    }

    public void setFirstName(String firstName, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setFirstName(firstName);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getFirstName(), validation);
    }

    public void setLastName(String lastName, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setLastName(lastName);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getLastName(), validation);
    }

    public void setEmail(String email, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setEmail(email);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getEmail(), validation);
    }

    public void setMatricule(String matricule, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setMatricule(matricule);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getMatricule(), validation);
    }

    public void setCourseByCode(String code, BiConsumer<Course, DataValidation> callback) {
        Course course = courseList.getCourseByCode(code);
        if (course != null) {
            DataValidation validation = this.courseReg.getValidatedForm().setCourse(course);
            updateFormStatus();
            callback.accept(this.courseReg.getValidatedForm().getCourse(), validation);
        }
        else {
            callback.accept(null, new DataValidation(false, ""));
        }
    }

    public void setCourse(Course course, BiConsumer<Course, DataValidation> callback) {
        DataValidation validation = this.courseReg.getValidatedForm().setCourse(course);
        updateFormStatus();
        callback.accept(this.courseReg.getValidatedForm().getCourse(), validation);
    }

    public void updateFormStatus() {
        if (this.formStatusCallback != null) {
            this.formStatusCallback.accept(this.courseReg.getValidatedForm().isFullyValid());
        }
    }

    public void registerFormStatusCallback(Consumer<Boolean> callback) {
        this.formStatusCallback = callback;
    }

    public void isFormComplete(Consumer<Boolean> callback) {
        callback.accept(this.courseReg.getValidatedForm().isFullyValid());
    }

    public void registerToCourse(Consumer<ModelResult<RegistrationForm>> callback) {
        callback.accept(this.courseReg.register());
    }
}
