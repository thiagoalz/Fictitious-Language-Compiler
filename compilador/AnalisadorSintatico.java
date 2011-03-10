/*
 * AnalisadorSintatico.java
 *
 * Created on 9 de Setembro de 2005, 19:21
 */
package compilador;

import token.*;
import erros.*;
/**
 * @author  20031000178
 */
public class AnalisadorSintatico {
    private AnalisadorLexico lexico;
    private Token lookAhead;
    private Erro Erros;
    private TabelaDeSimbolos tabela;
    private String escopo;
    /** Creates a new instance of AnalisadorSintatico */
    public AnalisadorSintatico() {
    	this.Erros=new Erro("");
        this.lexico = new AnalisadorLexico(Erros);
        this.tabela=new TabelaDeSimbolos();
        this.escopo="main";
    }
    
    public void analizar(String texto)throws Erro{
        this.lexico.setTexto(texto);
        Erros.limpaErros();
        tabela.limpaTabela();
        this.escopo="main";
        System.out.println("----------------------------------------------------");
        if(!lexico.isFimArquivo()){
        	lookAhead =this.lexico.getNextToken();
        	S();        	
        	if(!lexico.isFimArquivo() || lookAhead!=null){
        		Erros.addErro("Erro -> Existem Caracteres apos o Final na compilaçao",lexico.getNumeroCaractere());
        	}
        	if(Erros.temErro()){
        		throw Erros;
        	}
        	//System.out.println("Token->"+atual.getLexema()+"  Tipo->"+atual.getTipo()+"\n");
        }
        /*
        while(!lexico.isFimArquivo()){
        	lookAhead =this.lexico.getNextToken();
        	if(lookAhead!=null){
        		System.out.println("Token->"+lookAhead.getLexema()+"  Tipo->"+lookAhead.getTipo()+"\n");
        	}
        }
        if(Erros.temErro()){
    		throw Erros;
    	}
    	*/
        System.out.println("------------TRADUCAO----------");
    	System.out.println(lexico.getTraducao());
    	System.out.println("------------FIM----------");
    }
    
    
//////////////////////////////
    void reconheceLexema(String Lexema){
        //se o lexema esperado for igual ao do lookAhead, pega proximo token
        //senao informa o erro
        if (lookAhead!=null){
            if (lookAhead.getLexema().toUpperCase().equals(Lexema.toUpperCase())){//O compilador nao eh case sensitive
                
            	try{
                	lookAhead = this.lexico.getNextToken();
                }catch(Exception e){
                	System.out.println(e.getMessage());
                }
                
            }else{
                reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Era esperado: '" + Lexema + "'. Mas foi encontrado '" + lookAhead.getLexema() + "'.",lexico.getNumeroCaractere());
            }
        }else{
            reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Era esperado lexema '" + Lexema + "'. Mas nenhum token foi encontrado.",lexico.getNumeroCaractere());
        }
    }
    
    void reconheceClasse(String Classe){
        //se a classe esperada for igual a do lookAhead, pega proximo token
        //senao informa o erro
        if (lookAhead!=null){
            if (lookAhead.getTipo().toUpperCase().equals(Classe.toUpperCase())){
            	
            	try{
                	lookAhead = this.lexico.getNextToken();
                }catch(Exception e){
                	System.out.println(e.getMessage());
                }
           
            }else{
                reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Era esperada classe '" + Classe + "'. Mas foi encontrado '" + lookAhead.getTipo() + "'.",lexico.getNumeroCaractere());
            }
        }else{
            reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Era esperada classe '" + Classe.toUpperCase() + "'. Mas nenhum token foi encontrado.",lexico.getNumeroCaractere());
        }
    }

    boolean testaLexema(String Lexema){
        if ((lookAhead!=null)&&(lookAhead.getLexema().toUpperCase().equals(Lexema.toUpperCase())))
            return true;
        else
            return false;
    }

    boolean testaClasse(String Classe){
        if ((lookAhead!=null)&&(lookAhead.getTipo().toUpperCase().equals(Classe.toUpperCase())))
            return true;
        else
            return false;
    }
///////////////////////////////////////
    
//  ------------------------------------------------------------------------------------------------         
      private void reportarErro(String novoErro,int caractere){
          if (novoErro.trim().length()!=0)
              Erros.addErro(novoErro,caractere);
      }
    
//  ----------------------------------------------------------------------------------    
//  inicio da gramatica
//  ----------------------------------------------------------------------------------        
      
