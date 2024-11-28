package negócios;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import exceptions.CapacidadeMaximaException;

public class CaminhaoLixo extends Carro{
	private int nFuncionarios; 
	private double capacidade; 
	private double lixoArmazenado = 0;
	private int compressoes = 3;
	
	public CaminhaoLixo( int nFuncionarios, double capacidade, Bairro mapa) {
		super(0, mapa);
		this.nFuncionarios = nFuncionarios; 
		this.setCapacidade(capacidade);   
	}

	public int coletar() throws InterruptedException, CapacidadeMaximaException{
		PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
		int lixo = pontodecoleta.getvLixo();
		int tempo = 0;
		while(lixo > 0){
			if(capacidade - lixoArmazenado - lixo > 0){
				tempo = lixo / this.nFuncionarios;
				if(lixoRasgado(pontodecoleta)) {
					tempo = tempo * 2;
				}
			}else {
				tempo = (int) ((capacidade - lixoArmazenado) / nFuncionarios);
				if(lixoRasgado(pontodecoleta)) {
					tempo = tempo * 2;
				}
				try {
					comprimir();
					compressoes--;
				} catch (Exception e) {
					retornar();
					throw new CapacidadeMaximaException();
				}
			}
		}
		Thread.sleep(tempo * 1000);
		return tempo;
	}
	
	private boolean lixoRasgado(PontoDeColeta pColeta) {
		if(pColeta.getnCachorros() > 0 && pColeta.getnGatos() == 0 && pColeta.getnRatos() == 0) {
			return true;
		}else if(pColeta.getnCachorros() == 0 && pColeta.getnGatos() > 0 && pColeta.getnRatos() == 0) {
			return true;
		}else if(pColeta.getnCachorros() == 0 && pColeta.getnGatos() == 0 && pColeta.getnRatos() > 0) {
			return true;
		}
		return false;
	}
	
	public void comprimir() {
		switch (compressoes) {
		case 3: {
			lixoArmazenado = lixoArmazenado / 3;
			break;
		}
		case 2: {
			lixoArmazenado = (lixoArmazenado * 2 / 3) / 3;
			break;
		}
		case 1: {
			lixoArmazenado = (lixoArmazenado * 4 / 9) / 3;
			break;
		}
		case 0:
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + compressoes);
		}
	}
	
	public void chamarControle(Bairro grafo) throws InterruptedException {
		CentroDeZoonoses cz = (CentroDeZoonoses)grafo.getVertices().get(grafo.getVertices().size() -1);
		cz.mandarCarrocinha(grafo,PontoAtual);
	}
	
	@Override
	public void locomover(Bairro grafo, int destino, List<Integer> percurso) throws InterruptedException, CapacidadeMaximaException {
		int a = 0, b = 1;
		PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
		if(percurso != null) {
			do {
				avancar(grafo, a, b, percurso);
				if(capacidade > 0) {
					coletar();
				}
				if(pontodecoleta.getnCachorros() + pontodecoleta.getnGatos() > 0) {
					chamarControle(mapa);
				}
			} while(b != destino);
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
		int folha = menor(mapa.getFolhasMod()); // procura a menor folha que ainda não foi visitada ou está em rota
		List<Integer> percurso = seguirRamo(folha); // Traça o percurso pelo ramo até a folha
		try {
			locomover(this.mapa, percurso.get(percurso.size() - 1), percurso);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (CapacidadeMaximaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			folha = menor(mapa.getFolhasMod());
			percurso = mapa.getPercursos()[PontoAtual][folha];
			try {
				locomover(this.mapa, percurso.get(percurso.size() - 1), percurso);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CapacidadeMaximaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			percurso = seguirRamo(folha);
			percurso.reversed();
		}
	}
	
	public void retornar() throws InterruptedException, CapacidadeMaximaException {
		locomover(mapa, 0, mapa.getPercursos()[PontoAtual][0]);
		lixoArmazenado = 0;
		compressoes = 3;
	}
	
	private List<Integer> seguirRamo(int folha) {
		int[] pais = mapa.getMst().pais;
		LinkedList<Integer> caminho = null;
		Integer aux = folha;
		while(aux != -1) {
			caminho.add(aux);
			aux = pais[aux];
		}
		return caminho;
	}
	
	public void locomover2(Bairro grafo, int destino, List<Integer> percurso) throws InterruptedException, CapacidadeMaximaException {
		int a = 0, b = 1;
		PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
		if(percurso != null) {
			do {
				avancar(grafo, a, b, percurso);
				if(capacidade > 0) {
					if(coletar() > 0) {
						for (Integer i : mapa.getMst().mst[PontoAtual]) {
							if(i > 0 && i != percurso.get(b+1)){
								PontoDeColeta vertice = (PontoDeColeta) mapa.getVertices().get(i);
								if(vertice.getvLixo() > 0) {
									mapa.getFolhasMod().add(i); // marca o vértice anterior como folha da árvore
								}
							}
						}
					}
				}
				if(pontodecoleta.getnCachorros() + pontodecoleta.getnGatos() > 0) {
					chamarControle(mapa);
				}
			} while(b != destino);
			mapa.getFolhasMod().remove(destino);
		}
	}
	
	public void locomover3(Bairro grafo, int destino, List<Integer> percurso) throws InterruptedException, CapacidadeMaximaException {
		int a = 0, b = 1;
		PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
		if(percurso != null) {
			do {
				mapa.getFolhasMod().add(b + 1);
				avancar(grafo, a, b, percurso);
				if(capacidade > 0) {
					coletar();
				}
				if(pontodecoleta.getnCachorros() + pontodecoleta.getnGatos() > 0) {
					chamarControle(mapa);
				}
			} while(b != destino);
			mapa.getFolhasMod().remove(destino);
		}
	}
}
