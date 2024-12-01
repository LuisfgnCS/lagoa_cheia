package negócios;

import java.util.ArrayList;
import java.util.Iterator;
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
		if(carrocinha != null) {
			carrocinha.destino = destino;
			List<Integer> percurso = new ArrayList<>(grafo.getPercursos()[carrocinha.PontoAtual][destino]);
			try {
				carrocinha.start();
			} catch (Exception e) {
				synchronized (carrocinha.lock) {
		            carrocinha.ocupada = true;
		            carrocinha.lock.notify(); // Notifica a Thread para executar
		        }
			}
		}else {
			System.out.println("Todas as carrocinhas estão ocupadas.");
		}
	}
	
	public static void interromperCarrocinhas(Bairro grafo) {
		CentroDeZoonoses CZ = (CentroDeZoonoses) grafo.getVertices().get(grafo.getVertices().size() - 1);
		for (Carrocinha carrocinha : CZ.carrocinhas) {
			try{
				carrocinha.interrupt();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
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
