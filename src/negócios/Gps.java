package negócios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class Gps {
	
	public static List<Integer>[][] camMin(Bairro grafo){
		@SuppressWarnings("unchecked")
		List<Integer>[][] Pm = new List[grafo.getVertices().size()][grafo.getVertices().size()];
		
		for (int i = 0; i < grafo.getVertices().size(); i++) {
		    for (int j = 0; j < grafo.getVertices().size(); j++) {
		        Pm[i][j] = new ArrayList<>();
		    }
		}
		
		for (int i = 0; i < grafo.getVertices().size(); i++) {
			for (int j = 0; j < grafo.getVertices().size(); j++) {
				if(grafo.getDistancias()[i][j] > 0) {
					List<Integer> par = Arrays.asList(i, j);
					Pm[i][j].addAll(par);
				}
			}
		}
		
		for (int k = 0; k < grafo.getVertices().size(); k++) {
			for (int i = 0; i < grafo.getVertices().size(); i++) {
				for (int j = 0; j < grafo.getVertices().size(); j++) {
					if(grafo.getDistancias()[i][k] != INF && grafo.getDistancias()[k][j] != INF && grafo.getDistancias()[i][j] > grafo.getDistancias()[i][k] + grafo.getDistancias()[k][j]) {
						System.out.println(grafo.getDistancias()[k][j] + " + " + grafo.getDistancias()[i][k] + " = " + grafo.getDistancias()[i][j]);
						grafo.getDistancias()[i][j] = grafo.getDistancias()[i][k] + grafo.getDistancias()[k][j];
						Pm[i][j].clear();
						Pm[i][j].addAll(Pm[i][k]);
						Pm[i][j].addAll(Pm[k][j].subList(1, Pm[k][j].size()));
					}
				}
			}
		}
//		System.out.println(grafo.getDistancias()[2][1]);
//		System.out.println(grafo.getDistancias()[1][3]);
		System.out.println(Pm[0][2].toString());
		return Pm;
		
	}
	
    public static final int INF = Integer.MAX_VALUE;

    public static Result calcularMSTeFolhas(int[][] w) {
        int n = w.length;
        int[][] grafo = new int[n][n];
        int[] chave = new int[n];
        boolean[] naMST = new boolean[n];
        int[] pai = new int[n];
        int[][] mst = new int[n][n];
        List<Integer> folhas = new ArrayList<>();
        
        for (int i = 0; i < n - 1; i++) {
        	Arrays.fill(grafo[i], 0);
		}
        Arrays.fill(chave, INF);
        Arrays.fill(pai, -1);
        
        for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1; j++) {
				grafo[i][j] = w[i][j];
			}
		}

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

        for (int v = 1; v < n; v++) {
            int u = pai[v];
            if (u != -1) {
                mst[u][v] = grafo[u][v];
                mst[v][u] = grafo[u][v];
            }
        }

        for (int i = 0; i < n; i++) {
            int conexoes = 0;
            for (int j = 0; j < n; j++) {
                if (mst[i][j] > 0) {
                    conexoes++;
                }
            }
            if (conexoes == 1) {
                folhas.add(i);
            }
        }
        return new Result(mst, folhas, pai);
    }

    private static int minChave(int[] chave, boolean[] naMST) {
        int min = INF, minIndex = -1;
        for (int v = 0; v < chave.length; v++) {
            if (!naMST[v] && chave[v] < min) {
                min = chave[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    public static class Result {
        public int[][] mst;
        public List<Integer> folhas;
        public int[] pais;

        public Result(int[][] mst, List<Integer> folhas, int[] pais) {
            this.mst = mst;
            this.folhas = folhas;
            this.pais = pais;
        }
    }

//    public static void main(String[] args) {
//
//        int[][] grafo = {
//            {0, 2, 0, 6, 0},
//            {2, 0, 3, 8, 5},
//            {0, 3, 0, 0, 7},
//            {6, 8, 0, 0, 9},
//            {0, 5, 7, 9, 0}
//        };
//
//        Result resultado = calcularMSTeFolhas(grafo);
//
//        System.out.println("Matriz de Adjacência da MST:");
//        for (int[] linha : resultado.mst) {
//            System.out.println(Arrays.toString(linha));
//        }
//
//        System.out.println("Folhas da MST:");
//        System.out.println(resultado.folhas);
//    }
    
    
//        public static void floydWarshall(int[][] graph) {
//            int n = graph.length; // Número de vértices
//            int[][] dist = new int[n][n]; // Matriz de distâncias
//
//            // Inicializa a matriz de distâncias com os valores do grafo
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < n; j++) {
//                    dist[i][j] = graph[i][j];
//                }
//            }
//
//            // Aplica o algoritmo de Floyd-Warshall
//            for (int k = 0; k < n; k++) {
//                for (int i = 0; i < n; i++) {
//                    for (int j = 0; j < n; j++) {
//                        // Atualiza a distância se o caminho pelo vértice k é mais curto
//                        if (dist[i][k] != INF && dist[k][j] != INF && dist[i][j] > dist[i][k] + dist[k][j]) {
//                            dist[i][j] = dist[i][k] + dist[k][j];
//                        }
//                    }
//                }
//            }
//
//            // Exibe a matriz final de distâncias
//            printSolution(dist);
//        }
//
//        private static void printSolution(int[][] dist) {
//            System.out.println("Matriz de distâncias mínimas entre todos os pares de vértices:");
//            for (int i = 0; i < dist.length; i++) {
//                for (int j = 0; j < dist.length; j++) {
//                    if (dist[i][j] == INF) {
//                        System.out.print("INF ");
//                    } else {
//                        System.out.print(dist[i][j] + " ");
//                    }
//                }
//                System.out.println();
//            }
//        }
    
}
