package course;

public interface CourseTableObserver {
    void onCourseAdded(String courseName);
    void onCourseEdited(String courseName);
    void onCourseDeleted(String courseName);
}
