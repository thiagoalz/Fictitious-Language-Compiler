package compilador;

import java.util.Hashtable;

public class TabelaDeSimbolos {
	private Hashtable variaveis;
	private Hashtable constantes;
	private Hashtable metodos;
	
	public TabelaDeSimbolos(){
		this.limpaTabela();
	}
	
	public void limpaTabela(){
		variaveis=new Hashtable();
		constantes=new Hashtable();
		metodos=new Hashtable();
	}
	
	public void addConstante(String nome,String tipo){
		Variavel aux=new Variavel(nome,tipo);
		constantes.put(nome.toLowerCase(),aux);
	}
	
	public void addVariaveis(String escopo,String lista,String tipo){
		String[] vet=lista.split(",");
		if(escopo.equals("main")){
			for(int i=0;i<vet.length;i++){
				Variavel aux=new Variavel(vet[i],tipo);
				variaveis.put(vet[i].toLowerCase(),aux);
			}
		}else{
			Procedimento proc=this.getMetodo(escopo);
			for(int i=0;i<vet.length;i++){
				Variavel aux=new Variavel(vet[i],tipo);
				proc.addVariavel(vet[i].toLowerCase(),aux);
			}
		}
	}
	
	public boolean addProcedimento(String nome){
		boolean retorno=true;
		if(getMetodo(nome)!=null){
			retorno=false;
		}else{
			Procedimento novo=new Procedimento(nome);
			this.metodos.put(nome.toLowerCase(),novo);
		}
		return retorno;
	}
	
	public boolean addFuncao(String nome){
		boolean retorno=true;
		if(getMetodo(nome)!=null){
			retorno=false;
		}else{
			Funcao nova=new Funcao(nome);
			this.metodos.put(nome.toLowerCase(),nova);
		}
		return retorno;		
	}
	
	public Procedimento getMetodo(String nome){
		return (Procedimento)metodos.get(nome);
	}
	
	public Variavel getVar(String escopo,String nome){
		Variavel retorno=null;
		if(!escopo.equals("main")){
			Procedimento es=(Procedimento)metodos.get(escopo);
			retorno=es.getVar(nome);	
			
		}
		if(retorno==null){
			retorno=(Variavel)variaveis.get(nome);
		}
				
		return retorno;
	}

}
