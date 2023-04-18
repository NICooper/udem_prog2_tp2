package shared.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sous-classe de CourseList. Représente le cas où la List de Course est stockée dans le répertoire de ce système et donc
 * ceci lit le fichier qui contient la liste.
 */
public class LocalCourseList extends CourseList {
    private final String filePath;

    /**
     * Crée une nouvelle LocalCourseList avec comme paramètre le chemin vers le fichier qui contient les cours.
     * @param filePath Le chemin vers le fichier qui contient les cours.
     */
    public LocalCourseList(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Lit le fichier stocké localement qui contient la liste de cours.
     * Le fichier doit être dans le format tsv:
     * code nom session
     * @return Un ModelResult qui contient la List de Course, l'état de la tentative de lecture de fichier, et
     * éventuellement un message d'erreur.
     */
    @Override
    public ModelResult<List<Course>> loadFilteredCourseList() {
        ModelResult<List<Course>> result = new ModelResult<>();

        try (FileReader fr = new FileReader(this.filePath)) {
            BufferedReader reader = new BufferedReader(fr);

            List<Course> courses = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseInfo = line.split("\t");
                if (this.getSessionFilter() == null || courseInfo[2].equals(this.getSessionFilter())) {
                    courses.add(new Course(courseInfo[1], courseInfo[0], courseInfo[2]));
                }
            }

            result.data = courses;
            result.success = true;
        } catch (FileNotFoundException fe) {
            result.success = false;
            result.message = "Erreur de serveur. Liste de cours pas trouvé.";
        } catch (IOException ex) {
            result.success = false;
            result.message = "Erreur de serveur. Erreur en lisant le fichier de cours.";
        } catch (Exception e) {
            result.success = false;
            result.message = "Erreur de serveur. Le fichier de cours n'est pas dans le bon format.";
        }

        return result;
    }
}
