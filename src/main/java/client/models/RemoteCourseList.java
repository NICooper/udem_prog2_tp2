package client.models;

import models.Course;

import java.util.ArrayList;
import java.util.List;

public class RemoteCourseList extends CourseList {

    public RemoteCourseList() {
        super();
    }

    @Override
    public ModelResult<List<Course>> loadFilteredCourseList() {
        var list = new ArrayList<Course>();
        list.add(new Course("Programmation 2", "IFT1025", "Hiver"));
        list.add(new Course("abc", "IFT1026", "Hiver"));
        list.add(new Course("Genie Logiciel", "IFT2255", "Automne"));
        list.add(new Course("def", "IFT2256", "Automne"));

        var filteredList = list.stream().filter((course) -> this.getSessionFilter().equals(course.getSession())).toList();

        ModelResult<List<Course>> result = new ModelResult<>(filteredList, true, "");

        return result;
    }
}
