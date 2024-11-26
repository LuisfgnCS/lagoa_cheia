package negócios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Gps {
	public List<Integer>[][] camMin(Bairro grafo){
		int[][] distancia = Arrays.copyOf(grafo.getW(), grafo.getW().length);
		@SuppressWarnings("unchecked")
		List<Integer>[][] Pm = new List[grafo.getVertices().size()][grafo.getVertices().size()];
		
		for (int i = 0; i < grafo.getVertices().size(); i++) {
		    for (int j = 0; j < grafo.getVertices().size(); j++) {
		        Pm[i][j] = new ArrayList<>();
		    }
		}
		
		for (int i = 0; i < grafo.getVertices().size(); i++) {
			for (int j = 0; j < grafo.getVertices().size(); j++) {
				if(distancia[i][j] > 0) {
					List<Integer> par = Arrays.asList(i, j);
					Pm[i][j].addAll(par);
				}
			}
		}
		
		for (int k = 0; k < grafo.getVertices().size(); k++) {
			for (int i = 0; i < grafo.getVertices().size(); i++) {
				for (int j = 0; j < grafo.getVertices().size(); j++) {
					if(distancia[i][j] > distancia[i][k] + distancia[k][j]) {
						distancia[i][j] = distancia[i][k] + distancia[k][j];
						Pm[i][j].clear();
						Pm[i][j].addAll(Pm[i][k]);
						Pm[i][j].addAll(Pm[k][j]);
					}
				}
			}
		}
		return Pm;
	}
}