      void S(){
          // S-> definicoes() bloco()
          definicoes();
          bloco("");
      }
      
      void definicoes(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          /* definicoes() ->  CONSTANTE lista_Const() Definicoes() 
                              | DECLARE lista_Var1() Definicoes() 
                              | FUNCAO IF def_Funcao1() Definicoes() 
                              | PROCEDIMENTO ID def_Procedimento1 Definicoes() 
                              | vazio       */
          if (testaLexema("CONSTANTE")){
              reconheceLexema("CONSTANTE");
              lista_Const();
              definicoes();
          }
          else if (testaLexema("DECLARE")){
              reconheceLexema("DECLARE");
              lista_Var1();
              definicoes();
          }
          else if (testaLexema("FUNCAO")){
              reconheceLexema("FUNCAO");
              String id=lookAhead.getLexema();
              reconheceClasse("Identificador");
              Def_Funcao1(id);
              definicoes();
          }
          else if (testaLexema("PROCEDIMENTO")){
              reconheceLexema("PROCEDIMENTO");
              String id=lookAhead.getLexema();
              reconheceClasse("Identificador");
              Def_Procedimento1(id);
              definicoes();
          }
      }
       
      void lista_Const(){
    	  String nomeConst=null;
    	  String tipo;
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          //lista_Const() -> IDENTIFICADOR = Valor(); lista_Const1()
          if(testaClasse("Identificador")){
        	  nomeConst=lookAhead.getLexema();        	  
          }
          reconheceClasse("Identificador");
          reconheceLexema("=");
          tipo=valor();
          {
        	  if(nomeConst!=null){
        		  tabela.addConstante(nomeConst,tipo);
        	  }
          }
          reconheceLexema(";");
          lista_Const1();
      }
      
