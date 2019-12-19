package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Branch;
import com.axelor.apps.rku.db.Semester;
import com.axelor.apps.rku.db.repo.SemesterRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.util.List;

public class BranchController {

  public void selectSemester(ActionRequest request, ActionResponse response) {
    Branch branch = request.getContext().asType(Branch.class);

    if (branch.getBranchConfig() == null) {
      return;
    }
    String filter = "self.branchConfig.name = '" + branch.getBranchConfig().getName() + "'";
    List<Semester> semesters = Beans.get(SemesterRepository.class).all().filter(filter).fetch();
    response.setValue("semesterList", semesters);
  }
}
