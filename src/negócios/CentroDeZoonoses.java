package negócios;

import java.util.ArrayList;
import java.util.List;

public class CentroDeZoonoses extends Ponto{
	List<Carrocinha> carrocinhas;
	
	
	public CentroDeZoonoses(Bairro grafo) {
		for(int i = 0; i<= 2;i ++) {
			carrocinhas.add(new Carrocinha(grafo.getVertices().size() - 1));
		}
	}


	public void mandarCarrocinha(Bairro grafo,int destino) throws InterruptedException {
		Carrocinha carrocinha = procurarCarrocinhaDisponivel();
		List<Integer> percurso = new ArrayList<>(grafo.Percursos[carrocinha.PontoAtual][destino]);
		carrocinha.locomover(grafo, destino,percurso);
	}
	
	public Carrocinha procurarCarrocinhaDisponivel() {
		Carrocinha carrocinhaLivre = null;
		for(Carrocinha carrocinha : carrocinhas) {
			if(carrocinha.getOcupada() != true) {
				carrocinhaLivre = carrocinha;
				return carrocinhaLivre;
			}
		}
		return carrocinhaLivre;
	}
}
