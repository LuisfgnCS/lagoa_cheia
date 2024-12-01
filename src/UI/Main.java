package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import exceptions.CapacidadeMaximaException;
import negócios.Bairro;
import negócios.CaminhaoLixo;
import negócios.Ponto;
import negócios.PontoDeColeta;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		Bairro grafo = Setup.setup();
		int quantidadeFuncionarios = 3;
		int quantidadeCaminhoes = 0;
		int tempoTotalGasto = 0;
		List<Ponto> vertices = new ArrayList<>();
		List<Integer> folhas = new ArrayList<>();
		
		for(Ponto ponto : grafo.getVertices()) {
			try {
				vertices.add((Ponto) ponto.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		folhas.addAll(grafo.getFolhasMod());
		
		
		int[][] w = new int[grafo.getW().length][grafo.getW().length];

		
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[0].length; j++) {
				System.out.println(w[i][j] + " -> " + w[i][j]);
				w[i][j] = grafo.getW()[i][j];
			}
		}

//		while(!grafo.getFolhasMod().isEmpty()) {
//			int folha = caminhao1.menor(grafo.getFolhasMod()); // procura a menor folha que ainda não foi visitada ou está em rota
//			PontoDeColeta destino = (PontoDeColeta) grafo.getVertices().get(folha);
//			List<Integer> percurso = caminhao1.seguirRamo(folha).reversed();
//			
//			System.out.println();
//			System.out.println("Vértices do percurso: " + percurso.toString());
//			for (int j : percurso) {
//				System.out.println(grafo.getVertices().get(j).toString());
//			}
//			System.out.println(); 
//			
//			do {
//				try {
//					caminhao1.percorrer(percurso, 1);
//				} catch (InterruptedException | CapacidadeMaximaException e) {
//					// TODO Auto-generated catch block
//				}
//			}while(destino.getvLixo() != 0);
//			grafo.getFolhasMod().remove(grafo.getFolhasMod().indexOf(folha));
//			System.out.println(grafo.getFolhasMod().toString());
//			System.out.println();
//			System.out.println("Vértices do percurso: ");
//			for (int j : percurso) {
//				System.out.println(grafo.getVertices().get(j).toString());
//			}
//			System.out.println();
//		}
		

		
		List<CaminhaoLixo> frota = new ArrayList<>();
		loopexterno: do {
			grafo.getVertices().clear();
			for(Ponto ponto : vertices) {
				try {
					grafo.getVertices().add((Ponto) ponto.clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			grafo.getFolhasMod().clear();
			grafo.getFolhasMod().addAll(folhas);
			
			grafo.setW(w);
			System.out.println(grafo.getNome());
			System.out.println((PontoDeColeta) grafo.getVertices().get(1));

			quantidadeFuncionarios = 5;
			tempoTotalGasto = 0;
			quantidadeCaminhoes++;
			frota.clear();
			 

			System.out.println("Rodando com " + quantidadeCaminhoes + " caminhões");

			CountDownLatch latch = new CountDownLatch(quantidadeCaminhoes);

			for (int i = 0; i < quantidadeCaminhoes; i++) {
				frota.add(new CaminhaoLixo(quantidadeFuncionarios, 60.0, grafo, latch));
			}

			for (CaminhaoLixo caminhaolixo : frota) {
					caminhaolixo.start();
					Thread.sleep(2000);
			}

			latch.await();

			for (int j = 0; j < grafo.getW().length; j++) {
				System.out.println(grafo.getVertices().get(j).toString());
			}
			
			frota.stream().sorted(Comparator.comparing(CaminhaoLixo::getTempoGastoColetandoLixo).reversed());

			tempoTotalGasto = frota.getFirst().getTempoGastoColetandoLixo() + frota.getFirst().getTempoGastoPercorrendoCaminho();

			if (tempoTotalGasto <= (120)) {
				break loopexterno;
			}


			for (CaminhaoLixo caminhaolixo : frota) {
				for (int i = 4; i <= 5; i++) {
					System.out.println("Testando com " + i + " Funcionários");
					tempoTotalGasto -= caminhaolixo.getTempoGastoColetandoLixo();
					System.out.println("tempo total anterior para coletar lixo " + caminhaolixo.getTempoGastoColetandoLixo());
					tempoTotalGasto += ((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);
					caminhaolixo.setTempoGastoColetandoLixo((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);
					System.out.println();
					System.out.printf("Novo tempo total para coletar lixo %d", caminhaolixo.getTempoGastoColetandoLixo());
					System.out.println();

					caminhaolixo.setFuncionarios(i);
					if (tempoTotalGasto <= (120)) {
						break loopexterno;
					}
				}
			}
			System.out.println();
			System.out.println("Folhas do grafo: " + folhas.toString());
			System.out.println();
			System.out.println("Vértices do grafo: ");
			for (int j = 0; j < grafo.getVertices().size(); j++) {
				System.out.println(grafo.getVertices().get(j).toString());
			}
			System.out.println();

		} while (true);
		for (int j = 0; j < 5; j++) {
			System.out.println(grafo.getVertices().get(j).toString());
		}
		System.out.println("A quantidade de caminhões necessárias é de " + frota.size());
		int contador = 1;
		for (CaminhaoLixo caminhaoLixo : frota) {
			System.out.println("A quantidade de funcionários necessária para o " + contador + "º caminhão é : "
					+ caminhaoLixo.getFuncionarios());
			contador++;
		}

		
	}

}
