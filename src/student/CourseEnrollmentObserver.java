package student;


public interface CourseEnrollmentObserver {
    void courseEnrolled(String courseName, int level);
    void courseUnenrolled(String courseName, int level);
}
