package com.axelor.apps.rku.service;

import com.axelor.apps.rku.db.BranchConfig;
import com.axelor.apps.rku.db.Result;
import com.axelor.apps.rku.db.ResultLine;
import com.axelor.apps.rku.db.SemesterConfig;
import com.axelor.apps.rku.db.StudentPortal;
import com.axelor.apps.rku.db.Subject;
import com.axelor.apps.rku.db.repo.StudentPortalRepository;
import com.axelor.inject.Beans;
import java.util.ArrayList;
import java.util.List;

public class ResultServiceImpl implements ResultService {

  @Override
  public List<ResultLine> getResultLine(Result result) {

    boolean flag = false;

    List<ResultLine> resultLineList = new ArrayList<ResultLine>();
    SemesterConfig semConfig = result.getSubject().getSemesterConfig();
    BranchConfig branchConfig = result.getSubject().getBranchConfig();

    List<StudentPortal> students =
        Beans.get(StudentPortalRepository.class)
            .all()
            .filter(
                "self.currentSem.name = ? and self.branchConfig.name = ? and self.activeUser = true",
                semConfig.getName(),
                branchConfig.getName())
            .fetch();

    for (StudentPortal student : students) {
      for (Subject subject : student.getSubjectSet()) {
        if (result.getSubject().equals(subject)) {
          flag = true;
        }
      }
      if (flag == true) {
        ResultLine resultLine = new ResultLine();

        resultLine.setStudent(student.getUserStudent());
        resultLine.setSubject(result.getSubject());
        resultLine.setExam(result.getExam());
        resultLine.setSemesterConfig(semConfig);
        resultLine.setBranchConfig(branchConfig);

        resultLineList.add(resultLine);
        flag = false;
      }
    }

    return resultLineList;
  }
}
