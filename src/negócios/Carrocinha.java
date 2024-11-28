package negócios;

import java.util.List;

public class Carrocinha extends Carro {
	
	Boolean ocupada = false;
	
	public Carrocinha(int pontoAtual, Bairro mapa) {
		super(pontoAtual, mapa);
	}
	
	public boolean getOcupada() {
		return ocupada;
	}
	
	@Override
	public void locomover(Bairro grafo, int destino, List<Integer> percurso) throws InterruptedException {
		int a = 0, b = 1;
		if(percurso != null) {
			do {
				this.PontoAtual = -1;
				Thread.sleep(grafo.getW()[percurso.get(a)][percurso.get(b)]);
				this.PontoAtual = percurso.get(b);
				a++;
				b++;
			} while(b != destino);
		}
	}
}
