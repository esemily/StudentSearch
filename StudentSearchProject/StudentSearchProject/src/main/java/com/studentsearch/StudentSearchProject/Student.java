package com.studentsearch.StudentSearchProject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Student {
    String first_name;
    String gender;
    String email;
    JSONArray courses;

    Student(String f, String g, String e, JSONArray c) {
        this.first_name = f;
        this.gender = g;
        this.email = e;
        this.courses = c;
    }

    Student(String f, String g, String e) {
        this.first_name = f;
        this.gender = g;
        this.email = e;
    }

    void PrintStudent(){
        System.out.println(first_name);
        System.out.println(gender);
        System.out.println(email);
        if (courses != null){
            System.out.println(courses);
            double total = 0;
            for (int i = 0; i < courses.size(); i++){
                JSONObject course = (JSONObject) courses.get(i);
                String letterGrade = (String) course.get("grade");
                if (letterGrade.equals("A")) {
                    total += 4;
                } else if (letterGrade.equals("B")) {
                    total += 3;
                }else if (letterGrade.equals("C")) {
                    total += 2;
                }else if (letterGrade.equals("D")) {
                    total += 1;
                }
            }
            double gpa = total/courses.size();
            System.out.println(gpa);
        }

    }
}
