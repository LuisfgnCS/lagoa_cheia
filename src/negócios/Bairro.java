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
	
	public void avancarAnimais(int a, int b, int animal) {
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
	    this.mst = Gps.calcularMSTeFolhas(w);
		percursos = Gps.camMin(this);
		setFolhasMod(new ArrayList<Integer>(mst.folhas));
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
			if(animal == 0) {
				a.setnCachorros(0);
				b.setnCachorros(b.getnCachorros() + cachorros);
				System.out.println("Cachorro saindo de " + vertices.indexOf(a) + " para " + vertices.indexOf(b));
			}
			else if(animal == 1) {
				a.setnGatos(0);
				b.setnGatos(b.getnGatos() + gatos);
				System.out.println("Gato saindo de " + vertices.indexOf(a) + " para " + vertices.indexOf(b));
			}
			else if(animal == 2) {
				a.setnRatos(0);
				b.setnRatos(b.getnRatos() + ratos);
				System.out.println("Rato saindo de " + vertices.indexOf(a) + " para " + vertices.indexOf(b));
			}
		}
	}
	
	public int verificarCachorro(int a) {
		int ponto = 0;
		for (int i = 1; i < w.length - 1; i++) {
			if(w[a][i] > 0) {
				ponto = i;
				PontoDeColeta vizinho = (PontoDeColeta) vertices.get(i);
				if(vizinho.getnGatos() > 0 && vizinho.getvLixo() > 0) {
					break;
				}
			}
		}
		return ponto;
	}
	
	public int verificarGatos(int a) {
		int ponto = 0;
		for (int i = 1; i < w.length - 1; i++) {
			if(w[a][i] > 0) {
				ponto = i;
				PontoDeColeta vizinho = (PontoDeColeta) vertices.get(i);
				if(vizinho.getnRatos() > 0 && vizinho.getvLixo() > 0) {
					break;
				}
			}
		}
		return ponto;
	}
	
	public int verificarRatos(int a) {
		int ponto = 0;
		for (int i = 1; i < w.length - 1; i++) {
			if(w[a][i] > 0) {
				ponto = i;
				PontoDeColeta vizinho = (PontoDeColeta) vertices.get(i);
				if(vizinho.getvLixo() > 0) {
					break;
				}
			}
		}
		return ponto;
	}
	
	public void situacaoPonto(int a) {
		PontoDeColeta ponto = (PontoDeColeta) vertices.get(a);
		if(ponto.getvLixo() == 0) {
			avancarAnimais(a, verificarCachorro(a), 0);
			avancarAnimais(a, verificarGatos(a), 1);
			avancarAnimais(a, verificarRatos(a), 2);
		}
		else {
			if(ponto.getnGatos() > 0 && ponto.getnRatos() > 0) {
				ponto.setnRatos(Math.max(ponto.getnRatos() - ponto.getnGatos(), 0));
				if(ponto.getnRatos() > 0) {
					avancarAnimais(a, verificarRatos(a), 2);
				}
			}
			if(ponto.getnCachorros() > 0 && ponto.getnGatos() > 0) {
				avancarAnimais(a, verificarGatos(a), 1);
			}

		}
	}

}
