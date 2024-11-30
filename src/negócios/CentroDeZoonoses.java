package negócios;

import java.util.ArrayList;
import java.util.List;

public class CentroDeZoonoses extends Ponto{
	List<Carrocinha> carrocinhas = new ArrayList<>();
	
	public CentroDeZoonoses(Bairro grafo) {
		for(int i = 0; i<= 2;i ++) {
			carrocinhas.add(new Carrocinha(grafo.getVertices().size(), grafo));
		}
	}
	
	public void mandarCarrocinha(Bairro grafo,int destino) throws InterruptedException {
		Carrocinha carrocinha = procurarCarrocinhaDisponivel();
		carrocinha.destino = destino;
		List<Integer> percurso = new ArrayList<>(grafo.getPercursos()[carrocinha.PontoAtual][destino]);
		carrocinha.run();
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
