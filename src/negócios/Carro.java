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
