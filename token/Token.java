/*
 * Token.java
 *
 * Created on 9 de Setembro de 2005, 19:08
 */

package token;

/**
 *
 * @author  20031000178
 */
public abstract class Token {
    protected String lexema;
    protected int linha;
    protected int caractere;
    /** Creates a new instance of Token */
    public Token(String lexema,int linha,int caractere){
        this.lexema=lexema;
        this.linha=linha;
        this.caractere=caractere;
    }
    
    public abstract String getTipo();//metodo para retornar o tipo do token


    public String getLexema(){
        return this.lexema;
    }
    
    public int getLinha(){
        return this.linha;
    }
    
    public int getCaractere(){
        return this.caractere;
    }
}


