package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Account;
import com.axelor.apps.rku.db.SemesterConfig;
import com.axelor.apps.rku.db.StudentPortal;
import com.axelor.apps.rku.db.Subject;
import com.axelor.apps.rku.db.repo.SubjectRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.util.List;

public class StudentPortalController {

  public void setSubject(ActionRequest request, ActionResponse response) {
    StudentPortal student = request.getContext().asType(StudentPortal.class);
    if (student.getBranchConfig() == null) {
      return;
    }
    if (student.getCurrentSem() == null) {
      return;
    }
    String filter =
        "self.branchConfig.name = '"
            + student.getBranchConfig().getName()
            + "' and self.semesterConfig.name = '"
            + student.getCurrentSem().getName()
            + "' and self.course.name = '"
            + student.getCourse().getName()
            + "' and self.active = true";
    List<Subject> subjects = Beans.get(SubjectRepository.class).all().filter(filter).fetch();
    response.setValue("subjectSet", subjects);
  }

  public void changeSemester(ActionRequest request, ActionResponse response) {
    StudentPortal student = request.getContext().asType(StudentPortal.class);
    response.setValue("subjectSet", null);
    response.setValue("attendanceInfoLine", null);
    response.setValue("changeSemester", false);
  }

  public void checkCurrentYearFee(ActionRequest request, ActionResponse response) {
    StudentPortal student = request.getContext().asType(StudentPortal.class);
    SemesterConfig sem = student.getCurrentSem();
    List<Account> accounts = student.getAccount();
    boolean flag = false;

    for (Account account : accounts) {
      if (account.getSemester().equals(sem)) {
        flag = true;
        break;
      }
    }
    if (flag) {
      response.setValue("currentYearFee", true);
      return;
    } else {
      response.setValue("currentYearFee", false);
      return;
    }
  }
}
