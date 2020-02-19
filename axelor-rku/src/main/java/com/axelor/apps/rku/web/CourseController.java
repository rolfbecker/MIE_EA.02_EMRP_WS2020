package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Course;
import com.axelor.apps.rku.service.CourseService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class CourseController {

  public void setCurrentYear(ActionRequest request, ActionResponse response) throws Exception {
    Course course = request.getContext().asType(Course.class);

    if (course.getCode() == null || course.getYear() == null || course.getSemester() == null) {
      throw new Exception("Please set code,year and Semester type.");
    }
    String code = course.getCode();
    String year = course.getYear();
    String sem = course.getSemester();
    String currentYear = code + "-" + year + "-" + sem;

    response.setValue("currentYear", currentYear);
  }

  public void changeCurrentSem(ActionRequest request, ActionResponse response) {
    Course course = request.getContext().asType(Course.class);

    String code = course.getCode();
    int year = Integer.parseInt(course.getYear()) + 1;
    String sem = "";
    if (course.getSemester().equals("odd")) {
      sem = "even";
      response.setValue("semester", sem);
    } else {
      sem = "odd";
      response.setValue("semester", sem);
    }
    String currentYear = code + "-" + year + "-" + sem;

    response.setValue("year", String.valueOf(year));
    response.setValue("currentYear", currentYear);
  }

  public void updateSudentPortal(ActionRequest request, ActionResponse response) {
    Course course = request.getContext().asType(Course.class);

    Beans.get(CourseService.class).updateStudent(course);
  }
}
