/*
 * OperadorAritmetico.java
 *
 * Created on 9 de Setembro de 2005, 19:21
 */

package token;

/**
 *
 * @author  20031000178
 */
public class OperadorAritimetico extends Operador {
    
    /** Creates a new instance of OperadorAritmetico */
    public OperadorAritimetico(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return super.getTipo()+" Aritimetico";
    }
    
}