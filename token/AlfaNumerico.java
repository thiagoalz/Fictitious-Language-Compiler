/*
 * Alfanumï¿½rico.java
 *
 * Created on 9 de Setembro de 2005, 19:34
 */

package token;

/**
 *
 * @author  20031000178
 */
public class AlfaNumerico extends Token{
    
    /** Creates a new instance of Alfanumï¿½rico */
    public AlfaNumerico(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return "Alfa Numérico";
    }
    
    public String getLexema(){
        return "'"+this.lexema+"'";
    }
    
}
