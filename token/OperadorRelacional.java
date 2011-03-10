/*
 * OperadorRelacional.java
 *
 * Created on 9 de Setembro de 2005, 19:31
 */

package token;

/**
 *
 * @author  20031000178
 */
public class OperadorRelacional extends Operador{
    
    /** Creates a new instance of OperadorRelacional */
    public OperadorRelacional(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return super.getTipo()+" Relacional";
    }
    
}
