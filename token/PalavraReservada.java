/*
 * PalavraReservada.java
 *
 * Created on 16 de Setembro de 2005, 12:28
 */

package token;

/**
 *
 * @author Administrador
 */
public class PalavraReservada extends Token{
    
    /** Creates a new instance of PalavraReservada */
    public PalavraReservada(String lexema,int linha,int caractere) {
        super(lexema,linha,caractere);
    }
    
    public String getTipo(){
        return "Palavra Reservada";
    }
    
}
