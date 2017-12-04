package br.com.odontofacil.util;

import java.util.InputMismatchException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import br.com.odontofacil.model.Funcionario;


public class Util {
	
	private static String key = "$tM4l8OhfQ6&6f%#";
	
	public static void validarCPF(String cpf) throws Exception {
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
	    if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
	    	cpf.equals("22222222222") || cpf.equals("33333333333") ||
	    	cpf.equals("44444444444") || cpf.equals("55555555555") ||
	    	cpf.equals("66666666666") || cpf.equals("77777777777") ||
	    	cpf.equals("88888888888") || cpf.equals("99999999999") ||
	       (cpf.length() != 11)) {	  
	    	throw new Exception("O CPF informado é inválido!");
	    }

	    char dig10, dig11;
	    int sm, i, r, num, peso;

	    // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
	    try {
	    	// Calculo do 1o. Digito Verificador
	    	sm = 0;
	    	peso = 10;
	    	for (i=0; i<9; i++) {              
	    		// converte o i-esimo caractere do CPF em um numero:
	    		// por exemplo, transforma o caractere '0' no inteiro 0         
	    		// (48 eh a posicao de '0' na tabela ASCII)         
	    		num = (int)(cpf.charAt(i) - 48); 
	    		sm = sm + (num * peso);
	    		peso = peso - 1;
	    	}

	    	r = 11 - (sm % 11);
	    	if ((r == 10) || (r == 11))
	    		dig10 = '0';
	    	else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

	    	// Calculo do 2o. Digito Verificador
	    	sm = 0;
	    	peso = 11;
	    	for(i=0; i<10; i++) {
	    		num = (int)(cpf.charAt(i) - 48);
	    		sm = sm + (num * peso);
	    		peso = peso - 1;
	    	}

	    	r = 11 - (sm % 11);
	    	if ((r == 10) || (r == 11))
	    		dig11 = '0';
	    	else dig11 = (char)(r + 48);

	    	// Verifica se os digitos calculados conferem com os digitos informados.
	    	if ((dig10 != cpf.charAt(9)) || (dig11 != cpf.charAt(10))) {
	    		throw new Exception("O CPF informado é inválido!");
	    	}
	    } catch (InputMismatchException erro) {	
	    	throw new Exception("O CPF informado é inválido!");
	    }
	}
	
	public static String encrypt(String texto, Funcionario funcionario) throws Exception {
		System.out.println("Util.encrypt(texto, funcionario): início");
		byte[] key = funcionario.getChave();		
		
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES");        
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(texto.getBytes());        

        System.out.println("Util.encrypt(texto, funcionario): fim");
        return Base64.encodeBase64String(encrypted);
    }
	
	 public static String decrypt(String encrypted) throws Exception {
	    	System.out.println("Util.decrypt(texto): início");
	        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

	        Cipher cipher = Cipher.getInstance("AES");                
	    	cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    	 
	        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));        

	        System.out.println("Util.decrypt(texto): fim");
	        return new String(original);
	    }   
	 
	 
	 public static String decrypt(String encrypted, Funcionario funcionario) throws Exception {
	    	System.out.println("Util.decrypt(texto, funcionario): início");
	    	byte[] key = funcionario.getChave();    	
	    	    	    	    	
	        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

	        Cipher cipher = Cipher.getInstance("AES");                
	    	cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    	 
	        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));        

	        System.out.println("Util.decrypt(texto, funcionario): fim");
	        return new String(original);
	    }
	
	public static String gerarChave() throws Exception {
		SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();	
		
		return java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded()).substring(0, 16);
	}
	
	public static String encrypt(String texto) throws Exception {
		System.out.println("Util.encrypt(texto): início");
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");        
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(texto.getBytes());        

        System.out.println("Util.encrypt(texto): fim");
        return Base64.encodeBase64String(encrypted);
    }

}
