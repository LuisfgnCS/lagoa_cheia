package negócios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import negócios.Gps.Result;

public class Bairro {
	private String nome;
	private List<Ponto> vertices = new ArrayList<>();
	private int[][] w; // matriz de pesos
	public List<Integer>[][] percursos;
	private int[][] distancias;
	private Result mst;
	private List<Integer> folhasMod;
	
	
	public Bairro(String nome, int[][] w) {
		super();
		this.nome = nome;
		this.w = w;
		this.distancias = w;
//		this.mst = Gps.calcularMSTeFolhas(w);
//		percursos = Gps.camMin(this);
//		setFolhasMod(new ArrayList<Integer>(mst.folhas));
	}
	
	public String getNome() {
		return nome;
	}
	public Result getMst() {
		return mst;
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

	public List<Integer> getFolhasMod() {
		return folhasMod;
	}

	public void setFolhasMod(List<Integer> folhasMod) {
		this.folhasMod = folhasMod;
	}

}
