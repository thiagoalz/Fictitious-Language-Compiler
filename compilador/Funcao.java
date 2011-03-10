package compilador;

public class Funcao extends Procedimento {
	private String retorno;
	
	public Funcao(String nome){
		super(nome);
	}
	
	public void setRetorno(String retorno){
		this.retorno=retorno;
	}
	
	public String getRetorno(){
		return this.retorno;
	}
	
}
