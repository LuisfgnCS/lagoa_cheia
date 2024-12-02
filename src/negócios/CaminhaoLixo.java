package negócios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import exceptions.CapacidadeMaximaException;

public class CaminhaoLixo extends Carro{
	private int nFuncionarios; 
	private double capacidade; 
	private double lixoArmazenado = 0;
	private double lixoComprimido = 0;
	private int compressoes = 3; // quantidade de compressões restantes
	private int tempoGastoPercorrendoCaminho = 0;
	private int tempoGastoColetandoLixo = 0;
	private final CountDownLatch latch; // necessário para fazer o main esperar a thread acabar
	private List<String> relatorioIndividual = new ArrayList<>(); // relatório dos movimentos do caminhão
	private String caminhoArquivo; // Caminho onde o arquivo será criado
	
	public CaminhaoLixo( int nFuncionarios, double capacidade, Bairro mapa,CountDownLatch latch, String caminhoArquivo) {
		super(0, mapa);
		this.nFuncionarios = nFuncionarios; 
		this.setCapacidade(capacidade);   
		this.latch = latch;
		this.setCaminhoArquivo(caminhoArquivo);

	}

	public int coletar() throws InterruptedException, CapacidadeMaximaException{ // método para coletar o lixo no local
		int tempo = 0; // tempo gasto na ação
		int lixo; // quantidade de lixo do local
		if(PontoAtual != 0 && PontoAtual != mapa.getVertices().size() -1) { // caso o ponto atual não seja o aterro ou o centro de zoonoses:
			PontoDeColeta pontodecoleta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
			do{
				lixo = pontodecoleta.getvLixo();
				System.out.println("Lixo no local: " + lixo);
				System.out.println("Capacidade restante do caminhão: " + String.format("%.2f", capacidade - lixoArmazenado));
				relatorioIndividual.add("Lixo no local: " + lixo);
				relatorioIndividual.add("Capacidade restante do caminhão: " + String.format("%.2f", capacidade - lixoArmazenado));
				if(capacidade - lixoArmazenado - lixo > 0){ // Se todo o lixo do local couber no caminhão:
					tempo = lixo / this.nFuncionarios; 
					if(lixoRasgado(pontodecoleta, PontoAtual)) {
						tempo = tempo * 2; // Se o lixo no local for rasgado, duplica o tempo
						System.out.println("Animais encontrados no local. O lixo foi rasgado!");
						relatorioIndividual.add("Animais encontrados no local. O LIXO FOI RASGADO!");

					}
					Thread.sleep(tempo * 100); // Espera um tempo em centésimos de milissegundos equivalente ao tempo em minutos para coletar o lixo
					lixoArmazenado += lixo; // aumenta o lixo armazenado
					pontodecoleta.setvLixo(0); // zera o lixo no local
					System.out.println("Lixo recolhido: " + lixo);
				}else { // Caso contrário:
					double lixoRecolhido = (capacidade - lixoArmazenado); // O lixo recolhido vai ser o que o caminhão puder recolher com o armazenamento restante
					tempo = (int) (lixoRecolhido / nFuncionarios);
					if(lixoRasgado(pontodecoleta, PontoAtual)) {
						tempo = tempo * 2; // se o lixo for rasgado, duplica o tempo para coletar
						System.out.println("Animais encontrados no local. O lixo foi rasgado!");
					}
					Thread.sleep(tempo * 100); // espera um tempo em centésimos de milissegundos
					lixoArmazenado += lixoRecolhido; 
					pontodecoleta.setvLixo((int) (lixo - lixoRecolhido)); // diminui o lixo no local o lixo que foi recolhido dele
					System.out.println("Lixo recolhido: " + String.format("%.2f", lixoRecolhido)); 
					try { // tenta comprimir o lixo caso ainda consiga comprimir 
						comprimir();
						compressoes--;
						System.out.println("O volume do caminhão foi comprimido para " + String.format("%.2f", lixoArmazenado));
						relatorioIndividual.add("O volume do caminhão foi comprimido para " +String.format("%.2f", lixoArmazenado) );
						relatorioIndividual.add("O número restante de compressões é agora de " + compressoes);
					} catch (Exception e) {
						throw new CapacidadeMaximaException(); // caso não consiga, é porque o caminhão chegou na capacidade máxima e uma exceção é lançada.
					}
				}
				tempoGastoColetandoLixo += tempo; // O tempo gasto geral coletando o lixo é igual o tempo gasto geral + o tempo gasto nessa coleta em específico
			}while(lixo > 0); // Repita o processo enquanto o lixo no local for maior do que 0;
		}
		mapa.situacaoPonto(PontoAtual); // Verifica a situação atual dos animais no local
		return tempo; // retorna o tempo gasto
	}
	
