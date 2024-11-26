package negócios;

public class CaminhaoLixo {
	private int nFuncionarios;
	private double capacidade;
	
	public int coletar(PontoDeColeta pColeta) {
		int tempo = pColeta.getvLixo() / this.nFuncionarios;
		if(pColeta.getnCachorros() + pColeta.getnGatos() + pColeta.getnRatos() > 0) {
			tempo = tempo * 2;
			chamarControle(pColeta);
		}
		return tempo;
	}
	
	public void chamarControle(PontoDeColeta pColeta) {
		
	}
}
