package com.axelor.apps.rku.service;

import com.axelor.apps.rku.db.Course;
import com.axelor.apps.rku.db.SemesterConfig;
import com.axelor.apps.rku.db.StudentPortal;
import com.axelor.apps.rku.db.repo.SemesterConfigRepository;
import com.axelor.apps.rku.db.repo.StudentPortalRepository;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;
import java.util.List;

public class CourseServiceImpl implements CourseService {

  @Override
  @Transactional(rollbackOn = {Exception.class})
  public void updateStudent(Course course) {
    List<StudentPortal> students =
        Beans.get(StudentPortalRepository.class)
            .all()
            .filter("self.course = ? and self.activeUser = ?", course, true)
            .fetch();

    for (StudentPortal student : students) {

      if (student.getCurrentSem() == null) {
        continue;
      }
      SemesterConfig sem = student.getCurrentSem();

      int maxSem = course.getTotalSem();
      if (maxSem == sem.getSem()) {
        student.setActiveUser(false);
        student.setUpdateInfo(false);
        Beans.get(StudentPortalRepository.class).save(student);
        continue;
      }

      int semester = sem.getSem() + 1;
      SemesterConfig newSem =
          Beans.get(SemesterConfigRepository.class)
              .all()
              .filter("self.sem = ?", semester)
              .fetchOne();

      student.setCurrentSem(newSem);
      student.setUpdateInfo(true);
      student.setChangeSemester(true);
      Beans.get(StudentPortalRepository.class).save(student);
    }
  }
}
