/*
 * Operador.java
 *
 * Created on 12 de Setembro de 2005, 13:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package token;

/**
 *
 * @author thiago
 */
public abstract class Operador extends Token{
    
    /** Creates a new instance of Operador */
    public Operador(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return "Operador";
    }
    
}
