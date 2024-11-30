package negócios;

import java.util.List;

import exceptions.CapacidadeMaximaException;

public abstract class Carro extends Thread{
	protected int PontoAtual;
	protected Bairro mapa;
	
	public Carro(int pontoAtual, Bairro mapa) {
		super();
		PontoAtual = pontoAtual;
		this.mapa = mapa;
	}
	
	public abstract void locomover(Bairro grafo, int destino, List<Integer> percursos) throws InterruptedException, CapacidadeMaximaException; 
	
	public int avancar(Bairro grafo, int a, int b, List<Integer> percurso) throws InterruptedException {
		this.PontoAtual = -1;
		System.out.println("Saindo do ponto " + a + " Para o ponto " + b);
		Thread.sleep(grafo.getW()[percurso.get(a)][percurso.get(b)] * 1000);
		this.PontoAtual = percurso.get(b);
		System.out.println("Chegou no ponto " + b);
		return grafo.getW()[percurso.get(a)][percurso.get(b)];
	}
	
	
	
	public int menor(List<Integer> folhas) {
		int menorElemento = -1;
		try {
			if(!folhas.isEmpty()) {
				int menor = Gps.INF; 
		        for (int num : folhas) {
		            if (mapa.getDistancias()[PontoAtual][num] < menor) {
		            	if(!mapa.getVertices().get(num).emRota) {
		            		menor = mapa.getDistancias()[PontoAtual][num];
		            		menorElemento = num;
		            	}
		            }
		        }
		        return menorElemento;
			}else return -1;
		}catch (Exception e) {
			return -1;
		}
	}
}
