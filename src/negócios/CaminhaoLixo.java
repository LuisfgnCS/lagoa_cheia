package negócios;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CaminhaoLixo extends Carro{
	private int nFuncionarios; 
	private double capacidade; 
	private int compressoes;
	private Bairro mapa;
	
	public CaminhaoLixo( int nFuncionarios, double capacidade) {
		super(0);
		this.nFuncionarios = nFuncionarios; 
		this.setCapacidade(capacidade);     
	}

	public int coletar() throws InterruptedException{
		PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
		int lixo = pontodecoleta.getvLixo();
//		if(capacidade - lixo < 0)
		int tempo = lixo / this.nFuncionarios;
		if(lixoRasgado(pontodecoleta)) {
			tempo = tempo * 2;
		}
		
		if(pontodecoleta.getnCachorros() + pontodecoleta.getnGatos() > 0) {
			chamarControle(mapa);
		}
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
	
	public void chamarControle(Bairro grafo) throws InterruptedException {
		CentroDeZoonoses cz = (CentroDeZoonoses)grafo.getVertices().get(grafo.getVertices().size() -1);
		cz.mandarCarrocinha(grafo,PontoAtual);
	}

	@Override
	public void locomover(Bairro grafo, int destino, List<Integer> percurso) throws InterruptedException {
		int a = 0, b = 1;
		if(percurso != null) {
			do {
				avancar(grafo, a, b, percurso);
				coletar();
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
		int folha = Collections.min(mapa.getMst().folhas, Comparator.comparing(p -> mapa.getDistancias()[PontoAtual][p])); // procura a folha mais próxima
		List<Integer> percurso = seguirRamo(folha); // Traça o percurso pelo ramo até a folha
		try {
			locomover(this.mapa, percurso.get(percurso.size() - 1), percurso); // tenta se locomover até ele
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
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
}
