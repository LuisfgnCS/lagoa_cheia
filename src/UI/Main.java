package UI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.CapacidadeMaximaException;
import negócios.Bairro;
import negócios.CaminhaoLixo;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		Bairro grafo = Setup.setup();
		
		CaminhaoLixo caminhaoLixo = new CaminhaoLixo(3, 10, grafo);
		caminhaoLixo.start();
		Thread.sleep(5000);
		CaminhaoLixo caminhaoLixo2 = new CaminhaoLixo(3, 10, grafo);
		caminhaoLixo2.start();
		
//		List<Integer> percurso = caminhaoLixo.seguirRamo(3).reversed();
//		System.out.println(percurso.toString());
//		while(true) {
//			try {
//				caminhaoLixo.percorrer(percurso);
//				
//				System.out.println("tempo gasto: " + caminhaoLixo.getTempoGasto() + " minutos");
//				break;
//			} catch (CapacidadeMaximaException e) {
//				System.out.println("tudo bem.");
//			}
//		}
	}

}
