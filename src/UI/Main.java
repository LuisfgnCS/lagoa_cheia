package UI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import negócios.Bairro;
import negócios.CaminhaoLixo;

public class Main {

	public static void main(String[] args) {
		try {
			Bairro grafo = Setup.setup();
			int quantidadeFuncionarios = 3;
			int quantidadeCaminhoes = 1;
			
			List<CaminhaoLixo> frota = new ArrayList<>();
			int tempoTrabalho = 0;
			frota.add(new CaminhaoLixo(quantidadeFuncionarios, 10.0, grafo));	
			
			do {
	
			}while(tempoTrabalho > 8);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
