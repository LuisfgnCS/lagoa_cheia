package UI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.CapacidadeMaximaException;
import negócios.Bairro;
import negócios.CaminhaoLixo;

public class Main {

	public static void main(String[] args) throws InterruptedException, CapacidadeMaximaException {
		try {
			Bairro grafo = Setup.setup();
			
			CaminhaoLixo caminhaoLixo = new CaminhaoLixo(3, 10, grafo);
			caminhaoLixo.start();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
