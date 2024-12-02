package UI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import negócios.Aterro;
import negócios.Bairro;
import negócios.CentroDeZoonoses;
import negócios.Gps;
import negócios.Ponto;
import negócios.PontoDeColeta;

public class Setup {

	@SuppressWarnings({ "null", "finally" })
	public static Bairro setup() throws FileNotFoundException, IOException {
		String caminho = "/home/ruanp/Documentos/grafo.txt"; // Caminho para onde o arquivo com o grafo está
		int contador = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(caminho))) { // lendo o arquivo
			String linha;
			Ponto ponto = null;

			linha = br.readLine(); // Lendo a primeira linha e pegando quantidade de vértices
			int[][] w = new int[Integer.parseInt(linha)][Integer.parseInt(linha)]; // Criando matriz nXn
			Bairro grafo = new Bairro("Lagoa Cheia",w); // Criando bairro

			System.out.println(linha);
			
			for (int i = 0; i < Integer.parseInt(linha); i++) { // for que vai adicionar os n vértices na lista de vértices do bairro. O primeiro vértice é o aterro e o último o centro de zoonose
				if (i == 0) {
					ponto = new Aterro(grafo);
				} else if (i == Integer.parseInt(linha) - 1) {
					ponto = new CentroDeZoonoses(grafo);
				} else {
					ponto = new PontoDeColeta();
				}
				grafo.getVertices().add(i, ponto);
			}
			
			while ((linha = br.readLine()) != null) { // Gerando matriz de pesos com a leitura do arquivo
					
				String[] linhaMatriz = linha.split(" ");

				for (int i = 0; i < linhaMatriz.length; i++) {
					w[contador][i] = Integer.parseInt(linhaMatriz[i]) == -1 ? Gps.INF : Integer.parseInt(linhaMatriz[i]); // Todo peso que é -1 vai ser transformado em infinito
				}
				contador++;
			}
			grafo.setW(w); // Setando matriz de pesos
			grafo.construirMST(); 
			System.out.println();
			System.out.println("Grafo:");
			for(int i = 0 ; i < grafo.getW().length; i++) {
				for(int j = 0; j < grafo.getW().length;j++) {
					if(grafo.getW()[i][j] != Gps.INF) {
						System.out.printf(grafo.getW()[i][j]+" ");
					}else {
						System.out.printf("* ");
					}
				}
				System.out.println();
			}
			
//			  for (int[] ws: w) {
//				  System.out.println(Arrays.toString(ws));
//			  }
			System.out.println();
			  System.out.println("Distancias:");
			  for (int[] dist : grafo.getDistancias()) {
				  System.out.println(Arrays.toString(dist));
			  }
			  
			  System.out.println();
			  System.out.println("MST:");
			  for (int[] mst : grafo.getMst().mst) {
				  System.out.println(Arrays.toString(mst));
			  }
			  
			return grafo;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;

	}
}
