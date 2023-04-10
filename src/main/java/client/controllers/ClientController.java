package client.controllers;

import client.models.CourseList;
import client.models.CourseRegistration;
import client.models.DataValidation;
import client.models.ModelResult;
import models.Course;
import models.RegistrationForm;

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
        DataValidation validation = this.courseReg.setFirstName(firstName);
        updateFormStatus();
        callback.accept(this.courseReg.getFirstName(), validation);
    }

    public void setLastName(String lastName, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.setLastName(lastName);
        updateFormStatus();
        callback.accept(this.courseReg.getLastName(), validation);
    }

    public void setEmail(String email, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.setEmail(email);
        updateFormStatus();
        callback.accept(this.courseReg.getEmail(), validation);
    }

    public void setMatricule(String matricule, BiConsumer<String, DataValidation> callback) {
        DataValidation validation = this.courseReg.setMatricule(matricule);
        updateFormStatus();
        callback.accept(this.courseReg.getMatricule(), validation);
    }

    public void setCourse(Course course, BiConsumer<Course, DataValidation> callback) {
        DataValidation validation = this.courseReg.setCourse(course);
        updateFormStatus();
        callback.accept(this.courseReg.getCourse(), validation);
    }

    public void updateFormStatus() {
        if (this.formStatusCallback != null) {
            this.formStatusCallback.accept(this.courseReg.isFullyValid());
        }
    }

    public void registerFormStatusCallback(Consumer<Boolean> callback) {
        this.formStatusCallback = callback;
    }

    public void isFormComplete(Consumer<Boolean> callback) {
        callback.accept(this.courseReg.isFullyValid());
    }

    public void registerToCourse(Consumer<ModelResult<RegistrationForm>> callback) {
        callback.accept(this.courseReg.register());
    }
}
