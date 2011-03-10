/*
 * OperadorLogico.java
 *
 * Created on 9 de Setembro de 2005, 19:21
 */

package token;

/**
 *
 * @author  20031000178
 */
public class OperadorLogico extends Operador{
    
    /** Creates a new instance of OperadorLogico */
    public OperadorLogico(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return super.getTipo()+" Lógico";
    }
    
}
