package negócios;

import java.util.List;

public abstract class Carro {
	String placa;
	int PontoAtual;
	public abstract void locomover(Bairro grafo, int destino, List<Ponto>[][] percursos); 
}
