package br.com.odontofacil.ws.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.Body;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;
import com.twilio.type.PhoneNumber;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.Orcamento;

@RestController
public class SmsClientController {

	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "AC370dbb0328e678dc489320960625c0de";
	public static final String AUTH_TOKEN = "48915a17864687ad94d8b0f102d4b7d2";
	

	public void EnviaSMS(Agendamento agendamento) {
		// Initialize the Twilio client.
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	    // Declare To and From numbers and the Body of the SMS message
	    PhoneNumber to = new PhoneNumber("+"+agendamento.getCliente().getTelefonePrincipal()); // Replace with a valid phone number for your account.
	    PhoneNumber from = new PhoneNumber("+17472290974"); // Replace with a valid phone number for your account.
	    String body = "Consulta agendada para "+ agendamento.getStart().getTime();

	    // Create a Message creator passing From, To and Body values
	    // then send the SMS message by calling the create() method
	    Message sms = Message.creator(to, from, body).create();
	    
	    System.out.println(sms.getSid());
	}
	
//	@RequestMapping(
//			value = "/recebe-sms", 
//			method={RequestMethod.POST},
//			produces = MediaType.APPLICATION_JSON_VALUE,
//			consumes = MediaType.APPLICATION_JSON_VALUE
//			)
//	public String RecebeSMS (){
//		com.twilio.twiml.Message sms
//		      new Message.Builder().body(new Body("The Robots are coming! Head for the hills!")).build();
//
//		    MessagingResponse twiml = new MessagingResponse.Builder().message(sms).build();
//
//		   return twiml.toXml();
//		  }
		
}