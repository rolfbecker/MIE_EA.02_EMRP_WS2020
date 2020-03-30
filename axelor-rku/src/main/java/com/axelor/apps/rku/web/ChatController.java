package com.axelor.apps.rku.web;

import com.axelor.apps.rku.db.InternalChat;
import com.axelor.apps.rku.db.repo.InternalChatRepository;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;

public class ChatController {
	@Transactional
	 public void deletePost(ActionRequest request, ActionResponse response) {
		 InternalChatRepository chatRepo = Beans.get(InternalChatRepository.class);
		 InternalChat internalChat = request.getContext().asType(InternalChat.class);
		 InternalChat post = chatRepo.find(internalChat.getId());
		 chatRepo.remove(post); 
		 response.setCanClose(true);
	 }
}
