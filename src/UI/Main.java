package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import negócios.Bairro;
import negócios.CaminhaoLixo;
import negócios.CentroDeZoonoses;
import negócios.Ponto;
import negócios.PontoDeColeta;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		Bairro grafo = Setup.setup();
		int quantidadeFuncionarios = 3; 		// Quantidade base de funcionários para todo caminhão novo
		int quantidadeCaminhoes = 0;			// Quantidade de caminhões que vou aumentando linearmente em + 1
		int tempoTotalGasto = 0;			// Tempo que o caminhão mais demorado levou para terminar seu percurso
		Scanner sc = new Scanner(System.in);
		String caminhoArquivo = "/home/ruanp/Documentos/"; 		// Caminho para onde os relatórios dos caminhões vão ser gerados
		List<Ponto> vertices = new ArrayList<>();			// Otimização para não ter que criar um grafo novo caso eu tenha que rodar o algoritmo mais de uma vez para mais caminhões  
		List<Integer> folhas = new ArrayList<>();			// Análogo ao de cima 
		
		for(Ponto ponto : grafo.getVertices()) {	// Aqui estou apenas clonando os pontos
			try {
				vertices.add((Ponto) ponto.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		folhas.addAll(grafo.getFolhasMod()); // Aqui estou clonando as folhas
		
		
		int[][] w = new int[grafo.getW().length][grafo.getW().length];

		
		for (int i = 0; i < w.length; i++) { // Clonando matriz de pesos
			for (int j = 0; j < w[0].length; j++) {
				System.out.println(w[i][j] + " -> " + w[i][j]);
				w[i][j] = grafo.getW()[i][j];
			}
		}
		
		
		List<CaminhaoLixo> frota = new ArrayList<>();
		loopexterno: do {			// loop principal
			grafo.getVertices().clear();	//Limpando vetor para ter certeza que não ficou restígios de interações passadas 
			for(Ponto ponto : vertices) {	//Setando os vétices 
				try {
					grafo.getVertices().add((Ponto) ponto.clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			grafo.getFolhasMod().clear(); //Limpando vetor para ter certeza que não ficou restígios de interações passadas 
			grafo.getFolhasMod().addAll(folhas); // Setando as folhas 
			
			grafo.setW(w);
			System.out.println(grafo.getNome());
			System.out.println((PontoDeColeta) grafo.getVertices().get(1));

			quantidadeFuncionarios = 3;
			tempoTotalGasto = 0;
			quantidadeCaminhoes++; // Incremetando em um a quantidade de camnihões que vão ser utlizados
			frota.clear(); //Limpando vetor para ter certeza que não ficou restígios de interações passadas 
			 

			System.out.println("Rodando com " + quantidadeCaminhoes + " caminhões");

			CountDownLatch latch = new CountDownLatch(quantidadeCaminhoes); // Gerenciador de threads

			for (int i = 0; i < quantidadeCaminhoes; i++) {
				frota.add(new CaminhaoLixo(quantidadeFuncionarios, 40.0, grafo, latch,String.format(caminhoArquivo + "caminhão%d" , i + 1))); // Adicionando a quantidade de caminhões  na frota. 1 -> 2 -> 3
			}

			for (CaminhaoLixo caminhaolixo : frota) { // Iniciando os percursos dos caminhões das frotas. Inicialmente 1.
					caminhaolixo.start();
					Thread.sleep(800);
			}
			
			latch.await();	//Esperando os caminhões terminarem suas rotas
			
			CentroDeZoonoses.interromperCarrocinhas(grafo); // Quanto todos os caminhões terminarem as rotas, encerrar atividades de carrocinhas

			tempoTotalGasto = Collections.max(frota, Comparator.comparingDouble(p -> p.totalTempo())).totalTempo(); // O tempo total gasto vai ser o tempo do caminhão que mais demorou para fazer seu percurso
			

			System.out.println("=============================================");
			System.out.println(tempoTotalGasto);
			
			if (tempoTotalGasto <= (60*8)) { // checa se com o mínimo de funcionários a condição já não é atendida, se for sai do loop
				for(CaminhaoLixo caminhaolixo: frota) {
					caminhaolixo.construirRelatorio();
				}
				break loopexterno;
			}
			
			for (CaminhaoLixo caminhaolixo : frota) {
				for (int i = 4; i <= 5; i++) { // Adiciona gradualmente funcionários no caminhão que levou maior tempo, depois no segundo que levou maior tempo e assim por diante
					caminhaolixo.getRelatorioIndividual().add("Adicionando mais funcionários!");
					System.out.println("Testando com " + i + " Funcionários");
					caminhaolixo.getRelatorioIndividual().add("Testando com " + i + " funcionarios!");
					tempoTotalGasto -= caminhaolixo.getTempoGastoColetandoLixo();// Lógica para diminuir o tempo de coleta adicionando mais funcionários
					System.out.println("tempo total anterior para coletar lixo " + caminhaolixo.getTempoGastoColetandoLixo());
					caminhaolixo.getRelatorioIndividual().add("Tempo total anterior para coletar lixo: " + caminhaolixo.getTempoGastoColetandoLixo()); // Gerando relatório
					tempoTotalGasto += ((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);// Lógica para diminuir o tempo de coleta adicionando mais funcionários
					caminhaolixo.setTempoGastoColetandoLixo((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);// Lógica para diminuir o tempo de coleta adicionando mais funcionários
					System.out.println();
					System.out.printf("Novo tempo total ára coletar lixo %d", caminhaolixo.getTempoGastoColetandoLixo()); // Gerando relatório
					caminhaolixo.getRelatorioIndividual().add("Novo tempo total para coletar lixo:" + caminhaolixo.getTempoGastoColetandoLixo());// Gerando relatório
					caminhaolixo.getRelatorioIndividual().add("Novo tempo geral: " + caminhaolixo.totalTempo());// Gerando relatório
					System.out.println();
					caminhaolixo.setFuncionarios(i);
					caminhaolixo.construirRelatorio();
					if (tempoTotalGasto <= (60*8)) {
						break loopexterno;
					}
				}
			}
		} while (true);

		System.out.println("A quantidade de caminhões necessárias é de " + frota.size()); // Mostrando as quantidades mínimas calculadas
		int contador = 1;
		for (CaminhaoLixo caminhaoLixo : frota) {
			System.out.println("A quantidade de funcionários necessária para o " + contador + "º caminhão é : "
					+ caminhaoLixo.getFuncionarios());
			caminhaoLixo.interrupt();
			contador++;
		}
		System.exit(0);
	}

}
