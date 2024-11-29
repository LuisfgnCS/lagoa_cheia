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
		String caminho = "C:/Users/Softex/Downloads/grafo.txt";
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
					w[contador][i] = Integer.parseInt(linhaMatriz[i]) == -1 ? Gps.INF : Integer.parseInt(linhaMatriz[i]);
				}
				contador++;
			}
			grafo.setW(w);
			grafo.construirMST();
			for(int i = 0 ; i < grafo.getW().length; i++) {
				for(int j = 0; j < grafo.getW().length;j++) {
					System.out.printf(grafo.getW()[i][j]+" ");
				}
				System.out.println();
			}
			
			  for (int[] mst : grafo.getDistancias()) {
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
