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
package com.axelor.apps.mqtt.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class AppCrmController {

  //  @Inject private AppCrmService appCrmService;

  public void fetchSensorData(ActionRequest request, ActionResponse response) {
    //	  System.err.println("hello");
    //	  FileReader reader = null;
    //	    try {
    //	      reader = new FileReader("/home/vikas/my_file.json");
    //	      System.err.println("ppp"+reader.read());
    //	    } catch (Exception e) {
    //	      System.err.println("error in file");
    //	    }
    //
    //	    Object obj = null;
    //	    try {
    //	    	JSONParser jsonParser = new JSONParser();
    //	    	System.err.println("sdsd");
    //	      obj = jsonParser.parse(reader);
    //	    } catch (Exception e) {
    //
    //	    }
    //
    //	    JSONArray array = (JSONArray) obj;
    //	    System.err.println(array);
    //
    //	   Map map = new HashMap<>();
    //	    map.put("jsonArray", array);
    //
    //	    System.err.println(map);

    System.err.println("hello");

    response.setReload(true);
  }
}
