package negócios;

import java.util.ArrayList;
import java.util.List;

import exceptions.CapacidadeMaximaException;

public class Carrocinha extends Carro {
	
	Boolean ocupada = false;
	int destino;
	int capacidade = 5;
	public Carrocinha(int pontoAtual, Bairro mapa) {
		super(pontoAtual, mapa);
	}
	
	public void setDestino(int destino) {
		this.destino = destino;
	}
	
	public boolean getOcupada() {
		return ocupada;
	}
	
	public void coletar() throws InterruptedException, CapacidadeMaximaException{
		PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
	
		if(capacidade > 0) {
			if(pontodecoleta.getnCachorros()  + pontodecoleta.getnGatos() > 0) {
				if(pontodecoleta.getnCachorros() + pontodecoleta.getnGatos() <=5) {
					capacidade -= pontodecoleta.getnCachorros() + pontodecoleta.getnGatos();
					pontodecoleta.setnCachorros(0);
					pontodecoleta.setnGatos(0);
				}else {
					capacidade = 0;
					int i = 1;
					while(i <= pontodecoleta.getnCachorros()) {
						pontodecoleta.setnCachorros(pontodecoleta.getnCachorros() - 1);
						if(i == 5) {
							break;
						}
						i++;
					}
					
					for(int j = 0; j < 5 - i;j++) {
						pontodecoleta.setnGatos(pontodecoleta.getnGatos() - 1);
					}
					}
				}
				
			}
		else {
			List<Integer> percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
			chamarControle(mapa,destino);
			this.destino = mapa.getVertices().size() - 1;
			percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
			locomover(mapa,destino,percurso);
			throw new CapacidadeMaximaException();
		}
	}
	
	public void chamarControle(Bairro grafo,int destino) throws InterruptedException {
		CentroDeZoonoses cz = (CentroDeZoonoses)grafo.getVertices().get(grafo.getVertices().size() -1);
		cz.mandarCarrocinha(grafo,destino);
	}

	
	@Override
	public void run() {
		List<Integer> percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
		try {
			locomover(mapa,destino,percurso);
			ocupada = true;
			this.destino = mapa.getVertices().size() - 1;
			percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
			locomover(mapa,destino,percurso);
			capacidade = 5;
			ocupada = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}catch(CapacidadeMaximaException e) {
			System.out.println(e.getMessage());

		}

	}
	
	
	
	@Override
	public void locomover(Bairro grafo,int destino, List<Integer> percurso) throws InterruptedException, CapacidadeMaximaException {
		int a = 0, b = 1;
		this.destino = destino;
		if(percurso != null) {
			do {
				avancar(grafo, a, b, percurso);
					coletar();
			} while(b != this.destino);
		}
	}
}
