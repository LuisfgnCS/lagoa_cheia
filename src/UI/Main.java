package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.sql.PooledConnection;

import negócios.Bairro;
import negócios.CaminhaoLixo;
import negócios.Carrocinha;
import negócios.CentroDeZoonoses;
import negócios.Ponto;
import negócios.PontoDeColeta;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		Bairro grafo = Setup.setup();
		int quantidadeFuncionarios = 3;
		int quantidadeCaminhoes = 0;
		int tempoTotalGasto = 0;
		
		PontoDeColeta ponto = (PontoDeColeta) grafo.getVertices().get(1);
		ponto.setnCachorros(3);
		ponto.setnGatos(6);
		
		System.out.println();
		for (int j = 0; j < 5; j++) {
			System.out.println(grafo.getVertices().get(j).toString());
		}
		
		Carrocinha.chamarControle(grafo, 1);
		
		System.out.println();
		for (int j = 0; j < 5; j++) {
			System.out.println(grafo.getVertices().get(j).toString());
		}
		
//		List<CaminhaoLixo> frota = new ArrayList<>();
//		loopexterno: do {
//			Bairro grafoCopia = null;
//			grafoCopia = new Bairro(grafo.getNome(), grafo.getW());
//			for(Ponto ponto : grafo.getVertices()) {
//				try {
//					grafoCopia.getVertices().add((Ponto) ponto.clone());
//				} catch (CloneNotSupportedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			int[][] w = new int[grafo.getW().length][grafo.getW().length];
//			for (int i = 0; i < w.length; i++) {
//				for (int j = 0; j < w[0].length; j++) {
//					System.out.println(w[i][j] + " -> " + w[i][j]);
//					w[i][j] = grafo.getW()[i][j];
//				}
//			}
//			grafoCopia.setW(w);
//			grafoCopia.construirMST();
//			System.out.println(grafoCopia.getNome());
//			System.out.println((PontoDeColeta) grafoCopia.getVertices().get(1));
//
//			quantidadeFuncionarios = 3;
//			tempoTotalGasto = 0;
//			quantidadeCaminhoes++;
//			frota.clear();
//
//			System.out.println("Rodando com " + quantidadeCaminhoes + " caminhões");
//
//			CountDownLatch latch = new CountDownLatch(quantidadeCaminhoes);
//
//			for (int i = 0; i < quantidadeCaminhoes; i++) {
//				frota.add(new CaminhaoLixo(quantidadeFuncionarios, 20.0, grafoCopia, latch));
//			}
//
//			for (CaminhaoLixo caminhaolixo : frota) {
//				caminhaolixo.start();
//				Thread.sleep(10000);
//			}
//
//			latch.await();
//
//			for (CaminhaoLixo caminhaoLixo : frota) {
//				tempoTotalGasto += caminhaoLixo.getTempoGastoColetandoLixo()
//						+ caminhaoLixo.getTempoGastoPercorrendoCaminho();
//			}
//
//			if (tempoTotalGasto <= (2*60)) {
//				break loopexterno;
//			}
//
//			frota.stream().sorted(Comparator.comparing(CaminhaoLixo::getTempoGastoColetandoLixo).reversed());
//
//			for (CaminhaoLixo caminhaolixo : frota) {
//				for (int i = 4; i <= 5; i++) {
//					System.out.println("Testando com " + i + " Funcionários");
//					tempoTotalGasto -= caminhaolixo.getTempoGastoColetandoLixo();
//					System.out.println("tempo total anterior para coletar lixo " + caminhaolixo.getTempoGastoColetandoLixo());
//					tempoTotalGasto += ((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);
//					caminhaolixo.setTempoGastoColetandoLixo((caminhaolixo.getTempoGastoColetandoLixo() * (i - 1)) / i);
//					System.out.println();
//					System.out.printf("Novo tempo total ára coletar lixo %d", caminhaolixo.getTempoGastoColetandoLixo());
//					System.out.println();
//
//					caminhaolixo.setFuncionarios(i);
//					if (tempoTotalGasto <= (2*60)) {
//						break loopexterno;
//					}
//				}
//			}
//
//		} while (true);
//
//		System.out.println("A quantidade de caminhões necessárias é de " + frota.size());
//		int contador = 1;
//		for (CaminhaoLixo caminhaoLixo : frota) {
//			System.out.println("A quantidade de funcionários necessária para o " + contador + "º caminhão é : "
//					+ caminhaoLixo.getFuncionarios());
//			contador++;
//		}
//
	}
}
