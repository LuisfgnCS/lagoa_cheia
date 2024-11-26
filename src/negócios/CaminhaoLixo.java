package negócios;

import java.util.List;

public class CaminhaoLixo extends Carro{
	private int nFuncionarios;
	private double capacidade;
	
	public CaminhaoLixo(String placa, int nFuncionarios, double capacidade) {
		super(placa, 0);
		this.nFuncionarios = nFuncionarios;
		this.setCapacidade(capacidade);
	}

	public int coletar(PontoDeColeta pColeta) {
		int tempo = pColeta.getvLixo() / this.nFuncionarios;
		if(pColeta.getnCachorros() + pColeta.getnGatos() + pColeta.getnRatos() > 0) {
			tempo = tempo * 2;
			chamarControle(pColeta);
		}
		return tempo;
	}
	
	public void chamarControle(PontoDeColeta pColeta) {
		
	}

	@Override
	public void locomover(Bairro grafo, int destino, List<Integer>[][] percursos) {
		
	}

	public double getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(double capacidade) {
		this.capacidade = capacidade;
	}
}
