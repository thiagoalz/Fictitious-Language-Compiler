/*
 * Numero.java
 *
 * Created on 9 de Setembro de 2005, 19:22
 */

package token;

/**
 *
 * @author  20031000178
 */
public abstract class Numero extends Token{
    
    
    /** Creates a new instance of Numero */
    public Numero(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return "Numero";
    }
}