	private boolean lixoRasgado(PontoDeColeta pColeta, int ponto) {
		return (pColeta.getnCachorros() > 0 ? 1 : 0) + (pColeta.getnGatos() > 0 ? 1 : 0) + (pColeta.getnRatos() > 0 ? 1 : 0) == 1; 
		// verifica se o local tem apenas um tipo de animal, se tiver, o lixo foi rasgado
	}
	
	public void comprimir() throws Exception {
		if(compressoes > 0) {
			lixoComprimido += (lixoArmazenado - lixoComprimido) / 3; 
			// O lixo comprimido é igual ao lixo comprimido já existente + lixo armazenado que não foi comprimido ainda / 3
			lixoArmazenado = lixoComprimido;
		}
		else {
			throw new Exception();
		}
	}
	
	public void chamarControle(Bairro grafo) throws InterruptedException {
		CentroDeZoonoses cz = (CentroDeZoonoses)grafo.getVertices().get(grafo.getVertices().size() -1);
		cz.mandarCarrocinha(grafo,PontoAtual); // Chama uma carrocinha para o local atual do caminhão
	}
	
	public double getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(double capacidade) {
		this.capacidade = capacidade;
	}
	
	
	public void retornar() throws InterruptedException, CapacidadeMaximaException { // retorna para o aterro sanitário
		percorrer(mapa.getPercursos()[PontoAtual][0], 0);
		lixoArmazenado = 0;
		compressoes = 3;
	}
	
