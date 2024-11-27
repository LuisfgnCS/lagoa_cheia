package negócios;

import java.util.List;

public abstract class Carro extends Thread{
	int PontoAtual;
	
	public Carro(int pontoAtual) {
		super();
		PontoAtual = pontoAtual;
	}

	
	public abstract void locomover(Bairro grafo, int destino, List<Integer> percursos) throws InterruptedException; 
	
	public void avancar(Bairro grafo, int a, int b, List<Integer> percurso) throws InterruptedException {
		this.PontoAtual = -1;
		Thread.sleep(grafo.getW()[percurso.get(a)][percurso.get(b)]);
		this.PontoAtual = percurso.get(b);
		a++;
		b++;
	}
}
