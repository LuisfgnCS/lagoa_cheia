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
	
    private static final int INF = Integer.MAX_VALUE;

    public void prim(int[][] grafo) {
        int n = grafo.length;             
        int[] chave = new int[n];         
        boolean[] naMST = new boolean[n]; 
        int[] pai = new int[n];           

        Arrays.fill(chave, INF);          
        Arrays.fill(pai, -1);             

        chave[0] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = minChave(chave, naMST);
            naMST[u] = true; 

            for (int v = 0; v < n; v++) {
                if (grafo[u][v] != 0 && !naMST[v] && grafo[u][v] < chave[v]) {
                    pai[v] = u;
                    chave[v] = grafo[u][v];
                }
            }
        }
        imprimirMST(pai, grafo);
    }

    private int minChave(int[] chave, boolean[] naMST) {
        int min = INF, minIndex = -1;
        for (int v = 0; v < chave.length; v++) {
            if (!naMST[v] && chave[v] < min) {
                min = chave[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private void imprimirMST(int[] pai, int[][] grafo) {
        System.out.println("Aresta \tPeso");
        for (int i = 1; i < grafo.length; i++) {
            System.out.println(pai[i] + " - " + i + "\t" + grafo[i][pai[i]]);
        }
    }
}
