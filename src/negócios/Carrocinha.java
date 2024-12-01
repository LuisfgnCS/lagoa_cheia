package negócios;

import java.util.ArrayList;
import java.util.List;

import exceptions.CapacidadeMaximaException;

public class Carrocinha extends Carro {
	public final Object lock = new Object();
	Boolean ocupada = false;
	int destino = 0;
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
		if(PontoAtual != 0 && PontoAtual != mapa.getVertices().size() -1) {
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
						int c = pontodecoleta.getnCachorros();
						while(i <= c) {
							pontodecoleta.setnCachorros(pontodecoleta.getnCachorros() - 1);
							if(i == 5) {
								break;
							}
							i++;
						}
						System.out.println("Número de cachorros recolhidos: " + i);
						
						for(int j = 0; j <= 5 - i;j++) {
							pontodecoleta.setnGatos(pontodecoleta.getnGatos() - 1);
						}
						
						System.out.println("Número de gatos recolhidos: " + (5 - i));
						}
					}
					
				}
			else {
				List<Integer> percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
				chamarControle(mapa,destino);
				this.destino = mapa.getVertices().size() - 1;
				percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
				percorrer(percurso, 0);
				capacidade = 5;
				ocupada = false;
				throw new CapacidadeMaximaException();
			}
		}
	}
	
	public static void chamarControle(Bairro grafo,int destino) throws InterruptedException {
		CentroDeZoonoses cz = (CentroDeZoonoses)grafo.getVertices().get(grafo.getVertices().size() -1);
		cz.mandarCarrocinha(grafo, destino);
	}

	public void percorrer(List<Integer> percurso, int tipo) throws InterruptedException, CapacidadeMaximaException {
		int destino = percurso.size() - 1;
		System.out.println("Rota da carrocinha: " + percurso.toString());
		int a, b;
		for(int i = 1; i <= destino; i++) {
			PontoAtual = -1;
			a = percurso.get(i - 1);
			b = percurso.get(i);
			System.out.println("Carrocinha saindo do ponto " + a + " Para o ponto " + b);
			int tempoAB = mapa.getW()[a][b];
			Thread.sleep(tempoAB * 100);
			this.PontoAtual = b;
			System.out.println("Carrocinha chegou no ponto " + b);
			if(tipo != 0) {
				coletar();
			}
		}
	}
	
	
	@Override
	public void run() {
		while(true) {
			synchronized (lock) {
                while (ocupada) {
                	try {
						List<Integer> percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
						
						try {
							percorrer(percurso, 1);
							ocupada = true;
							this.destino = mapa.getVertices().size() - 1;
							percurso = new ArrayList<>(mapa.getPercursos()[PontoAtual][this.destino]);
							percorrer(percurso, 1);
							capacidade = 5;
							ocupada = false;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							System.out.println(e.getMessage());
						}catch(CapacidadeMaximaException e) {
							System.out.println("A carrocinha voltou para base, pois estava cheia");
						
						}
                	}catch (Exception e) {
                		break;
                	}
                }	
			}
		}
	}
	
	
	
	@Override
	public void locomover(Bairro grafo,int destino, List<Integer> percurso) throws InterruptedException, CapacidadeMaximaException {
		int a = 0, b = 1;
		this.destino = destino;
		if(!percurso.isEmpty()) {
			do {
				avancar(grafo, a, b, percurso);
					coletar();
			} while(b != this.destino);
		}
	}
}