package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.InternalChat;
import com.axelor.apps.rku.db.repo.InternalChatRepository;
import com.axelor.auth.AuthUtils;
import com.axelor.auth.db.User;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;
import java.util.Set;

public class ChatController {

  InternalChatRepository chatRepo = Beans.get(InternalChatRepository.class);

  @Transactional(rollbackOn = {AxelorException.class, Exception.class})
  public void deletePost(ActionRequest request, ActionResponse response) {
    InternalChat internalChat = request.getContext().asType(InternalChat.class);
    try {
      InternalChat post = chatRepo.find(internalChat.getId());
      chatRepo.remove(post);
    } catch (NullPointerException e) {
      response.setFlash("Post has been Removed");
    }
    response.setCanClose(true);
  }

  public void setLike(ActionRequest request, ActionResponse response) {
    InternalChat internalChat = request.getContext().asType(InternalChat.class);
    User user = AuthUtils.getUser();
    Set<User> userlikes = internalChat.getUserLikes();
    userlikes.add(user);
    internalChat.setUserLikes(userlikes);
    response.setValue("userLikes", userlikes);
  }

  public void removeLike(ActionRequest request, ActionResponse response) {
    InternalChat internalChat = request.getContext().asType(InternalChat.class);
    User user = AuthUtils.getUser();
    Set<User> userlikes = internalChat.getUserLikes();
    userlikes.remove(user);
    internalChat.setUserLikes(userlikes);
    response.setValue("userLikes", userlikes);
  }
}