      void lista_Const1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          //lista_Const1() -> lista_Const() | vazio
          if (testaClasse("IDENTIFICADOR"))
              lista_Const();
      }
      
      String valor(){
    	  String valor=null;
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return null;

          //Valor() -> ConstanteNUMERICAInteira | ConstanteNUMERICAReal | ConstanteAlfaNumerica | ConstanteBooleana          
          if (testaClasse("Numero Inteiro")){
        	  valor="inteiro";
              reconheceClasse("Numero Inteiro");
          }else if (testaClasse("Numero Real")){
        	  valor="real";
              reconheceClasse("Numero Real");
          }else if (testaClasse("Alfa Numérico")){
        	  valor="caracter";
              reconheceClasse("Alfa Numérico");
          }else if (testaClasse("Booleano")){
        	  valor="logico";
              reconheceClasse("Booleano");
          }else
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperada classe: ConstanteInteira ou ConstanteReal ou Alfa Numérico ou Booleano",lexico.getNumeroCaractere());
          
          return valor;
      }
      
      void lista_Var(){
    	  String lista;
    	  String tipo;
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          //lista_Var -> lista_ID: Tipo; lista_Var1
          lista=lista_ID();
          reconheceLexema(":");
          tipo=Tipo();
          {
        	  tabela.addVariaveis(escopo,lista,tipo);
          }
          reconheceLexema(";");
          lista_Var1();
      }
      
      void lista_Var1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          //lista_Var1 -> lista_Var | vazio
          if (testaClasse("IDENTIFICADOR"))
              lista_Var();
      }

      String Tipo(){
    	  String tipo="";
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return tipo;

          //Tipo ->INTEIRO | REAL | CARACTER | LOGICO | VETOR[Faixa] DE Tipo_Simples
          if (testaLexema("INTEIRO")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("INTEIRO");
          }else if (testaLexema("REAL")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("REAL");
          }else if (testaLexema("CARACTER")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("CARACTER");
          }else if (testaLexema("LOGICO")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("LOGICO");
          }else if (testaLexema("VETOR")){
              reconheceLexema("VETOR");        
              reconheceLexema("[");
              faixa();
              reconheceLexema("]");
              reconheceLexema("DE");
              tipo=Tipo_Simples();
              tipo="VETOR DE "+tipo;
          }
          else// Tipo nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Lexema: INTEIRO, REAL, CARACTER, LOGICO ou VETOR.",lexico.getNumeroCaractere());
          
          return tipo;
      }
      
      String Tipo_Simples(){
    	  String tipo="";
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return tipo;

          //Tipo_Simples -> INTEIRO | REAL | CARACTER | LOGICO     
          if (testaLexema("INTEIRO")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("INTEIRO");
          }else if (testaLexema("REAL")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("REAL");
          }else if (testaLexema("CARACTER")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("CARACTER");
          }else if (testaLexema("LOGICO")){
        	  tipo=lookAhead.getLexema();
              reconheceLexema("LOGICO");
          }else//tipo_simples nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Lexema: INTEIRO, REAL, CARACTER ou LOGICO.",lexico.getNumeroCaractere());
          
          return tipo;
      }
      
      void faixa(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          //Faixa -> Numero Inteiro..Numero Inteiro Faixa1
          reconheceClasse("Numero Inteiro");
          reconheceLexema("..");
          reconheceClasse("Numero Inteiro");
      }
      
      void faixa1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Faixa1 -> ,Faixa | vazio
          if (testaLexema(",")){
              reconheceLexema(",");
              faixa();
          }
      }
      
      void bloco(String nome){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Bloco -> INICIO lista_Cmd FIM
          reconheceLexema("INICIO");
          lista_Cmd();
          reconheceLexema("FIM");
          if(nome!=""){
        	  escopo="main";
          }
      }

      void lista_Cmd(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //lista_Cmd -> Cmd; lista_Cmd1
          Cmd();
          reconheceLexema(";");
          lista_Cmd1();
      }
      
      void lista_Cmd1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //lista_Cmd1 -> lista_Cmd | vazio
          if ( (testaClasse("Identificador")) || (testaLexema("SE")) || (testaLexema("PARA")) || (testaLexema("ENQUANTO")) || (testaLexema("REPITA")) || (testaLexema("ESCREVA")) || (testaLexema("LEIA")) )
              lista_Cmd();
      }

      String lista_ID(){
    	  String retorno="";
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return retorno;
          
          //lista_ID -> ID lista_ID1
          retorno=lookAhead.getLexema();
          reconheceClasse("Identificador");
          retorno+=lista_ID1();
          
          return retorno;
      }

      String lista_ID1(){
    	  String retorno="";
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return retorno;
          
          //lista_ID1 -> ,lista_ID | vazio
          if (testaLexema(",")){
        	  retorno=",";
              reconheceLexema(",");
              retorno+=lista_ID();
          }
          return retorno;
      }
      

      void Cmd(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Cmd -> ID Cmd1 | SE Exp_Logica ENTAO D Cmd_SE1 | PARA ID Ind DE I ATE I FACA D
  	//	| ENQUANTO Exp_Logica FACA D | REPITA D ATE Exp_Logica | ESCREVA( Cmd_Escreva1
  	//	| LEIA( lista_ID )
          if (testaClasse("Identificador")){//ID Cmd1
        	  String nome=lookAhead.getLexema();
              reconheceClasse("Identificador");
              Cmd1(nome);
          }
          else if (testaLexema("SE")){//SE Exp_Logica ENTAO D Cmd_SE1
              reconheceLexema("SE");
              Exp_Logica();
              reconheceLexema("ENTAO");                
              D();
              Cmd_SE1();
          }
          else if (testaLexema("PARA")){//PARA ID Ind DE I ATE I FACA D
              reconheceLexema("PARA");
              reconheceClasse("Identificador");
              IND();
              reconheceLexema("DE");
              I();
              reconheceLexema("ATE");
              I();
              reconheceLexema("FACA");
              D();            
          }
          else if (testaLexema("ENQUANTO")){//ENQUANTO Exp_Logica FACA D
              reconheceLexema("ENQUANTO");
              Exp_Logica();
              reconheceLexema("FACA");
              D();            
          }        
          else if (testaLexema("REPITA")){//REPITA D ATE Exp_Logica
              reconheceLexema("REPITA");
              D();            
              reconheceLexema("ATE");
              Exp_Logica();
          }        
          else if (testaLexema("ESCREVA")){//ESCREVA( Cmd_Escreva1
              reconheceLexema("ESCREVA");
              reconheceLexema("(");
              Cmd_Escreva1();
          }        
          else if (testaLexema("LEIA")){//LEIA( lista_ID )
              reconheceLexema("LEIA");
              reconheceLexema("(");
              lista_ID();
              reconheceLexema(")");
          }        
          else//cmd nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Identificador ou Lexema: SE, PARA, ENQUANTO, REPITA, ESCREVA ou LEIA.",lexico.getNumeroCaractere());
      }

      void Cmd1(String nome){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          Variavel varaux;
          //Cmd1 -> Ind <- Cmd_Atr | (LP) | vazio    
          if (testaLexema("[")){
        	  varaux=tabela.getVar(escopo,nome);
        	  if(varaux==null){
        		  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+nome+"' Nao declarada",lexico.getNumeroCaractere());
        	  }else{
        		  if(!varaux.isVetor()){
        			  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+nome+"' Nao é um vetor",lexico.getNumeroCaractere());
        		  }
        	  }
              IND();
              reconheceLexema("<-");
              Cmd_Atr(varaux);
          }
          else if (testaLexema("<-")){
        	  varaux=tabela.getVar(escopo,nome);
        	  if(varaux==null){
        		  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+nome+"' Nao declarada",lexico.getNumeroCaractere());
        	  }
              reconheceLexema("<-");
              Cmd_Atr(varaux);
          }
          else if (testaLexema("(")){
        	  Procedimento auxproc=tabela.getMetodo(nome);
        	  if(auxproc==null){
        		  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Funcao ou Procedimento '"+nome+"' Nao declarado",lexico.getNumeroCaractere());
        	  }
              reconheceLexema("(");
              LP(auxproc,0);
              reconheceLexema(")");
          }else{// vazio
        	  Procedimento auxproc=tabela.getMetodo(nome);
        	  if(auxproc==null){
        		  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Funcao ou Procedimento '"+nome+"' Nao declarado",lexico.getNumeroCaractere());
        	  }
          }
      }
      
      void Cmd_SE1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Cmd_SE1 -> SENAO D | vazio 
          if (testaLexema("SENAO")){
              reconheceLexema("SENAO");
              D();
          }
      }

      void Cmd_Escreva1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Cmd_Escreva1 -> lista_ID) | Alfa Numérico Cmd_Escreva2
          if (testaClasse("Identificador")){
              lista_ID();
              reconheceLexema(")");            
          }
          else if (testaClasse("Alfa Numérico")){
              reconheceClasse("Alfa Numérico");
              Cmd_Escreva2();
          }
          else//Cmd_Escreva1 nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Identificador ou Constante Alfa-Numerica.",lexico.getNumeroCaractere());
      }

      void Cmd_Escreva2(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Cmd_Escreva2 -> ,lista_ID) | )
          if (testaLexema(",")){
              reconheceLexema(",");
              lista_ID();
              reconheceLexema(")");
          }
          else if (testaLexema(")")){
              reconheceLexema(")");
          }
          else//Cmd_Escreva2 nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado , ou ).",lexico.getNumeroCaractere());
      }

      void Def_Funcao1(String nome){
    	  String aux;
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Def_Funcao1 -> (lista_Par_Def):Tipo_Simples def_Var1 Bloco | :Tipo_Simples def_Var1 Bloco
          boolean ok=tabela.addFuncao(nome);
          if(!ok){
        	  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Funcao ou Procedimento '"+nome+"' duplicada(o)",lexico.getNumeroCaractere());
          }
          escopo=nome;
          if (testaLexema("(")){
              reconheceLexema("(");
              lista_Par_Def(nome);
              reconheceLexema(")");
              reconheceLexema(":");
              aux=Tipo_Simples();
              Funcao x=(Funcao)tabela.getMetodo(nome);
              x.setRetorno(aux);
              Def_Var1();
              bloco(nome);
          }
          else if (testaLexema(":")){
              reconheceLexema(":");
              aux=Tipo_Simples();
              Funcao x=(Funcao)tabela.getMetodo(nome);
              x.setRetorno(aux);
              Def_Var1();
              bloco(nome);            
          }
          else//Def_Funcao1 nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado : ou (.",lexico.getNumeroCaractere());
      }
      
      void lista_Par_Def(String nome){
    	  String aux,tipo;
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          //lista_Par_Def -> lista_ID: Tipo_Simples; lista_Par_Def1
          aux=lista_ID();
          reconheceLexema(":");
          tipo=Tipo_Simples();
          tabela.getMetodo(nome).addParametros(aux,tipo);
          reconheceLexema(";");
          lista_Par_Def1(nome);
      }
      
      void lista_Par_Def1(String nome){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;

          //lista_Par_Def1 -> lista_Par_Def | vazio
          if (testaClasse("Identificador")){
              lista_Par_Def(nome);
          }
      }

      void Def_Procedimento1(String nome){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Def_Procedimento1 -> (lista_Par_Def) def_Var1 Bloco | def_Var1 Bloco
          boolean ok= tabela.addProcedimento(nome);
          escopo=nome;
          if(!ok){
        	  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Funcao ou Procedimento '"+nome+"' duplicada(o)",lexico.getNumeroCaractere());
          }
          if (testaLexema("(")){
              reconheceLexema("(");
              lista_Par_Def(nome);
              reconheceLexema(")");
              Def_Var1();
              bloco(nome);
          }
          else if (testaLexema("DECLARE")){
              Def_Var1();
              bloco(nome);            
          }
          else//Def_Procedimento1 nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado ( ou DECLARE.",lexico.getNumeroCaractere());
      }

      void Def_Var1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Def_Var1 -> DECLARE lista_Var | vazio
          if (testaLexema("DECLARE")){
              reconheceLexema("DECLARE");
              lista_Var();
          }
      }

      void IND(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //IND -> [I LIND1] | vazio
          if (testaLexema("[")){
              reconheceLexema("[");
              I();
              LIND1();
              reconheceLexema("]");
          }
      }
      
      void LIND1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //LIND -> ,I LIND1 | vazio
          if (testaLexema(",")){
              reconheceLexema(",");
              I();
              LIND1();
          }
      }

      void I(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //I -> ID | Numero Inteiro
          if (testaClasse("Identificador"))
              reconheceClasse("Identificador");
          else if (testaClasse("Numero Inteiro"))
              reconheceClasse("Numero Inteiro");
          else//I nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Identificador ou Numero Inteiro.",lexico.getNumeroCaractere());
      }

      void Cmd_Atr(Variavel var){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Cmd_Atr -> T ExpressaoAritmetica1 | Alfa Numérico | Booleano
          if ((testaLexema("("))||(testaClasse("Numero Inteiro"))||(testaClasse("Numero Real"))||(testaClasse("Identificador"))){
        	  if( testaClasse("Numero Inteiro") && !var.getTipo().equals("inteiro")){
            	  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+var.getNome()+"' nao é 'Inteiro'",lexico.getNumeroCaractere());
              }else if(testaClasse("Numero Real")&& !var.getTipo().equals("real")){
            	  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+var.getNome()+"' nao é 'Real'",lexico.getNumeroCaractere());
              }
              T();
              ExpressaoAritmetica1();
          }else if (testaClasse("Alfa Numérico")){
              reconheceClasse("Alfa Numérico");
              if(!var.getTipo().equals("caracter")){
            	  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+var.getNome()+"' nao é do tipo 'Alfa numerico'",lexico.getNumeroCaractere());
              }
          }else if (testaClasse("Booleano")){
              reconheceClasse("Booleano");
              if(!var.getTipo().equals("logico")){
            	  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+var.getNome()+"' nao é do tipo 'Booleano'",lexico.getNumeroCaractere());
              }
          }else//Cmd_Atr nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado (, ContanteAlfaNumerica ou Booleano.",lexico.getNumeroCaractere());
      }

      void ExpressaoAritmetica1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //ExpressaoAritmetica1 -> +T ExpressaoAritmetica1 | -T ExpressaoAritmetica1 | vazio
          if (testaLexema("+")){
              reconheceLexema("+");
              T();
              ExpressaoAritmetica1();
          }
          else if (testaLexema("-")){
              reconheceLexema("-");
              T();
              ExpressaoAritmetica1();
          }
      }

      void T(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //T -> FT1
          F();
          T1();
      }

      void T1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //T1 -> *FT1 | /FT1 | vazio
          if (testaLexema("*")){
              reconheceLexema("*");
              F();
              T1();
          }
          else if (testaLexema("/")){
              reconheceLexema("/");
              F();
              T1();
          }        
      }

      void F(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //F -> (T ExpressaoAritmetica1) | Numero Inteiro | Numero Real | ID F1
          if (testaLexema("(")){
              reconheceLexema("(");
              T();
              ExpressaoAritmetica1();
              reconheceLexema(")");
          }
          else if (testaClasse("Numero Inteiro"))
              reconheceClasse("Numero Inteiro");
          else if (testaClasse("Numero Real"))
              reconheceClasse("Numero Real");
          else if (testaClasse("Identificador")){
        	  String aux=lookAhead.getLexema();
              reconheceClasse("Identificador");
              F1(aux);
          }
          else//F nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado (, ContanteNumericaInteira, ContanteNumericaReal ou Identificador.",lexico.getNumeroCaractere());
      }
      
      void F1(String nome){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //F1 -> IND | (LP)
          if (testaLexema("(")){
        	  Procedimento auxproc=tabela.getMetodo(nome);
        	  if(auxproc==null){
        		  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Funcao ou Procedimento '"+nome+"' Nao declarado",lexico.getNumeroCaractere());
        	  }
              reconheceLexema("(");
              LP(auxproc,0);
              reconheceLexema(")");
          }else{
        	  if(tabela.getVar(escopo,nome)==null){
        		  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+nome+"' Nao declarada",lexico.getNumeroCaractere());
        	  }
              IND();            
          }
          //else F1 nao retorna vazio
          //   reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado ( ou [.");
      }

      void LP(Procedimento proc,int num){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //LP -> valor LP1 | ID LP1
          if ((testaClasse("Numero Inteiro")) || (testaClasse("Numero Real")) || (testaClasse("Alfa Numérico")) || (testaClasse("Booleano"))){        	  
        	  String val=valor();
        	  {
        		  Variavel varEsperada=proc.getParametro(num);
        		  if(varEsperada==null){
        			  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Parametro nao esperado para a funcao/procedimento '"+proc.getNome()+"'",lexico.getNumeroCaractere());
        		  }else{
        			  if(!varEsperada.getTipo().equals(val)){
        				  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Parametro esperado para a funcao/procedimento '"+proc.getNome()+"' era do tipo '"+varEsperada.getTipo()+"'",lexico.getNumeroCaractere());
        			  }
        		  }
        	  }
              LP1(proc,num);
          }
          else if (testaClasse("Identificador")){
        	  Variavel varaux=tabela.getVar(escopo,lookAhead.getLexema());
        	  if(varaux==null){
        		  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Variavel '"+lookAhead.getLexema()+"' Nao declarada",lexico.getNumeroCaractere());
        	  }else{
        		  Variavel varEsperada=proc.getParametro(num);
        		  if(varEsperada==null){
        			  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Parametro nao esperado para a funcao/procedimento '"+proc.getNome()+"'",lexico.getNumeroCaractere());
        		  }else{
        			  if( !varEsperada.getTipo().equals(varaux.getTipo()) ){
        				  reportarErro("Erro Semantico("+lexico.getNumeroLinha()+","+lexico.getNumeroCaractere()+")-> Parametro esperado para a funcao/procedimento '"+proc.getNome()+"' era do tipo '"+varEsperada.getTipo()+"'",lexico.getNumeroCaractere());
        			  }
        		  }
        	  }
              reconheceClasse("Identificador");
              LP1(proc,num);
          }
          else//LP nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Identificador, Numero Inteiro, Numero Real, Alfa Numérico ou Booleano.",lexico.getNumeroCaractere());
      }

      void LP1(Procedimento proc,int num){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //LP1 -> ,LP | vazio
          if (testaLexema(",")){
              reconheceLexema(",");
              LP(proc,num+1);            
          }
      }

      void D(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //D -> bloco | Cmd
          if (testaLexema("INICIO"))
              bloco("");
          else if ((testaClasse("Identificador")) || (testaLexema("SE")) || (testaLexema("PARA")) || (testaLexema("ENQUANTO")) || (testaLexema("REPITA")) || (testaLexema("ESCREVA")) || (testaLexema("LEIA")))
              Cmd();
          else//D nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Identificador, SE, PARA, ENQUANTO, REPITA, ESCREVA ou LEIA.",lexico.getNumeroCaractere());
      }    

      void Termo(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Termo -> T ExpressaoAritmetica1 | Alfa Numérico
          if ((testaLexema("(")) || (testaClasse("Numero Inteiro")) || (testaClasse("Numero Real")) || (testaClasse("Identificador"))){
              T();
              ExpressaoAritmetica1();
          }
          else if (testaClasse("Alfa Numérico"))
              reconheceClasse("Alfa Numérico");
          else//Termo nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado (, Identificador, ConstanteNumerica ou Alfa Numérico.",lexico.getNumeroCaractere());
      }    

      void OP_Logico(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //OP_Logico -> E | OU
          if (testaClasse("Operador Lógico"))
              reconheceClasse("Operador Lógico");
          else//OP_Logico nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Operador Lógico.",lexico.getNumeroCaractere());
      }    

      void OP_Relacional(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //OP_Relacional -> < | <= | > | >= | = | <>
          if (testaClasse("Operador Relacional"))
              reconheceClasse("Operador Relacional");
          else//OP_Relacional nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Operador Relacional.",lexico.getNumeroCaractere());
      } 
      
      //AQUI TINHA ALGO DE ERRADO, TIVE QUE FAZER ALTERACAO E PARECE ESTAR CERTO AGORA.
      void Exp_Relacional1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Exp_Relacional1 -> IND OP_Relacional Termo | vazio
          if (testaLexema("[")){
              IND();
          }
          if (testaClasse("Operador Relacional")){
              OP_Relacional();
              Termo();
          }
          
      }    

      void Exp_Logica(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Exp_Logica -> ID Exp_Relacional1 | NÃO (Exp_Logica1 | (Exp_Logica1
          if (testaLexema("NAO")){
              reconheceLexema("NAO");
              reconheceLexema("(");
              Exp_Logica1();
          }
          else if (testaClasse("Identificador")){
              reconheceClasse("Identificador");
              Exp_Relacional1();
          }
          else if (testaLexema("(")){
              reconheceLexema("(");
              Exp_Logica1();
          }
          else//Exp_Logica nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Identificador, ( ou NAO.",lexico.getNumeroCaractere());
      }    

      void Exp_Logica1(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Exp_Logica1 -> ID Exp_Relacional1) Exp_Logica2 | NAO(Exp_Logica1) | (Exp_Logica1)
          if (testaClasse("Identificador")){
              reconheceClasse("Identificador");
              Exp_Relacional1();
              reconheceLexema(")");
              Exp_Logica2();
          }
          else if (testaLexema("NAO")){
              reconheceLexema("NAO");
              reconheceLexema("(");
              Exp_Logica1();
              reconheceLexema(")");
          }
          else if (testaLexema("(")){
              reconheceLexema("(");
              Exp_Logica1();
              reconheceLexema(")");
          }
          else//Exp_Logica1 nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado Identificador, ( ou NAO.",lexico.getNumeroCaractere());
      }    

      void Exp_Logica2(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Exp_Logica2 -> Exp_Logica2 ? Operador Lógico Exp_Logica3 |vazio
          if (testaClasse("Operador Lógico")){
              reconheceClasse("Operador Lógico");
              Exp_Logica3();
          }
      }    

      void Exp_Logica3(){
          if (Erros.temErro())//retorna se tiver acontecido algum erro
              return;
          
          //Exp_Logica3 -> NAO(ID Exp_Relacional1) | (ID Exp_Relacional1) 
          if (testaLexema("NAO")){
              reconheceLexema("NAO");
              reconheceLexema("(");
              reconheceClasse("Identificador");
              Exp_Relacional1();
              reconheceLexema(")");            
          }
          else if (testaLexema("(")){
              reconheceLexema("(");
              reconheceClasse("Identificador");
              Exp_Relacional1();
              reconheceLexema(")");            
          }
          else//Exp_Logica3 nao retorna vazio
              reportarErro("Erro Sintático(" + lexico.getNumeroLinha() +","+lexico.getNumeroCaractere()+ ")-> Esperado ( ou NAO.",lexico.getNumeroCaractere());
      }
//    ----------------------------------------------------------------------------------    
//    FIM da gramatica
//    ----------------------------------------------------------------------------------
    
}
