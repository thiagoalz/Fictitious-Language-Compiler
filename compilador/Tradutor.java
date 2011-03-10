package compilador;

import token.Token;

public class Tradutor {
	private String texto;
	
	public Tradutor(){
		this.texto="";
	}
	
	public void add(char aux){
		texto+=aux;
	}
	public void add(String aux){
		texto+=aux;
	}
	
	public String getTraducao(){
		return this.texto;
	}
	
	public void reset(){
		this.texto="";
	}
	
	public void add(Token token){
		if(token!=null){
			if(token.getTipo().equals("Palavra Reservada")){
				if(token.getLexema().toLowerCase().equals("enquanto")){
					add("WHILE");
				}else if(token.getLexema().toLowerCase().equals("inicio")){
					add("BEGIN");
				}else if(token.getLexema().toLowerCase().equals("fim")){
					add("END");
				}else if(token.getLexema().toLowerCase().equals("se")){
					add("IF");
				}else if(token.getLexema().toLowerCase().equals("entao")){
					add("THEN");
				}else if(token.getLexema().toLowerCase().equals("senao")){
					add("ELSE");
				}else if(token.getLexema().toLowerCase().equals("constante")){
					add("DEFINE");
				}else if(token.getLexema().toLowerCase().equals("declare")){
					add("DECLARE");
				}else if(token.getLexema().toLowerCase().equals("vetor")){
					add("ARRAY");
				}else if(token.getLexema().toLowerCase().equals("inteiro")){
					add("INTEGER");
				}else if(token.getLexema().toLowerCase().equals("caracter")){
					add("CARACTER");
				}else if(token.getLexema().toLowerCase().equals("real")){
					add("REAL");
				}else if(token.getLexema().toLowerCase().equals("logico")){
					add("BOOLEAN");
				}else if(token.getLexema().toLowerCase().equals("faca")){
					add("DO");
				}else if(token.getLexema().toLowerCase().equals("de")){
					add("FROM");
				}else if(token.getLexema().toLowerCase().equals("ate")){
					add("CARACTER");
				}else if(token.getLexema().toLowerCase().equals("para")){
					add("FOR");
				}else if(token.getLexema().toLowerCase().equals("repita")){
					add("REPEAT");
				}else if(token.getLexema().toLowerCase().equals("nao")){
					add("NOT");
				}else if(token.getLexema().toLowerCase().equals("escreva")){
					add("WRITE");
				}else if(token.getLexema().toLowerCase().equals("leia")){
					add("READ");
				}else if(token.getLexema().toLowerCase().equals("funcao")){
					add("FUNCTION");
				}else if(token.getLexema().toLowerCase().equals("procedimento")){
					add("PROCEDURE");
				}else{
					add("Pal-Reservada_N_conhecida");
				}
			}else if(token.getTipo().equals("Operador Lógico")){
				if(token.getLexema().toLowerCase().equals("e")){
					add("AND");
				}else if(token.getLexema().toLowerCase().equals("ou")){
					add("OR");
				}else{
					add("OP-LOGICO_N_conhecido");
				}
			}else if(token.getTipo().equals("Booleano")){
				if(token.getLexema().toLowerCase().equals("verdadeiro")){
					add("TRUE");
				}else if(token.getLexema().toLowerCase().equals("falso")){
					add("FALSE");
				}else{
					add("Booleano_N_conhecido");
				}
			}else{
				add(token.getLexema());
			}
		}
	}

}
