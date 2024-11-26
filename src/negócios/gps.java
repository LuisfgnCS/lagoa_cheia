package negócios;

import java.util.Arrays;
import java.util.List;

public abstract class Gps {
	public List<Ponto>[][] camMin(Bairro grafo){
		int[][] distancia = Arrays.copyOf(grafo.getW(), grafo.getW().length);
		int[][][] Pm = null;
		for (int i = 0; i < grafo.getVertices().size(); i++) {
			for (int j = 0; j < grafo.getVertices().size(); j++) {
				if(distancia[i][j] > 0) {
					Pm[i][j].add(grafo.getVertices().get(i));
					Pm[i][j].add(grafo.getVertices().get(j));
				}
			}
		}
		for (int k = 0; k < grafo.getVertices().size(); k++) {
			for (int i = 0; i < grafo.getVertices().size(); i++) {
				for (int j = 0; j < grafo.getVertices().size(); j++) {
					if(distancia[i][j] > distancia[i][k] + distancia[k][j]) {
						distancia[i][j] = distancia[i][k] + distancia[k][j];
						Pm[i][j] = null;
						Pm[i][j].addAll(Pm[i][k]);
						Pm[i][j].addAll(Pm[k][j]);
					}
				}
			}
		}
		return Pm;
	}
}
