package br.com.odontofacil.ws.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import br.com.odontofacil.model.Agendamento;

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

}