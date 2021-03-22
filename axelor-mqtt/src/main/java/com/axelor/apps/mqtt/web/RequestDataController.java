package com.axelor.apps.mqtt.web;

import com.axelor.apps.mqtt.db.Device;
import com.axelor.apps.mqtt.db.DeviceData;
import com.axelor.apps.mqtt.db.Property;
import com.axelor.apps.mqtt.db.repo.DeviceDataRepository;
import com.axelor.apps.mqtt.db.repo.DeviceRepository;
import com.axelor.apps.mqtt.db.repo.PropertyRepository;
import com.axelor.meta.CallMethod;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RequestDataController {

  @Inject DeviceRepository deviceRepo;
  @Inject PropertyRepository propRepo;
  @Inject DeviceDataRepository deviceDataRepo;

  @SuppressWarnings({"unchecked", "static-access"})
  @CallMethod
  @Transactional
  public void getSensorData(ActionRequest request, ActionResponse response) {
    // System.err.println(request.getData());

    Map<String, Object> payload = request.getData();

    Device device = deviceRepo.all().filter("self.name =?1", payload.get("app_id")).fetchOne();

    Map<String, Object> payloadAllFields = (Map<String, Object>) payload.get("payload_fields");
    Map<String, Object> payloadFields = (Map<String, Object>) payloadAllFields.get("soil_sensor");

    Map<String, Object> metaData = (Map<String, Object>) (payload.get("metadata"));

    List<Property> propertys = device.getProperty();

    for (Property prop : propertys) {

      DeviceData newData = new DeviceData();
      String name = prop.getName();

      Map<String, Object> propMap = (Map<String, Object>) payloadFields.get(name);
      System.err.println(propMap);
      newData.setName(payload.get("app_id").toString());
      BigDecimal val = new BigDecimal(propMap.get("value").toString());
      newData.setValue(val);
      newData.setGetway(metaData.get("modulation").toString());
      newData.setTimeStamp(metaData.get("time").toString());
      newData.setFrequency(metaData.get("frequency").toString());
      newData.setProperty(prop);

      DeviceData d = deviceDataRepo.save(newData);
    }
  }
}
