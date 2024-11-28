package negócios;

import java.util.List;

import negócios.Gps.Result;

public class Bairro {
	private String nome;
	private List<Ponto> vertices;
	private int[][] w; // matriz de pesos
	public List<Integer>[][] percursos;
	private int[][] distancias;
	private Result mst;
	
	public Bairro(String nome, List<Ponto> vertices, int[][] w) {
		super();
		this.nome = nome;
		this.vertices = vertices;
		this.w = w;
		this.distancias = w;
		this.mst = Gps.calcularMSTeFolhas(w);
		percursos = Gps.camMin(this);
	}
	
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


	public int[][] getDistancias() {
		return distancias;
	}

	public List<Integer>[][] getPercursos() {
		return percursos;
	}
}
