package negócios;

import java.util.List;

public class CaminhaoLixo extends Carro{
	private int nFuncionarios;
	private double capacidade;
	
	public CaminhaoLixo( int nFuncionarios, double capacidade) {
		super(0);
		this.nFuncionarios = nFuncionarios;
		this.setCapacidade(capacidade);
	}

	public int coletar(Bairro grafo) throws InterruptedException {
		PontoDeColeta pontodecoleta = (PontoDeColeta) grafo.getVertices().get(PontoAtual);
		int tempo = pontodecoleta.getvLixo() / this.nFuncionarios;
		if(lixoRasgado(pontodecoleta)) {
			tempo = tempo * 2;
		}
		
		if(pontodecoleta.getnCachorros() + pontodecoleta.getnGatos() > 0) {
			chamarControle(grafo);
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
			} while(b != destino);
		}
	}


	public double getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(double capacidade) {
		this.capacidade = capacidade;
	}
}