	public List<Integer> seguirRamo(int folha) { // Método que traça o caminho de uma folha até o aterro sanitário.
		int[] pais = mapa.getMst().pais;
		LinkedList<Integer> caminho = new LinkedList<>();
		Integer aux = folha;
		while(aux != -1) {
			caminho.add(aux);
			aux = pais[aux];
		}
		return caminho;
	}
	
	
	public void percorrer(List<Integer> percurso, int tipo) throws InterruptedException, CapacidadeMaximaException { // Método que percorre um caminho dado
		// Tipo 0: Não coleta nenhum lixo, apenas percorre
		// Tipo 1: Coleta todo o lixo até terminar o percurso
		// Tipo 2: Descartado -----
		// Tipo 3: Coleta todo o lixo até encontrar um vértice que já foi coletado.
		
		int destino = percurso.size() - 1; // destino = último elemento do caminho
		System.out.println("Rota do caminhão: " + percurso.toString());
		
		relatorioIndividual.add("Rota do caminhão: " + percurso.toString());
		relatorioIndividual.add("        ");

		relatorioIndividual.add("Iniciando rota do caminhão");
		int a, b;
		if(tipo == 3) {
			coletar(); 
		}
		for(int i = 1; i <= destino; i++) {
			PontoAtual = -1;
			a = percurso.get(i - 1);
			b = percurso.get(i);
			System.out.println("Saindo do ponto " + a + " Para o ponto " + b);
			relatorioIndividual.add("Saindo do ponto" + a + " Para o ponto" + b);

			int tempoAB = mapa.getW()[a][b];
			tempoGastoPercorrendoCaminho = getTempoGastoPercorrendoCaminho() + tempoAB;
			Thread.sleep(tempoAB * 100);
			this.PontoAtual = b;
			System.out.println("Chegou no ponto " + b);
			relatorioIndividual.add("Chegou no ponto" + b);

			if(tipo != 0) {
				try {
					PontoDeColeta pColeta = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
					if(pColeta.getnCachorros()  + pColeta.getnGatos() + pColeta.getnRatos() > 0) { // Caso tenha animais no local:
						try {
							mapa.situacaoPonto(PontoAtual); // Verifica a situação do ponto
							Carrocinha.chamarControle(mapa, PontoAtual); // Chama uma carrocinha pro local
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}catch (Exception e) {
					System.out.println("não foi possível chamar a carrocinha -------------------------------------------------------------------");
				}
				coletar();
				if(tipo == 2) {
					try {
						mapa.getFolhasMod().add(buscarAntecessor(PontoAtual, percurso.get(i + 1)));
					}catch (Exception e) {
						break;
					}
					System.out.println("Nova folha adicionada: " + percurso.get(i + 1));
				}
				if(tipo == 3) {
					PontoDeColeta aux = (PontoDeColeta) mapa.getVertices().get(PontoAtual);
					if(aux.getvLixo() == 0) { // Caso encontre lixo, interrompe o processo
						break;
					}
				}
			}
		}
	}

	private int buscarAntecessor(int atual, int prox) { // Ideia descartada 
		int n = mapa.getW().length;
		for(int i = 0; i < n; i++) {
			int j = mapa.getMst().mst[atual][i];
			if(j != atual && j > 0) {
				return j;
			}
		}
		return -1;
	}
	
	public void setFuncionarios(int nFuncionarios) {
		this.nFuncionarios = nFuncionarios;
	}
	
	public int getFuncionarios() {
		return nFuncionarios;
	}


	public int getTempoGastoPercorrendoCaminho() {
		return tempoGastoPercorrendoCaminho;
	}

	public void setTempoGastoPercorrendoCaminho(int tempoGastoPercorrendoCaminho) {
		this.tempoGastoPercorrendoCaminho = tempoGastoPercorrendoCaminho;
	}

	public int getTempoGastoColetandoLixo() {
		return tempoGastoColetandoLixo;
	}

	public void setTempoGastoColetandoLixo(int tempoGastoColetandoLixo) {
		this.tempoGastoColetandoLixo = tempoGastoColetandoLixo;
	}
	
	@Override
	public void run() {
		while(!mapa.getFolhasMod().isEmpty()) { // Enquanto o vetor de folhas ainda tiver folhas
			int folha = menor(mapa.getFolhasMod()); // procura a folha mais próxima que ainda não foi visitada ou está em rota
			if(folha > 0) { // Se conseguiu buscar a folha mais próxima com sucesso:
				int iFolha = mapa.getFolhasMod().indexOf(folha);
				List<Integer> percurso = seguirRamo(folha).reversed(); // Traça o percurso pelo ramo até a folha
				PontoDeColeta destino = (PontoDeColeta) mapa.getVertices().get(folha);
				destino.emRota = true; // Marca a folha como em rota
				try {
					percorrer(percurso, 1); // percorre do aterro até a folha mais próxima
					System.out.println("o lixo na folha ainda é de: " + destino.getvLixo());
					relatorioIndividual.add("O lixo restante na folha ainda é de " + destino.getvLixo());

					System.out.println("número da folha: " + folha);
					System.out.println("Indice da folha: " + iFolha);
					System.out.println("Ramo completo.");
					System.out.println("Vetor de folhas: " + mapa.getFolhasMod().toString());
					try {
						mapa.getFolhasMod().remove(mapa.getFolhasMod().indexOf(folha)); // Se ele chegou até a folha e a recolheu, remove ela do vetor de folhas
					} catch (Exception e) {
						System.out.println("A folha já foi removida.");
					}
					System.out.println("Vetor de folhas: " + mapa.getFolhasMod().toString());
					relatorioIndividual.add("Vetor de folhas: " + mapa.getFolhasMod().toString());

					while(lixoArmazenado != capacidade && !mapa.getFolhasMod().isEmpty()) { // Enquanto o caminhão tiver capacidade e existirem folhas:
						folha = menor(mapa.getFolhasMod());
						if(folha != -1) {
							iFolha = mapa.getFolhasMod().indexOf(folha);
							destino = (PontoDeColeta) mapa.getVertices().get(folha);
							destino.emRota = true;
							percurso = mapa.getPercursos()[PontoAtual][folha];
							try {
								percorrer(percurso, 0); // Percorre da última folha até a folha mais próxima pelo menor caminho o possível e sem recolher nada
							} catch (InterruptedException | CapacidadeMaximaException e) {
								// TODO Auto-generated catch block
							}
							percurso = seguirRamo(folha);
							try {
								percurso.remove(percurso.size()-1); // Traça o Percurso da folha até o Aterro, mas excluindo o aterro
							}catch (Exception e) {
								// TODO: handle exception
							}
							try {
								System.out.println("o lixo na folha ainda é de: " + destino.getvLixo());
								
								System.out.println("número da folha: " + folha);
								System.out.println("Indice da folha: " + iFolha);
								System.out.println("Ramo completo.");
								System.out.println("Vetor de folhas: " + mapa.getFolhasMod().toString());
								try {
									mapa.getFolhasMod().remove(mapa.getFolhasMod().indexOf(folha)); // Remove a folha do vetor de folhas
								} catch (Exception e) {
									System.out.println("A folha já foi removida.");
								}
								percorrer(percurso, 3); // E começa a percorrer dela até o aterro

							} catch (InterruptedException | CapacidadeMaximaException e) {
								destino.emRota = false;
								if(mapa.getFolhasMod().indexOf(PontoAtual) == -1) {
									mapa.getFolhasMod().add(PontoAtual);
									System.out.println("Nova folha adicionada ao vetor de folhas: " + PontoAtual + " -> " + mapa.getFolhasMod().toString());
								}
								try {
									retornar(); // Caso o caminhão fique cheio, retorna para esvaziar
									System.out.println("O caminhão voltou para base pois a capacidade máxima foi atingida");
									relatorioIndividual.add("O caminhão voltou para a base pois a capacidade máxima foi atingida!");

								} catch (InterruptedException | CapacidadeMaximaException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								break;
							}
							System.out.println("número da folha: " + folha);
							System.out.println("Indice da folha: " + iFolha);
							System.out.println("Ramo completo.");
							System.out.println("Vetor de folhas: " + mapa.getFolhasMod().toString());
							try {
								mapa.getFolhasMod().remove(mapa.getFolhasMod().indexOf(folha)); // tenta remover a folha se ela ainda não foi removida
							} catch (Exception e) {
								System.out.println("A folha já foi removida.");
							}
						}
					}
				} catch (InterruptedException | CapacidadeMaximaException e) {
					destino.emRota = false;
					try {
						retornar(); // Caso o caminhão fique cheio, retorna para esvaziar
						System.out.println("O caminhão voltou para base pois a capacidade máxima foi atingida");
					} catch (InterruptedException | CapacidadeMaximaException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}else {
				break;
			}
		}
		System.out.println("Tempo para recolher todo lixo: " + tempoGastoColetandoLixo + " minutos");
		System.out.println("Tempo para percorrer caminho: " + tempoGastoPercorrendoCaminho + " minutos");
		relatorioIndividual.add("Resultado da rota do caminhão");
		relatorioIndividual.add("Tempo para recolher todo lixo: " + tempoGastoColetandoLixo + " minutos");
		relatorioIndividual.add("Tempo para percorrer caminho: " + tempoGastoPercorrendoCaminho + " minutos");
		relatorioIndividual.add("Tempo total gasto: " + String.format("%d", tempoGastoColetandoLixo + tempoGastoPercorrendoCaminho) );
		latch.countDown();	
	}

	public void  construirRelatorio() { // Método que escreve o trajeto do caminhão em um arquivo;
		 try {
	            Files.write(Paths.get(caminhoArquivo),relatorioIndividual);
	            System.out.println("Escrita concluída!");
	        } catch (IOException e) {
	            System.out.println("Ocorreu um erro: " + e.getMessage());
	        }
	}
	
	public int totalTempo() {
		return tempoGastoPercorrendoCaminho + tempoGastoColetandoLixo;
	}

	
	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public List<String> getRelatorioIndividual() {
		return relatorioIndividual;
	}

	public void setRelatorioIndividual(List<String> relatorioIndividual) {
		this.relatorioIndividual = relatorioIndividual;
	}
	
}