package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Semester;
import com.axelor.apps.rku.db.Subject;
import com.axelor.apps.rku.db.repo.SubjectRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.util.List;

public class SemesterController {

  public void setSemesterName(ActionRequest request, ActionResponse response) {

    Semester semester = request.getContext().asType(Semester.class);

    if (semester.getBranchConfig() == null || semester.getSemesterConfig() == null) {
      return;
    }

    String branchName = semester.getBranchConfig().getName();
    String semName = semester.getSemesterConfig().getName();

    response.setValue("name", branchName + "(" + semName + ")");
  }

  public void selectSubject(ActionRequest request, ActionResponse response) {

    Semester semester = request.getContext().asType(Semester.class);
    if (semester.getBranchConfig() == null || semester.getSemesterConfig() == null) {
      return;
    }
    String semesterName = semester.getSemesterConfig().getName();
    String branchName = semester.getBranchConfig().getName();
    String filter =
        "self.branchConfig.name = '"
            + branchName
            + "' AND self.semesterConfig.name = '"
            + semesterName
            + "'";

    List<Subject> subjects = Beans.get(SubjectRepository.class).all().filter(filter).fetch();

    response.setValue("subjectList", subjects);
  }
}
