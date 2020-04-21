package com.axelor.apps.rku.service;

import com.axelor.apps.rku.db.Attendance;
import com.axelor.apps.rku.db.AttendanceInfoLine;
import com.axelor.apps.rku.db.AttendanceLine;
import com.axelor.apps.rku.db.BranchConfig;
import com.axelor.apps.rku.db.SemesterConfig;
import com.axelor.apps.rku.db.StudentPortal;
import com.axelor.apps.rku.db.Subject;
import com.axelor.apps.rku.db.repo.AttendanceInfoLineRepository;
import com.axelor.apps.rku.db.repo.StudentPortalRepository;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AttendanceServiceImpl implements AttendanceService {

  @Override
  public List<AttendanceLine> getAttendanceLine(Attendance attendance) {

    boolean flag = false;

    List<AttendanceLine> attendancsLineList = new ArrayList<AttendanceLine>();
    SemesterConfig semConfig = attendance.getSubject().getSemesterConfig();
    BranchConfig branchConfig = attendance.getSubject().getBranchConfig();

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
        if (attendance.getSubject().equals(subject)) {
          flag = true;
        }
      }
      if (flag == true) {
        AttendanceLine attendanceLine = new AttendanceLine();

        attendanceLine.setStudent(student.getUserStudent());
        attendanceLine.setAttendanceDate(attendance.getAttendanceDate());
        attendanceLine.setSubject(attendance.getSubject());
        attendanceLine.setSemesterConfig(semConfig);
        attendanceLine.setBranchConfig(branchConfig);
        attendanceLine.setCurrentYear(attendance.getCurrentYear());

        attendancsLineList.add(attendanceLine);
        flag = false;
      }
    }

    return attendancsLineList;
  }

  @Override
  @Transactional(rollbackOn = {AxelorException.class, Exception.class})
  public void setAttendanceInfo(Attendance attendance) {
    List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
    AttendanceInfoLineRepository attendanceInfoLineRepo =
        Beans.get(AttendanceInfoLineRepository.class);
    StudentPortalRepository studentPortalRepo = Beans.get(StudentPortalRepository.class);
    for (AttendanceLine attendanceLine : attendanceLines) {

      StudentPortal student =
          studentPortalRepo
              .all()
              .filter("self.userStudent = ?", attendanceLine.getStudent())
              .fetchOne();

      AttendanceInfoLine attendanceInfo =
          attendanceInfoLineRepo
              .all()
              .filter(
                  "self.studentPortal = ? and self.subject = ?",
                  student,
                  attendanceLine.getSubject())
              .fetchOne();

      if (attendanceInfo == null) {
        AttendanceInfoLine infoLine = new AttendanceInfoLine();
        infoLine.setSubject(attendanceLine.getSubject());
        infoLine.setSemesterConfig(attendanceLine.getSemesterConfig());
        infoLine.setBranchConfig(attendanceLine.getBranchConfig());
        infoLine.setStudentPortal(student);
        infoLine.setCurrentYear(attendanceLine.getCurrentYear());
        if (attendanceLine.getPresent() == 1) {
          infoLine.setTotalPresent(1);
          infoLine.setTotalAbsent(0);
          infoLine.setTotalLecture(1);
          infoLine.setTotalPercent(new BigDecimal(100));
        } else {
          infoLine.setTotalAbsent(1);
          infoLine.setTotalPresent(0);
          infoLine.setTotalLecture(1);
          infoLine.setTotalPercent(new BigDecimal(00));
        }

        attendanceInfoLineRepo.save(infoLine);
      } else {
        if (attendance.getUpdateAttendance()) {
          if (attendanceLine.isSelected()) {
            if (attendanceLine.getPresent() == 0) {
              attendanceInfo.setTotalPresent(attendanceInfo.getTotalPresent() - 1);
              attendanceInfo.setTotalAbsent(attendanceInfo.getTotalAbsent() + 1);
            } else {
              attendanceInfo.setTotalPresent(attendanceInfo.getTotalPresent() + 1);
              attendanceInfo.setTotalAbsent(attendanceInfo.getTotalAbsent() - 1);
            }
          }
          float totalPercent =
              (attendanceInfo.getTotalPresent() * 100) / attendanceInfo.getTotalLecture();
          attendanceInfo.setTotalPercent(new BigDecimal(totalPercent));

        } else {
          if (attendanceLine.getPresent() == 1) {
            attendanceInfo.setTotalPresent(attendanceInfo.getTotalPresent() + 1);
            attendanceInfo.setTotalLecture(
                attendanceInfo.getTotalPresent() + attendanceInfo.getTotalAbsent());
          } else {
            attendanceInfo.setTotalAbsent(attendanceInfo.getTotalAbsent() + 1);
            attendanceInfo.setTotalLecture(
                attendanceInfo.getTotalPresent() + attendanceInfo.getTotalAbsent());
          }
          float totalPercent =
              (attendanceInfo.getTotalPresent() * 100) / attendanceInfo.getTotalLecture();
          attendanceInfo.setTotalPercent(new BigDecimal(totalPercent));
        }
      }
    }
  }

  @Override
  @Transactional(rollbackOn = {AxelorException.class, Exception.class})
  public void setAttendanceInfoOnDelete(Attendance attendance) {
    List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
    AttendanceInfoLineRepository attendanceInfoLineRepo =
        Beans.get(AttendanceInfoLineRepository.class);
    StudentPortalRepository studentPortalRepo = Beans.get(StudentPortalRepository.class);
    for (AttendanceLine attendanceLine : attendanceLines) {

      StudentPortal student =
          studentPortalRepo
              .all()
              .filter("self.userStudent = ?", attendanceLine.getStudent())
              .fetchOne();

      AttendanceInfoLine attendanceInfo =
          attendanceInfoLineRepo
              .all()
              .filter(
                  "self.studentPortal = ? and self.subject = ?",
                  student,
                  attendanceLine.getSubject())
              .fetchOne();

      if (attendanceInfo == null) {
        return;
      } else {
        if (attendance.getUpdateAttendance()) {
          if (attendanceLine.getPresent() == 0) {
            attendanceInfo.setTotalAbsent(attendanceInfo.getTotalAbsent() - 1);
            attendanceInfo.setTotalLecture(attendanceInfo.getTotalLecture() - 1);
          } else {
            attendanceInfo.setTotalPresent(attendanceInfo.getTotalPresent() - 1);
            attendanceInfo.setTotalLecture(attendanceInfo.getTotalLecture() - 1);
          }
          if (attendanceInfo.getTotalLecture() == 0) {
            attendanceInfo.setTotalPercent(new BigDecimal(00));
          } else {
            float totalPercent =
                (attendanceInfo.getTotalPresent() * 100) / attendanceInfo.getTotalLecture();
            attendanceInfo.setTotalPercent(new BigDecimal(totalPercent));
          }
        }
      }
    }
  }
}
