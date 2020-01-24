package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Result;
import com.axelor.apps.rku.db.ResultLine;
import com.axelor.apps.rku.db.repo.ResultLineRepository;
import com.axelor.apps.rku.service.ResultService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.util.List;

public class ResultController {

  public void getResultLineList(ActionRequest request, ActionResponse response) {

    Result result = request.getContext().asType(Result.class);

    if (result.getSubject() == null || result.getExam() == null) {
      return;
    }

    List<ResultLine> resultLineList = Beans.get(ResultService.class).getResultLine(result);

    response.setValue("resultLine", resultLineList);
  }

  public void generateResult(ActionRequest request, ActionResponse response) {
    Result result = request.getContext().asType(Result.class);

    if (result.getExam() == null
        && result.getUserFaculty() == null
        && result.getSemester() == null) {
      return;
    }
    List<ResultLine> resultLine =
        Beans.get(ResultLineRepository.class)
            .all()
            .filter(
                "self.student = ? and self.exam = ? and self.semesterConfig = ?",
                result.getUserFaculty(),
                result.getExam(),
                result.getSemester())
            .fetch();
    response.setValue("resultLine", resultLine);
  }
}
