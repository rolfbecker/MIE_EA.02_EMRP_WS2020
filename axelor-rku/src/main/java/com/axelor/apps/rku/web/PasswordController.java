package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.Password;
import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import javax.mail.MessagingException;

public class PasswordController {

  @Transactional
  public void setPassword(ActionRequest request, ActionResponse response)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException,
          MessagingException, IOException, AxelorException {

    Password password = request.getContext().asType(Password.class);

    User user =
        Beans.get(UserRepository.class)
            .all()
            .filter("self.code = ?", password.getUserName())
            .fetchOne();
    System.err.println(user.getPassword());
    user.setTransientPassword("Swara@tata2");
    System.err.println();

    //     Map<String,Object> map = new HashMap<String,Object>();
    //     map.put("oldPassword", "admin");
    //     map.put("newPassword","Swara@tata2");
    //     map.put("chkPassword","Swara@tata2");
    //     User u = Beans.get(UserService.class).changeUserPassword(user, map);

    //    User i =  Beans.get(UserRepository.class).save(u);

    Beans.get(UserRepository.class).save(user);
  }
}
