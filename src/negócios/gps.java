package negócios;

import java.util.Iterator;
import java.util.List;

public abstract class gps {
	public List<Ponto>[][] camMin(Bairro grafo){
		int[][] distancia = grafo.getW();
		List<Ponto>[][] Pm = null;
		for (int i = 0; i < grafo.getVertices().size(); i++) {
			for (int j = 0; j < grafo.getVertices().size(); j++) {
				if(distancia[i][j] > 0) {
					Pm[i][j].add(grafo.getVertices().get(i));
					Pm[i][j].add(grafo.getVertices().get(j));
				}
			}
		}
		return null; // REMOVER -----------------------------------
	}
}
