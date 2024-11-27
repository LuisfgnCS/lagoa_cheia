package UI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import negócios.Aterro;
import negócios.Bairro;
import negócios.CentroDeZoonoses;
import negócios.Ponto;
import negócios.PontoDeColeta;

public class Setup {

	@SuppressWarnings("null")
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Bairro grafo = new Bairro();
		String caminho = "/home/ruanp/Documentos/grafo.txt";
		int contador = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(caminho))){
			String linha;
			Ponto ponto = null;
			int[][] w = null;
			while((linha = br.readLine()) != null) {
				if(contador == 0) {
					for(int i = 0; i < Integer.parseInt(linha);i++ ) {
						if(i == 0) {
							ponto = new Aterro(grafo);
						}else if (i == Integer.parseInt(linha) - 1){
							ponto = new CentroDeZoonoses(grafo);
						}else {
							ponto = new PontoDeColeta();
						}
						grafo.getVertices().set(i, ponto);
						continue;
					}
				String[] linhaMatriz = linha.split(" ");
				
				for(int i = 0; i< linhaMatriz.length;i++) {
					w[contador - 1][i] = Integer.parseInt(linhaMatriz[i]);
				}
					
				}
				contador++;
			}
			for(Ponto vertice : grafo.getVertices()) {
				System.out.println(vertice);
			}
		}
		
		
	}
}
