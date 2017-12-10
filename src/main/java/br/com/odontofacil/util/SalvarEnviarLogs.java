package br.com.odontofacil.util;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.odontofacil.model.Email;
import br.com.odontofacil.ws.controller.SendEmailController;
import br.com.odontofacil.ws.controller.SendLogController;

public class SalvarEnviarLogs {
	
	    private static File erros;
	    public static String password = "odontoFacil2017";

	    public static void gravarArquivo(Exception e) {
	        try {
	            Date data = new Date();
	            //Coloque o caminho que voce quiser, de preferencia dentro do projeto
	            String caminho = "C:\\Users\\phsil\\Desktop\\logsOdontoFacil";
	            erros = new File(caminho);
	            erros.mkdir();

	            File destino = new File(erros, "erros.log");            

	            StringWriter sw = new StringWriter();
	            PrintWriter pw = new PrintWriter(sw);
	            e.printStackTrace(pw);
	            sw.toString();

	            // este true deixa adicionar novas palavras e linhas
	            FileWriter fw = new FileWriter(destino, true);
	            BufferedWriter bw = new BufferedWriter(fw);

	            bw.write("["+new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data)+"]");
	            bw.newLine();
	            bw.write("    "+sw);
	            bw.newLine();
	            //bw.flush();
	            bw.close();
	            
	            Email email = new Email();
				// Pra quem vai mandar
				email.setTo("tccodontofacil@gmail.com");
				// Quem ta mandando
				email.setFrom("tccodontofacil@gmail.com");
				// senha
				email.setPass(password);
				email.setTexto("");
				email.setEmailFormatado("<html>"
						+ "<body>"
						+ "<div style=\"text-align: center;\">"
						+ "<span style=\"font-size:16px;\">Olá, Administrador, </span></h2></br>"
						+ "<span style=\"font-size:16px;\">Este &eacute; um e-mail autom&aacute;tico "
								+ "para informar que o sistema Odontofácil registrou um erro em seu log"
								+ " no dia "+ new Date()+" </span></strong></p>");

				SendLogController sendMail = new SendLogController(email);

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
}
