package student;

public interface StudentsObserver {
    void onStudentViewed(String studentName);
    void onStudentEdited(String studentName);
    void onStudentAdded(String studentName);
    void onStudentDeleted(String studentName);
}
