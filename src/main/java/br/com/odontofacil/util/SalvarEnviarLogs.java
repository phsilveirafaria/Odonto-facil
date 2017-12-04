package br.com.odontofacil.util;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalvarEnviarLogs {
	
	    private static File erros;

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

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
}
