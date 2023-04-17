package shared.models;

import java.util.regex.Pattern;

/**
 *
 */
public class ValidatedRegistrationForm {
    protected RegistrationForm form;
    private final boolean[] validationTracker = {false, false, false, false, false};

    /**
     * Construit un nouveau ValidatedRegistrationForm où les champs du RegistrationForm sont tous des String vides.
     */
    public ValidatedRegistrationForm() {
//        this(new RegistrationForm("", "", "", "", new Course("", "", "")));
    }

    /**
     * Indique si tous les champs du RegistrationForm sont valides.
     * @return true si tous les champs du RegistrationForm sont valides, sinon false.
     */
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
        if (Pattern.compile("\\d{8}").matcher(matricule).matches()) {
            this.form.setMatricule(matricule);
            validationTracker[3] = true;
            return new DataValidation(true);
        } else {
            validationTracker[3] = false;
            return new DataValidation(false, "Un matricule valide ne contient que 8 chiffres.");
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
}
