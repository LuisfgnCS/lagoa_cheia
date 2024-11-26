package negócios;

import java.util.ArrayList;
import java.util.List;

public class Carrocinha extends Carro {
	
	public Carrocinha(String placa, int pontoAtual) {
		super(placa, pontoAtual);
	}

	@Override
	public void locomover(Bairro grafo, int destino, List<Integer>[][] percursos) throws InterruptedException {
		List<Integer> percurso = new ArrayList<>(percursos[PontoAtual][destino]);
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
