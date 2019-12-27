package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Attendance;
import com.axelor.apps.rku.db.AttendanceLine;
import com.axelor.apps.rku.db.BranchConfig;
import com.axelor.apps.rku.db.SemesterConfig;
import com.axelor.apps.rku.db.StudentPortal;
import com.axelor.apps.rku.db.Subject;
import com.axelor.apps.rku.db.repo.StudentPortalRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.util.ArrayList;
import java.util.List;

public class AttendanceController {

  public void getAttendanceLineList(ActionRequest request, ActionResponse response) {

    Attendance attendance = request.getContext().asType(Attendance.class);
    boolean flag = false;
    
    if(attendance.getSubject() == null || attendance.getAttendanceDate() == null) {
    	return;
    }

    List<AttendanceLine> attendancsLineList = new ArrayList<AttendanceLine>();
    SemesterConfig semConfig = attendance.getSubject().getSemesterConfig();
    BranchConfig branchConfig = attendance.getSubject().getBranchConfig();

    List<StudentPortal> students =
        Beans.get(StudentPortalRepository.class)
            .all()
            .filter("self.currentSem.name = ? and self.branch.branchConfig.name = ? and self.activeUser = true",
            semConfig.getName(),
            branchConfig.getName())
            .fetch();

    for (StudentPortal student : students) {
    	for(Subject subject : student.getSubjectSet()) {
    		if(attendance.getSubject().equals(subject)) {
    			flag = true;
    		}
    	}
    	if(flag == true) {
    		 AttendanceLine attendanceLine = new AttendanceLine();

    	      attendanceLine.setStudent(student.getUserStudent());
    	      attendanceLine.setAttendanceDate(attendance.getAttendanceDate());
    	      attendanceLine.setSubject(attendance.getSubject());
    	      attendanceLine.setSemesterConfig(semConfig);

    	      attendancsLineList.add(attendanceLine);
    	      flag = false;
    	}
     
    }

    response.setValue("attendanceLine", attendancsLineList);
  }
  
  public void changeLineDate(ActionRequest request, ActionResponse response) {
	  Attendance attendance = request.getContext().asType(Attendance.class);
	  if(attendance.getSubject() == null || attendance.getAttendanceDate() == null) {
	    	return;
	    }
	  List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
	  for(AttendanceLine aLine : attendanceLines) {
		  aLine.setAttendanceDate(attendance.getAttendanceDate());
		  
	  }
	  response.setValue("attendanceLine",attendanceLines);
  }
  
  public void setPresent(ActionRequest request, ActionResponse response) {
	  Attendance attendance = request.getContext().asType(Attendance.class);
	  List<AttendanceLine> attendanceLines = attendance.getAttendanceLine();
	  if(request.getContext().get("_signal").toString().equals("present")) {
		  for(AttendanceLine attendanceLine : attendanceLines) {
			  if(attendanceLine.isSelected()) {
				  attendanceLine.setPresent(1);
			  }else {
				  attendanceLine.setPresent(0);
			  }
		  }
	  }else {
		  for(AttendanceLine attendanceLine : attendanceLines) {
			  if(attendanceLine.isSelected()) {
				  attendanceLine.setPresent(0);
			  }else {
				  attendanceLine.setPresent(1);
			  }
		  }
	  }
	  
	  response.setValue("attendanceLine", attendanceLines);
  }
}
