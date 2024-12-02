package negócios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Gps {

    // Método que calcula os caminhos mínimos entre todos os pares de vértices em um grafo
    public static List<Integer>[][] camMin(Bairro grafo) {
        @SuppressWarnings("unchecked")
        List<Integer>[][] Pm = new List[grafo.getVertices().size()][grafo.getVertices().size()];
        
        // Inicializa a matriz Pm com listas vazias
        for (int i = 0; i < grafo.getVertices().size(); i++) {
            for (int j = 0; j < grafo.getVertices().size(); j++) {
                Pm[i][j] = new ArrayList<>();
            }
        }
        
        // Preenche a matriz Pm com pares diretos de vértices que possuem conexão no grafo
        for (int i = 0; i < grafo.getVertices().size(); i++) {
            for (int j = 0; j < grafo.getVertices().size(); j++) {
                if (grafo.getDistancias()[i][j] > 0) {
                    List<Integer> par = Arrays.asList(i, j);
                    Pm[i][j].addAll(par);
                }
            }
        }
        
        // Algoritmo de Floyd-Warshall para encontrar o menor caminho entre todos os pares
        for (int k = 0; k < grafo.getVertices().size(); k++) {
            for (int i = 0; i < grafo.getVertices().size(); i++) {
                for (int j = 0; j < grafo.getVertices().size(); j++) {
                    if (grafo.getDistancias()[i][k] != INF && grafo.getDistancias()[k][j] != INF &&
                        grafo.getDistancias()[i][j] > grafo.getDistancias()[i][k] + grafo.getDistancias()[k][j]) {
                        // Atualiza a menor distância e o caminho correspondente
                        grafo.getDistancias()[i][j] = grafo.getDistancias()[i][k] + grafo.getDistancias()[k][j];
                        Pm[i][j].clear();
                        Pm[i][j].addAll(Pm[i][k]);
                        Pm[i][j].addAll(Pm[k][j].subList(1, Pm[k][j].size()));
                    }
                }
            }
        }

        System.out.println(Pm[0][2].toString()); // Exibe um exemplo de caminho mínimo
        return Pm;
    }

    public static final int INF = Integer.MAX_VALUE; // Valor que representa infinito

    // Método para calcular a Árvore Geradora Mínima (MST) e identificar folhas
    public static Result calcularMSTeFolhas(int[][] w) {
        int n = w.length;
        int[][] grafo = new int[n][n];
        int[] chave = new int[n]; // Menores custos para inclusão na MST
        boolean[] naMST = new boolean[n]; // Controla os vértices já incluídos na MST
        int[] pai = new int[n]; // Armazena o pai de cada vértice na MST
        int[][] mst = new int[n][n]; // Matriz da Árvore Geradora Mínima
        List<Integer> folhas = new ArrayList<>(); // Lista de folhas na MST

        Arrays.fill(chave, INF); // Inicializa os custos com infinito
        Arrays.fill(pai, -1); // Inicializa os pais com -1
        
        // Copia os pesos do grafo para evitar alterações no original
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grafo[i][j] = w[i][j];
            }
        }

        chave[0] = 0; // O primeiro vértice começa com custo 0
        
        // Algoritmo de Prim para construir a MST
        for (int count = 0; count < n; count++) {
            int u = minChave(chave, naMST); // Seleciona o vértice com menor chave
            naMST[u] = true; // Inclui o vértice na MST

            // Atualiza os custos dos vértices adjacentes
            for (int v = 0; v < n; v++) {
                if (grafo[u][v] != 0 && !naMST[v] && grafo[u][v] < chave[v]) {
                    pai[v] = u;
                    chave[v] = grafo[u][v];
                }
            }
        }

        // Preenche a matriz da MST com os valores calculados
        for (int v = 1; v < n; v++) {
            int u = pai[v];
            if (u != -1) {
                mst[u][v] = grafo[u][v];
                mst[v][u] = grafo[u][v];
            }
        }

        // Identifica as folhas na MST
        for (int i = 0; i < n; i++) {
            int conexoes = 0;
            for (int j = 0; j < n; j++) {
                if (mst[i][j] > 0) {
                    conexoes++;
                }
            }
            // Vértices com 1 ou 0 conexões são folhas
            if (conexoes <= 1) {
                if (i == mst.length - 1) {
                    folhas.add(pai[i]);
                } else if (i != 0) {
                    folhas.add(i);
                }
            }
        }
        return new Result(mst, folhas, pai); // Retorna a MST, folhas e pais
    }

    // Função auxiliar para encontrar o índice da chave mínima
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

    // Classe que encapsula o resultado da MST
    public static class Result {
        public int[][] mst; // Matriz da MST
        public List<Integer> folhas; // Lista de folhas
        public int[] pais; // Vetor de pais dos vértices

        public Result(int[][] mst, List<Integer> folhas, int[] pais) {
            this.mst = mst;
            this.folhas = folhas;
            this.pais = pais;
        }
    }
}
