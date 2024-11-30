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
		
	}

}
