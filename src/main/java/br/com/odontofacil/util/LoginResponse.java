package br.com.odontofacil.util;

public class LoginResponse {
	
	public String token;
	public LoginResponse(String token){
		this.token=token;
	}
	public String getToken() {
		return token;
	}		
}