package compilador;

public class Variavel {
	private String nome,tipo;
	private boolean vetor;
	public Variavel(String nome,String tipo){
		this.nome=nome.toLowerCase();
		if(tipo.startsWith("VETOR DE")){
			this.vetor=true;
			tipo=tipo.substring(9,tipo.length());
		}
		this.tipo=tipo.toLowerCase();
		this.vetor=false;
	}
	
	public String getNome(){
		return this.nome;
	}
	
	public String getTipo(){
		return this.tipo;
	}
	
	public void setVetor(boolean x){
		this.vetor=x;
	}
	
	public boolean isVetor(){
		return this.vetor;
	}
}
