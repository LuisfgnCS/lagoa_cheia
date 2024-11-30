package negócios;

public abstract class Ponto implements Cloneable {
	String nome;
	Boolean emRota = false;
	@Override
	public String toString() {
		return "Ponto [nome=" + nome + ", emRota=" + emRota + "]";
	}
	
	 @Override 
	 public Object clone() throws CloneNotSupportedException { return super.clone(); }
}
