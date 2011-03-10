package compilador;

import java.util.ArrayList;
import java.util.Hashtable;

public class Procedimento {
	private String nome;
	private String tipo;
	private ArrayList parametros;
	private Hashtable variaveis;
	
	public Procedimento(String nome){
		this.nome=nome;
		parametros=new ArrayList();
		variaveis=new Hashtable();
	}
	
	
	public void addVariavel(String nome,Variavel var){
		variaveis.put(nome.toLowerCase(),var);
	}
	
	public void addParametros(String lista,String tipo){
		parametros.trimToSize();
		String[] vet=lista.split(",");
		for(int i=0;i<vet.length;i++){
			Variavel aux=new Variavel(vet[i],tipo);
			parametros.add(parametros.size(),aux);
		}		
	}
	
	public String getNome(){
		return this.nome;
	}
	public String getTipo(){
		return this.tipo;
	}
	
	public Variavel getVar(String nome){
		Variavel aux=(Variavel)variaveis.get(nome);
		return aux;
	}
	
	public Variavel getParametro(int pos){
		Variavel retorno=null;
		try{
			retorno=(Variavel)parametros.get(pos);
		}catch(Exception e){
			retorno=null;
		}
		return retorno;
	}
}
