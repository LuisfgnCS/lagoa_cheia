package negócios;

import java.util.List;

public abstract class Carro {
	String placa;
	int PontoAtual;
	
	public Carro(String placa, int pontoAtual) {
		super();
		this.placa = placa;
		PontoAtual = pontoAtual;
	}

	public abstract void locomover(Bairro grafo, int destino, List<Integer>[][] percursos) throws InterruptedException; 
}
