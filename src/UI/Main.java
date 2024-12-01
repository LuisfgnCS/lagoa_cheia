package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

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
		Scanner sc = new Scanner(System.in);
		String caminhoArquivo = "/home/ruanp/Documentos/";
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

			quantidadeFuncionarios = 3;
			tempoTotalGasto = 0;
			quantidadeCaminhoes++;
			frota.clear();
			 

			System.out.println("Rodando com " + quantidadeCaminhoes + " caminhões");

			CountDownLatch latch = new CountDownLatch(quantidadeCaminhoes);

			for (int i = 0; i < quantidadeCaminhoes; i++) {
				frota.add(new CaminhaoLixo(quantidadeFuncionarios, 20.0, grafo, latch,String.format(caminhoArquivo + "caminhão%d" , i + 1)));
			}

			for (CaminhaoLixo caminhaolixo : frota) {
					caminhaolixo.start();
			}

			latch.await();
			
			grafo.getVertices().forEach(vertice -> System.out.println(vertice));


			frota.stream().sorted(Comparator.comparingInt(CaminhaoLixo::totalTempo).reversed());

			tempoTotalGasto = frota.getFirst().getTempoGastoColetandoLixo() + frota.getFirst().getTempoGastoPercorrendoCaminho();
			

			System.out.println("=============================================");
			System.out.println(tempoTotalGasto);
			sc.next();
			
			if (tempoTotalGasto <= (120)) {
				break loopexterno;
			}


			for (CaminhaoLixo caminhaolixo : frota) {
				for (int i = 4; i <= 5; i++) {
					caminhaolixo.getRelatorioIndividual().add("Adicionando mais funcionários!");
					System.out.println("Testando com " + i + " Funcionários");
					caminhaolixo.getRelatorioIndividual().add("Testando com " + i + "funcionarios!");
					tempoTotalGasto -= caminhaolixo.getTempoGastoColetandoLixo();
					System.out.println("tempo total anterior para coletar lixo " + caminhaolixo.getTempoGastoColetandoLixo());
					caminhaolixo.getRelatorioIndividual().add("Tempo total anterior para coletar lixo: " + caminhaolixo.getTempoGastoColetandoLixo());
					tempoTotalGasto += ((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);
					caminhaolixo.setTempoGastoColetandoLixo((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);
					System.out.println();
					System.out.printf("Novo tempo total ára coletar lixo %d", caminhaolixo.getTempoGastoColetandoLixo());
					caminhaolixo.getRelatorioIndividual().add("Novo tempo total para coletar lixo:" + caminhaolixo.getTempoGastoColetandoLixo());
					caminhaolixo.getRelatorioIndividual().add("Novo tempo geral: " + caminhaolixo.totalTempo());
					System.out.println();
					caminhaolixo.setFuncionarios(i);
					caminhaolixo.construirRelatorio();
					if (tempoTotalGasto <= (120)) {
						break loopexterno;
					}
				}
			}

		} while (true);

		System.out.println("A quantidade de caminhões necessárias é de " + frota.size());
		int contador = 1;
		for (CaminhaoLixo caminhaoLixo : frota) {
			System.out.println("A quantidade de funcionários necessária para o " + contador + "º caminhão é : "
					+ caminhaoLixo.getFuncionarios());
			contador++;
		}

		
	}

}
