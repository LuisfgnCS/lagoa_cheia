package negócios;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import exceptions.CapacidadeMaximaException;

public class CaminhaoLixo extends Carro{
	private int nFuncionarios; 
	private double capacidade; 
	private double lixoArmazenado = 0;
	private double lixoComprimido = 0;
	private int compressoes = 3;
	private int tempoGastoPercorrendoCaminho = 0;
	private int tempoGastoColetandoLixo = 0;
	private final CountDownLatch latch;
	
	public CaminhaoLixo( int nFuncionarios, double capacidade, Bairro mapa,CountDownLatch latch) {
		super(0, mapa);
		this.nFuncionarios = nFuncionarios; 
		this.setCapacidade(capacidade);   
		this.latch = latch;
	}

	public int coletar() throws InterruptedException, CapacidadeMaximaException{
		int tempo = 0;
		int lixo;
		if(PontoAtual != 0) {
			PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
			do{
				lixo = pontodecoleta.getvLixo();
				System.out.println("Lixo no local: " + lixo);
				System.out.println("Capacidade restante do caminhão: " + String.format("%.2f", capacidade - lixoArmazenado));
				if(capacidade - lixoArmazenado - lixo > 0){
					tempo = lixo / this.nFuncionarios;
					if(lixoRasgado(pontodecoleta, PontoAtual)) {
						tempo = tempo * 2;
						System.out.println("Animais encontrados no local. O lixo foi rasgado!");
					}
					Thread.sleep(tempo * 1000);
					lixoArmazenado += lixo;
					pontodecoleta.setvLixo(0);
					System.out.println("Lixo recolhido: " + lixo);
				}else {
					double lixoRecolhido = (capacidade - lixoArmazenado);
					tempo = (int) (lixoRecolhido / nFuncionarios);
					if(lixoRasgado(pontodecoleta, PontoAtual)) {
						tempo = tempo * 2;
						System.out.println("Animais encontrados no local. O lixo foi rasgado!");
					}
					Thread.sleep(tempo * 1000);
					lixoArmazenado += lixoRecolhido;
					pontodecoleta.setvLixo((int) (lixo - lixoRecolhido));
					System.out.println("Lixo recolhido: " + String.format("%.2f", lixoRecolhido));
					try {
						comprimir();
						compressoes--;
						System.out.println("O volume do caminhão foi comprimido para " + String.format("%.2f", lixoArmazenado));
					} catch (Exception e) {
						retornar();
						throw new CapacidadeMaximaException();
					}
				}
				tempoGastoColetandoLixo += tempo;
			}while(lixo > 0);
		}
		mapa.situacaoPonto(PontoAtual);
		return tempo;
	}
	
