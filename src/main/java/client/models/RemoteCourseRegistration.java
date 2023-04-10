package client.models;

import models.RegistrationForm;

public class RemoteCourseRegistration extends CourseRegistration {
    public RemoteCourseRegistration() {
        super();
    }

    public RemoteCourseRegistration(RegistrationForm form) {
        super(form);
    }

    @Override
    public ModelResult<RegistrationForm> register() {
        ModelResult<RegistrationForm> result = new ModelResult<>();
        if (this.isFullyValid()) {
            // Send data to server
            result.data = this.form;
            result.success = true;
            result.message = "Form saved.";
        }
        else {
            result.success = false;
            result.message = "An error occurred.";
        }

        return result;
    }
}
