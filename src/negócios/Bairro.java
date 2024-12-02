package negócios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import negócios.Gps.Result;

public class Bairro {
	private String nome;
	private List<Ponto> vertices = new ArrayList<>();
	private int[][] w; // matriz de pesos
	public List<Integer>[][] percursos;
	private int[][] distancias;
	private Result mst;
	private List<Integer> folhasMod;
	
	
	public Bairro(String nome, int[][] w) {
		super();
		this.nome = nome;
		this.w = w;
	}
	
	public void avancarAnimais(int a, int b, int animal) { // Manda os animais do tipo selecionado de um ponto A para outro ponto B
		PontoDeColeta A = (PontoDeColeta)  vertices.get(a);
		PontoDeColeta B = (PontoDeColeta)  vertices.get(b);
		TrocaAnimal trocaAnimal = new TrocaAnimal(A, B, animal);
		trocaAnimal.start();
	    try {
	        trocaAnimal.join(); // Espera a thread terminar
	    } catch (InterruptedException e) {
	        System.out.println("A execução da thread foi interrompida.");
	        Thread.currentThread().interrupt(); // Restaura o estado de interrupção
	    }

//	    situacaoPonto(b);
	}


	public String getNome() {
		return nome;
	}
	public Result getMst() {
		return mst;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Ponto> getVertices() {
		return vertices;
	}
	public void setVertices(List<Ponto> vertices) {
		this.vertices = vertices;
	}
	public int[][] getW() {
		return w;
	}
	public void setW(int[][] w) {
		this.w = w;
	}
	
	public void construirMST() {
	    this.distancias = new int[w.length][w[0].length];
	    for (int i = 0; i < w.length; i++) {
	        for (int j = 0; j < w[0].length; j++) {
	        	System.out.println(w[i][j] + " -> " + distancias[i][j]);
	            distancias[i][j] = w[i][j];
	            
	        }
	    }		
	    this.mst = Gps.calcularMSTeFolhas(w); // Gera a MST
		percursos = Gps.camMin(this); // Calcula os caminhos mínimos
		setFolhasMod(new ArrayList<Integer>(mst.folhas)); // Cria um vetor modificável de folhas da árvore
	}

	public int getDistanciasValor(int i, int j) {
		return distancias[i][j];
	}
	
	public void setDistanciaValor(int i, int j,int valor) {
		distancias[i][j] = valor;
	}
	
	public int[][] getDistancias() {
		return distancias;
	}

	public List<Integer>[][] getPercursos() {
		return percursos;
	}

	public List<Integer> getFolhasMod() {
		return folhasMod;
	}

	public void setFolhasMod(List<Integer> folhasMod) {
		this.folhasMod = folhasMod;
	}

	private class TrocaAnimal extends Thread{
		private int gatos, cachorros, ratos, animal;
		// Animal 0 = Cachorro
		// Animal 1 = Gato
		// Animal 2 = Rato
		
		private PontoDeColeta a, b;

		public TrocaAnimal(PontoDeColeta a, PontoDeColeta b, int animal) {
			super();
			this.animal = animal;
			this.a = a;
			this.cachorros = a.getnCachorros();
			this.gatos = a.getnGatos();
			this.ratos = a.getnRatos();
			this.b = b;
		}
		
		@Override
		public void run() {
			if(animal == 0) { // Move os cachorros do local
				a.setnCachorros(0);
				b.setnCachorros(b.getnCachorros() + cachorros);
				System.out.println("Cachorro saindo de " + vertices.indexOf(a) + " para " + vertices.indexOf(b)); 
			}
			else if(animal == 1) { // Move os gatos do local
				a.setnGatos(0);
				b.setnGatos(b.getnGatos() + gatos);
				System.out.println("Gato saindo de " + vertices.indexOf(a) + " para " + vertices.indexOf(b));
			}
			else if(animal == 2) { // Move os ratos do local
				a.setnRatos(0);
				b.setnRatos(b.getnRatos() + ratos);
				System.out.println("Rato saindo de " + vertices.indexOf(a) + " para " + vertices.indexOf(b));
			}
		}
	}
	
	public int verificarCachorro(int a) { // Método para verificar melhor vizinho para o cachorro se locomover 
		int ponto = 0;
		for (int i = 1; i < w.length - 1; i++) {
			if(w[a][i] > 0) {
				ponto = i;
				PontoDeColeta vizinho = (PontoDeColeta) vertices.get(i);
				if(vizinho.getnGatos() > 0 && vizinho.getvLixo() > 0) {
					break; // Se o vizinho tiver gatos e lixo, Se move pra ele imediatamente
				}
			}
		}
		return ponto;
	}
	
	public int verificarGatos(int a) { // Método para verificar o melhor vizinho para o gato se se locomover
		int ponto = 0;
		for (int i = 1; i < w.length - 1; i++) {
			if(w[a][i] > 0) {
				ponto = i;
				PontoDeColeta vizinho = (PontoDeColeta) vertices.get(i);
				if(vizinho.getnRatos() > 0 && vizinho.getvLixo() > 0) {
					break; // Se o vizinho tiver ratos e lixo, Se move pra ele imediatamente
				}
			}
		}
		return ponto;
	}
	
	public int verificarRatos(int a) { // Método para verificar o melhor vizinho para o rato se locomover
		int ponto = 0;
		for (int i = 1; i < w.length - 1; i++) {
			if(w[a][i] > 0) {
				ponto = i;
				PontoDeColeta vizinho = (PontoDeColeta) vertices.get(i);
				if(vizinho.getvLixo() > 0) {
					break; // Se o vizinho tiver lixo, se move pra ele imediatamente
				}
			}
		}
		return ponto;
	}
	
	public void situacaoPonto(int a) {
		PontoDeColeta ponto = (PontoDeColeta) vertices.get(a);
		if(ponto.getvLixo() == 0) { // Se o lixo no local acabou, move os animais dele
			avancarAnimais(a, verificarCachorro(a), 0);
			avancarAnimais(a, verificarGatos(a), 1);
			avancarAnimais(a, verificarRatos(a), 2);
		}
		else { // Caso não:
			if(ponto.getnGatos() > 0 && ponto.getnRatos() > 0) { // Se tiver gatos no local
				ponto.setnRatos(Math.max(ponto.getnRatos() - ponto.getnGatos(), 0)); // Os gatos matam um rato cada
				if(ponto.getnRatos() > 0) { // Se sobrarem ratos...
					avancarAnimais(a, verificarRatos(a), 2); // Manda os ratos para o melhor vizinho
				}
			}
			if(ponto.getnCachorros() > 0 && ponto.getnGatos() > 0) { // se tiver cachorros no local
				avancarAnimais(a, verificarGatos(a), 1); // Manda os gatos para o melhor vizinho
			}

		}
	}

}
