package panel;

public interface TeacherTableObserver {
    void onTeacherViewed(String teacherName);
    void onTeacherEdited(String teacherName);
    void onTeacherAdded(String teacherName);
    void onTeacherDeleted(String teacherIdString);}
