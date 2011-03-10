/*
 * Identificador.java
 *
 * Created on 9 de Setembro de 2005, 19:21
 */

package token;

/**
 *
 * @author  20031000178
 */
public class Identificador extends Token {
    
    /** Creates a new instance of Identificador */
    public Identificador(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return "Identificador";
    }
    
}
