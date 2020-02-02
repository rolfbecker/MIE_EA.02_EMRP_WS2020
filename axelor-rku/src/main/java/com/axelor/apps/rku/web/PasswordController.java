package com.axelor.apps.rku.web;

import org.bouncycastle.crypto.PasswordConverter;
import org.pac4j.core.credentials.password.PasswordEncoder;

import com.axelor.apps.rku.db.Password;
import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;

public class PasswordController {

	@Transactional
  public void setPassword(ActionRequest request, ActionResponse response) {
    Password password = request.getContext().asType(Password.class);

    User user = Beans.get(UserRepository.class).all().filter("self.code = ?",password.getUserName()).fetchOne();
    System.err.println(user.getPassword());
    user.setPassword(".......");
    System.err.println(user.getPassword());
    Beans.get(UserRepository.class).save(user);
  }
}
