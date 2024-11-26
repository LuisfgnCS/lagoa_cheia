package negócios;

import java.util.List;

public class Bairro {
	private String nome;
	private List<Ponto> vertices;
	private int[][] w; // matriz de pesos
	public List<Integer>[][] getPercursos;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Ponto> getVertices() {
		return vertices;
	}
	public void setVertices(List<Ponto> vertices) {
		this.vertices = vertices;
	}
	public int[][] getW() {
		return w;
	}
	public void setW(int[][] w) {
		this.w = w;
	}
}
