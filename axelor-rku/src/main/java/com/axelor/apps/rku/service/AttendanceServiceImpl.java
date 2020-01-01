package com.axelor.apps.rku.service;

import com.axelor.apps.rku.db.Attendance;
import com.axelor.apps.rku.db.AttendanceLine;
import com.axelor.apps.rku.db.BranchConfig;
import com.axelor.apps.rku.db.SemesterConfig;
import com.axelor.apps.rku.db.StudentPortal;
import com.axelor.apps.rku.db.Subject;
import com.axelor.apps.rku.db.repo.StudentPortalRepository;
import com.axelor.inject.Beans;
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
                "self.currentSem.name = ? and self.branch.branchConfig.name = ? and self.activeUser = true",
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

        attendancsLineList.add(attendanceLine);
        flag = false;
      }
    }

    return attendancsLineList;
  }

	@Override
	public void setAttendanceInfo(List<AttendanceLine> attendanceLines) {
		for(AttendanceLine attendanceLine : attendanceLines) {
			
			StudentPortal student = Beans.get(StudentPortalRepository.class)
					.all()
					.filter("self.userStudent = ?",attendanceLine.getStudent())
					.fetchOne();

			
		}
		
	}
}
