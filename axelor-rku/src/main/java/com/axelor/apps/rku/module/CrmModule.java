/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2019 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.rku.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.rku.service.AttendanceService;
import com.axelor.apps.rku.service.AttendanceServiceImpl;
import com.axelor.apps.rku.service.CourseService;
import com.axelor.apps.rku.service.CourseServiceImpl;
import com.axelor.apps.rku.service.ResultService;
import com.axelor.apps.rku.service.ResultServiceImpl;

public class CrmModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(AttendanceService.class).to(AttendanceServiceImpl.class);
    bind(ResultService.class).to(ResultServiceImpl.class);
    bind(CourseService.class).to(CourseServiceImpl.class);
  }
}
