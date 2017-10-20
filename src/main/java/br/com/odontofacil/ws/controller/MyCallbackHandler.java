package br.com.odontofacil.ws.controller;

import org.springframework.stereotype.Component;

import com.github.marsbits.restfbmessenger.Messenger;
import com.github.marsbits.restfbmessenger.webhook.AbstractCallbackHandler;
import com.restfb.types.webhook.messaging.MessagingItem;

@Component
public class MyCallbackHandler extends AbstractCallbackHandler {

	@Override
	public void onMessage(Messenger messenger, MessagingItem messaging) {
		// TODO implement..
	}

}
