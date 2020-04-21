package com.axelor.apps.rku.service;

import com.axelor.apps.rku.db.Attendance;
import com.axelor.apps.rku.db.AttendanceLine;
import java.util.List;

public interface AttendanceService {

  List<AttendanceLine> getAttendanceLine(Attendance attendance);

  void setAttendanceInfo(Attendance attendance);

  void setAttendanceInfoOnDelete(Attendance attendance);
}
