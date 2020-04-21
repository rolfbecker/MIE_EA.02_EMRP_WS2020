package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Attendance;
import com.axelor.apps.rku.db.AttendanceInfoLine;
import com.axelor.apps.rku.db.AttendanceLine;
import com.axelor.apps.rku.db.repo.AttendanceInfoLineRepository;
import com.axelor.apps.rku.db.repo.AttendanceLineRepository;
import com.axelor.apps.rku.db.repo.AttendanceRepository;
import com.axelor.apps.rku.service.AttendanceService;
import com.axelor.apps.rku.service.AttendanceServiceImpl;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;
import java.util.List;

public class AttendanceController {

  public void getAttendanceLineList(ActionRequest request, ActionResponse response) {

    Attendance attendance = request.getContext().asType(Attendance.class);

    if (attendance.getSubject() == null || attendance.getAttendanceDate() == null) {
      return;
    }

    List<AttendanceLine> attendancsLineList =
        Beans.get(AttendanceServiceImpl.class).getAttendanceLine(attendance);

    response.setValue("attendanceLine", attendancsLineList);
  }

  public void changeLineDate(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);
    if (attendance.getSubject() == null || attendance.getAttendanceDate() == null) {
      return;
    }
    List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
    for (AttendanceLine aLine : attendanceLines) {
      aLine.setAttendanceDate(attendance.getAttendanceDate());
    }
    response.setValue("attendanceLine", attendanceLines);
  }

  public void setPresent(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);

    if (attendance.getAttendanceLine() == null) {
      return;
    }
    List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
    if (request.getContext().get("_signal").toString().equals("present")) {
      for (AttendanceLine attendanceLine : attendanceLines) {
        if (attendanceLine.isSelected()) {
          attendanceLine.setPresent(1);
        } else {
          attendanceLine.setPresent(0);
        }
      }
    } else {
      for (AttendanceLine attendanceLine : attendanceLines) {
        if (attendanceLine.isSelected()) {
          attendanceLine.setPresent(0);
        } else {
          attendanceLine.setPresent(1);
        }
      }
    }

    response.setValue("attendanceLine", attendanceLines);
  }

  public void updateAttendanceLine(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);

    if (attendance.getAttendanceLine() == null) {
      return;
    }
    List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
    for (AttendanceLine attendanceLine : attendanceLines) {
      if (attendanceLine.isSelected()) {
        if (attendanceLine.getPresent() == 1) {
          attendanceLine.setPresent(0);
        } else {
          attendanceLine.setPresent(1);
        }
      }
    }
    response.setValue("attendanceLine", attendanceLines);
  }

  public void getTodayAttendance(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);

    if (attendance.getAttendanceDate() == null && attendance.getUserFaculty() == null) {
      return;
    }
    List<AttendanceLine> attendanceLine =
        Beans.get(AttendanceLineRepository.class)
            .all()
            .filter(
                "self.student = ? and self.attendanceDate = ?",
                attendance.getUserFaculty(),
                attendance.getAttendanceDate())
            .fetch();
    response.setValue("attendanceLine", attendanceLine);
  }

  public void getOverallAttendance(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);
    if (attendance.getSemester() == null) {
      response.setFlash("Please select semester..");
      return;
    }
    List<AttendanceInfoLine> attendanceInfo =
        Beans.get(AttendanceInfoLineRepository.class)
            .all()
            .filter(
                "self.studentPortal.userStudent = ? and self.semesterConfig = ?",
                attendance.getUserFaculty(),
                attendance.getSemester())
            .fetch();

    response.setValue("attendanceInfoLine", attendanceInfo);
  }

  public void getStudentAttendanceInfo(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);
    if (attendance.getSubject() == null) {
      response.setFlash("Please select subject..");
      return;
    }

    List<AttendanceInfoLine> attendanceInfo =
        Beans.get(AttendanceInfoLineRepository.class)
            .all()
            .filter(
                "self.subject = ? and self.currentYear = ?",
                attendance.getSubject(),
                attendance.getSubject().getCourse().getCurrentYear())
            .fetch();

    response.setValue("attendanceInfoLine", attendanceInfo);
  }

  public void setAllPresent(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);
    List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
    if (attendanceLines == null) {
      return;
    }
    System.err.println(request.getContext().get("_signal"));
    if (request.getContext().get("_signal").toString().equals("allPresent")) {
      for (AttendanceLine attendanceLine : attendanceLines) {
        attendanceLine.setPresent(1);
      }
    } else {
      for (AttendanceLine attendanceLine : attendanceLines) {
        attendanceLine.setPresent(0);
      }
    }

    response.setValue("attendanceLine", attendanceLines);
  }

  public void setAttandanceInfo(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);
    Beans.get(AttendanceService.class).setAttendanceInfo(attendance);
  }

  public void changeAttendanceInfoOnDelete(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);
    Beans.get(AttendanceService.class).setAttendanceInfoOnDelete(attendance);
  }

  @Transactional
  public void deleteAttendace(ActionRequest request, ActionResponse response) {
    Attendance attendance = request.getContext().asType(Attendance.class);
    AttendanceRepository attendanceRepo = Beans.get(AttendanceRepository.class);
    try {
      Attendance record = attendanceRepo.find(attendance.getId());
      attendanceRepo.remove(record);
    } catch (NullPointerException e) {
      response.setFlash("Post has been Removed");
    }
    response.setCanClose(true);
  }
}
