package negócios;

public abstract class Ponto {
	String nome;
	Boolean emRota = false;
	@Override
	public String toString() {
		return "Ponto [nome=" + nome + ", emRota=" + emRota + "]";
	}
}
