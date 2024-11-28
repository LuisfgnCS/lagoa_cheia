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
		String caminho = "/home/ruanp/Documentos/grafo.txt";
		int contador = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
			String linha;
			Ponto ponto = null;

			linha = br.readLine();
			int[][] w = new int[Integer.parseInt(linha)][Integer.parseInt(linha)];
			Bairro grafo = new Bairro("Lagoa Cheia",w);

			System.out.println(linha);
			
			for (int i = 0; i < Integer.parseInt(linha); i++) {
				if (i == 0) {
					ponto = new Aterro(grafo);
				} else if (i == Integer.parseInt(linha) - 1) {
					ponto = new CentroDeZoonoses(grafo);
				} else {
					ponto = new PontoDeColeta();
				}
				grafo.getVertices().add(i, ponto);
			}
			
			while ((linha = br.readLine()) != null) {
					
				String[] linhaMatriz = linha.split(" ");

				for (int i = 0; i < linhaMatriz.length; i++) {
					w[contador][i] = Integer.parseInt(linhaMatriz[i]);
				}
				contador++;
			}
			grafo.setW(w);
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}
