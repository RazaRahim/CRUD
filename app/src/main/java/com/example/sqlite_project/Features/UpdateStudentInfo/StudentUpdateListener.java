package com.example.sqlite_project.Features.UpdateStudentInfo;

import com.example.sqlite_project.Features.CreateStudent.Student;

public interface StudentUpdateListener {
    void onStudentInfoUpdated(Student student, int position);
}
