/*
 * NumeroInteiro.java
 *
 * Created on 9 de Setembro de 2005, 19:22
 */

package token;

/**
 *
 * @author  20031000178
 */
public class NumeroInteiro extends Numero{
    
    /** Creates a new instance of NumeroInteiro */
    public NumeroInteiro(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return super.getTipo()+" Inteiro";
    }
    
}
