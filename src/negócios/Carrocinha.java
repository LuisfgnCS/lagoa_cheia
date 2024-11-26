package negócios;

import java.util.List;

public class Carrocinha extends Carro {
	
	@Override
	public void locomover(Bairro grafo, int destino, List<Ponto>[][] percursos) {
		List<Ponto> percurso = percursos[PontoAtual][destino];
		if(percurso != null) {
			
		}
	}
	
}
