/*
 * NumeroReal.java
 *
 * Created on 9 de Setembro de 2005, 19:22
 */

package token;

/**
 *
 * @author  20031000178
 */
public class NumeroReal extends Numero{
    
    /** Creates a new instance of NumeroReal */
    public NumeroReal(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return super.getTipo()+" Real";
    }
}
