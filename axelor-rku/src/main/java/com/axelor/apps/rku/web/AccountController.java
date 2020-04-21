package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Account;
import com.axelor.apps.rku.db.BranchConfig;
import com.axelor.apps.rku.db.Course;
import com.axelor.apps.rku.db.Fee;
import com.axelor.apps.rku.db.SemesterConfig;
import com.axelor.apps.rku.db.StudentPortal;
import com.axelor.apps.rku.db.repo.AccountRepository;
import com.axelor.apps.rku.db.repo.FeeRepository;
import com.axelor.apps.rku.db.repo.StudentPortalRepository;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.List;

public class AccountController {

  public void setFeeDetails(ActionRequest request, ActionResponse response) {
    Account account = request.getContext().asType(Account.class);

    if (account.getStudent() == null) {
      response.setValue("course", null);
      response.setValue("branchConfig", null);
      response.setValue("semester", null);
      response.setValue("tutionFee", 0);
      response.setValue("examFee", 0);
      response.setValue("isPayTutionFee", false);
      response.setValue("isPayExamFee", false);
      response.setValue("scolerShip", 0);
      response.setValue("onHold", false);
      response.setValue("holdReason", null);
      response.setValue("currentYear", null);
      return;
    }

    StudentPortal student = account.getStudent();
    Course course = student.getCourse();
    SemesterConfig semester = student.getCurrentSem();
    BranchConfig branch = student.getBranchConfig();

    Fee fee =
        Beans.get(FeeRepository.class)
            .all()
            .filter(
                "self.course = ? AND self.branchConfig = ? AND self.semester = ?",
                course,
                branch,
                semester)
            .fetchOne();

    if (fee == null) {
      response.setValue("course", null);
      response.setValue("branchConfig", null);
      response.setValue("semester", null);
      response.setValue("student", null);
      response.setValue("tutionFee", 0);
      response.setValue("examFee", 0);
      response.setValue("isPayTutionFee", false);
      response.setValue("isPayExamFee", false);
      response.setValue("scolerShip", 0);
      response.setValue("onHold", false);
      response.setValue("holdReason", null);
      response.setValue("currentYear", null);
      response.setFlash("Please set fee details");
      return;
    }

    response.setValue("course", fee.getCourse());
    response.setValue("branchConfig", fee.getBranchConfig());
    response.setValue("semester", fee.getSemester());
    response.setValue("tutionFee", fee.getTutionFees());
    response.setValue("examFee", fee.getExamFees());
    response.setValue("currentYear", fee.getCourse().getCurrentYear());
  }

  public void lessScolerShip(ActionRequest request, ActionResponse response) {
    Account account = request.getContext().asType(Account.class);
    BigDecimal examFee = account.getExamFee();
    BigDecimal scolerShip = account.getScolerShip();
    BigDecimal totalFee = examFee.subtract(scolerShip);

    if (totalFee.compareTo(new BigDecimal("0.00")) < 0) {
      response.setValue("scolerShip", 0);
      response.setFlash("invalid scoler ship..!!");
    }

    response.setValue("tutionFee", totalFee);
  }

  @Transactional(rollbackOn = {AxelorException.class, Exception.class})
  public void payStudentFee(ActionRequest request, ActionResponse response) {
    Account account = request.getContext().asType(Account.class);
    StudentPortalRepository studentRepo = Beans.get(StudentPortalRepository.class);
    StudentPortal student = account.getStudent();
    SemesterConfig sem = student.getCurrentSem();
    List<Account> fees = student.getAccount();
    boolean flag = false;

    for (Account fee : fees) {
      if (fee.getSemester().equals(sem)) {
        flag = true;
        break;
      }
    }
    if (flag) {
      student.setCurrentYearFee(true);
      studentRepo.save(student);
      return;
    } else {
      student.setCurrentYearFee(false);
      studentRepo.save(student);
      return;
    }
  }

  @Transactional(rollbackOn = {AxelorException.class, Exception.class})
  public void deleteRecord(ActionRequest request, ActionResponse response) {
    Account account = request.getContext().asType(Account.class);
    AccountRepository accountRepo = Beans.get(AccountRepository.class);
    StudentPortalRepository studentRepo = Beans.get(StudentPortalRepository.class);
    StudentPortal student = account.getStudent();

    try {
      student.setCurrentYearFee(false);
      studentRepo.save(student);
      Account record = accountRepo.find(account.getId());
      accountRepo.remove(record);
    } catch (NullPointerException e) {
      response.setFlash("Post has been Removed");
      return;
    }
    response.setCanClose(true);
  }
}
