package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RedesController {
	public RedesController() {
		super();
	}
	
	private String os() {
		String os = System.getProperty("os.name");
		return os;
	}
	
	public void ip() {
		String os = os();
		String processo = "";
		
		if(os.contains("Windows")) {
			processo = "ipconfig";
		}else if(os.contains("Linux")) {
			processo = "ip addr";
		}
		
		try {
			Process p = Runtime.getRuntime().exec(processo);
			InputStream fluxo = p.getInputStream();
			InputStreamReader leitor = new InputStreamReader(fluxo);
			BufferedReader buffer = new BufferedReader(leitor);
			String linha;
			StringBuffer ipconfig = new StringBuffer();

			if(os.contains("Linux")) {
				Process p2 = Runtime.getRuntime().exec("lspci");
				InputStream fluxo2 = p2.getInputStream();
				InputStreamReader leitor2 = new InputStreamReader(fluxo2);
				buffer = new BufferedReader(leitor2);
				
				while((linha = buffer.readLine())!= null) {
					ipconfig.append(linha);
					ipconfig.append("\n");
				}
				leitor2.close();
				fluxo2.close();
				buffer = new BufferedReader(leitor);
			}
			
			while((linha = buffer.readLine()) != null) {
				ipconfig.append(linha);
				ipconfig.append("\n");
			}
			
			String[] linhas = ipconfig.toString().split("\n");
			String adaptador = " ";
			int i = 0;
			
			for(String l : linhas) {
				l = l.trim();
				
				if((l.contains("Ethernet") || l.contains("Adaptador")) && i == 0) {
					if(os.contains("Windows")) {
						adaptador = l.substring(0, l.length()-1);
					}else if(os.contains("Linux")) {
						adaptador = l.substring(29, l.length()-9);
					}
				}

				if(l.contains("IPv4") || l.contains("inet")){
					i++;
					if(os.contains("Windows") && i == 3) {
						String[] partes = l.split(":");
						String ipv4 = partes[1];
						System.out.println("IPv4: " + ipv4);
						break;
					}else if (os.contains("Linux") && i == 3) {
						String[] partes = l.split(" ");
						String inet = partes[1].trim();
						System.out.println("inet: " + inet);
						break;
					}
				}
			}
			System.out.println("Adaptador de rede: " + adaptador);
			
			buffer.close();
			leitor.close();
			fluxo.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void ping() {
		String os = os();
		String ms = "";
		String processo = "";
		
		if(os.contains("Windows")) {
			processo = "ping -4 -n 10 www.google.com.br";
			ms = "ms";
		}else if(os.contains("Linux")) {
			processo = "ping -4 -c 10 www.google.com.br";
			ms = "time";
		}
		
		try {
			Process p = Runtime.getRuntime().exec(processo);
			InputStream fluxo = p.getInputStream();
			InputStreamReader leitor = new InputStreamReader(fluxo);
			BufferedReader buffer = new BufferedReader(leitor);
			String linha = buffer.readLine();
			List<Double> tempos = new ArrayList<>();
			
			while(linha != null) {
				String[] partes = linha.split(" ");
				for(String parte : partes) {
					if(parte.endsWith(ms) || parte.startsWith(ms)) {
						String[] tempoParte = parte.split("=");
						if(tempoParte.length > 1) {
							 try {
								 tempos.add(Double.parseDouble(tempoParte[1].replace(ms, "")));
							 }catch (NumberFormatException e){
								 System.err.println("Erro ao converter o número: " + e.getMessage());
							 }
						}
					}
				}
				
				linha = buffer.readLine();
			}
			
			if(!tempos.isEmpty()) {
				double soma = 0;
				for(Double tempo : tempos) {
					soma += tempo;
				}
				int media = (int) soma / tempos.size();
				System.out.println("O ping médio foi de " + media + "ms");
			}else {
				System.out.println("Nenhum tempo de resposta foi encontrado.");
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}