	private boolean lixoRasgado(PontoDeColeta pColeta, int ponto) {
		if(pColeta.getnCachorros()  + pColeta.getnGatos() + pColeta.getnRatos() > 0) {
			try {
				Carrocinha.chamarControle(mapa, ponto);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (pColeta.getnCachorros() > 0 ? 1 : 0) + (pColeta.getnGatos() > 0 ? 1 : 0) + (pColeta.getnRatos() > 0 ? 1 : 0) == 1;
	}
	
	public void comprimir() throws Exception {
		if(compressoes > 0) {
			lixoComprimido += (lixoArmazenado - lixoComprimido) / 3;
			lixoArmazenado = lixoComprimido;
		}
		else {
			throw new Exception();
		}
	}
	
	public void chamarControle(Bairro grafo) throws InterruptedException {
		CentroDeZoonoses cz = (CentroDeZoonoses)grafo.getVertices().get(grafo.getVertices().size() -1);
		cz.mandarCarrocinha(grafo,PontoAtual);
	}
	
	@Override
	public void locomover(Bairro grafo, int destino, List<Integer> percurso) throws InterruptedException, CapacidadeMaximaException {
		Ponto vertice = mapa.getVertices().get(destino);
		vertice.emRota = true;
		int a = 0, b = 1;
		if(percurso.size() > 1) {
			while(b != destino) {
				avancar(grafo, a, b, percurso);
				a += 1;
				b += 1;
				if(capacidade > 0) {
					coletar();
				}
				if(PontoAtual != 0) {
					PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
					if(pontodecoleta.getnCachorros() + pontodecoleta.getnGatos() > 0) {
						chamarControle(mapa);
					}
				}
			} 
			mapa.getFolhasMod().remove(destino);
		}
	}
	
	public double getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(double capacidade) {
		this.capacidade = capacidade;
	}
	
	@Override
	public void run() {
		while(!mapa.getFolhasMod().isEmpty()) {
			int folha = menor(mapa.getFolhasMod()); // procura a menor folha que ainda não foi visitada ou está em rota
			int iFolha = mapa.getFolhasMod().indexOf(folha);
			List<Integer> percurso = seguirRamo(folha).reversed(); // Traça o percurso pelo ramo até a folha
			PontoDeColeta destino = (PontoDeColeta) mapa.getVertices().get(folha);
			destino.emRota = true;
			while(destino.getvLixo() > 0) {
				try {
					percorrer(percurso, 1);
				} catch (InterruptedException | CapacidadeMaximaException e) {
					System.out.println("O caminhão voltou para base pois a capacidade máxima foi atingida");
				}
				System.out.println("o lixo na folha ainda é de: " + destino.getvLixo());
			}
			System.out.println("número da folha: " + folha);
			System.out.println("Indice da folha: " + iFolha);
			System.out.println("Ramo completo.");
			System.out.println("Vetor de folhas: " + mapa.getFolhasMod().toString());
			mapa.getFolhasMod().remove(mapa.getFolhasMod().indexOf(folha));
			System.out.println("Vetor de folhas: " + mapa.getFolhasMod().toString());
			while(lixoArmazenado != capacidade && !mapa.getFolhasMod().isEmpty()) {
				folha = menor(mapa.getFolhasMod());
				iFolha = mapa.getFolhasMod().indexOf(folha);
				if(folha != -1) {
					percurso = mapa.getPercursos()[PontoAtual][folha];
					try {
						percorrer(percurso, 0);
					} catch (InterruptedException | CapacidadeMaximaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					percurso = seguirRamo(folha);
					try {
						percurso.remove(percurso.size()-1);
					}catch (Exception e) {
						// TODO: handle exception
					}
					try {
						percorrer(percurso, 1);
					} catch (InterruptedException | CapacidadeMaximaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mapa.getFolhasMod().remove(iFolha);
				}
			}
		}
		System.out.println("Tempo para recolher todo lixo: " + tempoGastoColetandoLixo + " minutos");
		System.out.println("Tempo para percorrer caminho: " + tempoGastoPercorrendoCaminho + " minutos");
		latch.countDown();	}
	
	
	
	public void retornar() throws InterruptedException, CapacidadeMaximaException {
		percorrer(mapa.getPercursos()[PontoAtual][0], 0);
		lixoArmazenado = 0;
		compressoes = 3;
	}
	
	public List<Integer> seguirRamo(int folha) {
		int[] pais = mapa.getMst().pais;
		LinkedList<Integer> caminho = new LinkedList<>();
		Integer aux = folha;
		while(aux != -1) {
			caminho.add(aux);
			aux = pais[aux];
		}
		return caminho;
	}
	
	
	public void percorrer(List<Integer> percurso, int tipo) throws InterruptedException, CapacidadeMaximaException {
		int destino = percurso.size() - 1;
		System.out.println("Rota do caminhão: " + percurso.toString());
		int a, b;
		for(int i = 1; i <= destino; i++) {
			PontoAtual = -1;
			a = percurso.get(i - 1);
			b = percurso.get(i);
			System.out.println("Saindo do ponto " + a + " Para o ponto " + b);
			int tempoAB = mapa.getW()[a][b];
			tempoGastoPercorrendoCaminho = getTempoGastoPercorrendoCaminho() + tempoAB;
			Thread.sleep(tempoAB * 1000);
			this.PontoAtual = b;
			System.out.println("Chegou no ponto " + b);
			if(tipo != 0) {
				coletar();
				if(tipo == 2) {
					try {
						mapa.getFolhasMod().add(buscarAntecessor(PontoAtual, percurso.get(i + 1)));
					}catch (Exception e) {
						break;
					}
					System.out.println("Nova folha adicionada: " + percurso.get(i + 1));
				}
				if(tipo == 3) {
					PontoDeColeta aux = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
					if(aux.getvLixo() == 0) {
						break;
					}
				}
			}
		}
		if(tipo == 3) {
			mapa.getMst().folhas.add(PontoAtual);
		}
	}

	private int buscarAntecessor(int atual, int prox) {
		int n = mapa.getW().length;
		for(int i = 0; i < n; i++) {
			int j = mapa.getMst().mst[atual][i];
			if(j != atual && j > 0) {
				return j;
			}
		}
		return -1;
	}
	
	public void setFuncionarios(int nFuncionarios) {
		this.nFuncionarios = nFuncionarios;
	}
	
	public int getFuncionarios() {
		return nFuncionarios;
	}


	public int getTempoGastoPercorrendoCaminho() {
		return tempoGastoPercorrendoCaminho;
	}

	public void setTempoGastoPercorrendoCaminho(int tempoGastoPercorrendoCaminho) {
		this.tempoGastoPercorrendoCaminho = tempoGastoPercorrendoCaminho;
	}

	public int getTempoGastoColetandoLixo() {
		return tempoGastoColetandoLixo;
	}

	public void setTempoGastoColetandoLixo(int tempoGastoColetandoLixo) {
		this.tempoGastoColetandoLixo = tempoGastoColetandoLixo;
	}


}
