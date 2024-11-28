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
		Thread.sleep(grafo.getW()[percurso.get(a)][percurso.get(b)]);
		this.PontoAtual = percurso.get(b);
		a++;
		b++;
		return grafo.getW()[percurso.get(a)][percurso.get(b)];
	}
	
	public int menor(int[] valores) {
		int menor = valores[0];
        for (int num : valores) {
            if (mapa.getDistancias()[PontoAtual][num] < mapa.getDistancias()[PontoAtual][menor]) {
            	if(!mapa.getVertices().get(num).emRota) {
            		menor = num;
            	}
            }
        }
        return menor;
	}
}
