package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Subject;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class SubjectController {

  public void setFullName(ActionRequest request, ActionResponse response) {
    Subject subject = request.getContext().asType(Subject.class);

    if (subject.getBranchConfig() == null
        || subject.getSemesterConfig() == null
        || subject.getName() == null) {
      return;
    }

    String name = subject.getName();
    String branchName = subject.getBranchConfig().getName();
    String semName = subject.getSemesterConfig().getName();

    response.setValue("fullName", name + "(" + branchName + "-" + semName + ")");
  }
}
