package negócios;

public class PontoDeColeta extends Ponto{
	private int nCachorros;
	private int nGatos;
	private int nRatos;
	private int vLixo;
	
	public PontoDeColeta() {
		int randomNumRato = (int)(Math.random() * 100);
		int randomNumGato = (int)(Math.random() * 100);
		int randomNumCachorro = (int)(Math.random() * 100);
		
		if (randomNumRato < 50) {
			nRatos = 1;
		} else {
			nRatos = 0;
		}
		
		if (randomNumGato < 25) {
			nGatos = 1;
		} else {
			nGatos = 0;
		}
		
		if (randomNumCachorro < 10) {
			nCachorros = 1;
		} else {
			nCachorros = 0;
		}
		
		vLixo = (int)(Math.random() * 20 - 10 + 1) + 10;
	}
	
	public void setnCachorros(int nCachorros) {
		this.nCachorros = nCachorros;
	}

	public void setnGatos(int nGatos) {
		this.nGatos = nGatos;
	}

	public void setnRatos(int nRatos) {
		this.nRatos = nRatos;
	}

	public int getnCachorros() {
		return nCachorros;
	}
	public int getnGatos() {
		return nGatos;
	}
	public int getnRatos() {
		return nRatos;
	}
	
	public void addnAnimais(int nCachorros, int nGatos, int nRatos) {
		this.nCachorros += nCachorros;
		this.nGatos += nGatos;
		this.nRatos += nRatos;
	}
	public int getvLixo() {
		return vLixo;
	}
	public void setvLixo(int vLixo) {
		this.vLixo = vLixo;
	}

	@Override
	public String toString() {
		return "PontoDeColeta [nCachorros=" + nCachorros + ", nGatos=" + nGatos + ", nRatos=" + nRatos + ", vLixo="
				+ vLixo + "]";
	}
	
	
}
