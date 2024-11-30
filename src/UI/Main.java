package UI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.CapacidadeMaximaException;
import negócios.Bairro;
import negócios.CaminhaoLixo;
import negócios.PontoDeColeta;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		
		Bairro grafo = Setup.setup();
		PontoDeColeta vertice = (PontoDeColeta) grafo.getVertices().get(1);
		vertice.setnCachorros(0);
		vertice.setnGatos(1);
		vertice.setnRatos(3);
		
		
		for (int j = 0; j < 5; j++) {
			System.out.println(grafo.getVertices().get(j).toString());
		}
		
//
//		Thread.sleep(5000);
//		grafo.situacaoPonto(1);
//		System.out.println();
//		
//		for (int j = 0; j < 5; j++) {
//			System.out.println(grafo.getVertices().get(j).toString());
//		}
		
		CaminhaoLixo caminhaoLixo = new CaminhaoLixo(3, 10, grafo);
		caminhaoLixo.start();
		Thread.sleep(5000);
		CaminhaoLixo caminhaoLixo2 = new CaminhaoLixo(3, 10, grafo);
		caminhaoLixo2.start();
		
		
		
	}

}
