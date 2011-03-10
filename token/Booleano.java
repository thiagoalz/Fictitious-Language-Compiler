/*
 * Booleano.java
 *
 * Created on 9 de Setembro de 2005, 19:34
 */

package token;

/**
 *
 * @author  20031000178
 */
public class Booleano extends Token{
    
    /** Creates a new instance of Booleano */
    public Booleano(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return "Booleano";
    }
    
}
