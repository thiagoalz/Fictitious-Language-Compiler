/*
 * Compilador.java
 *
 * Created on 10 de Setembro de 2005, 19:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package compilador;

import erros.*;
/**
 *
 * @author  20031000178
 */
public class Compilador{

    private AnalisadorSintatico sintatico;
    private AnalisadorSemantico semantico;
    /** Creates a new instance of Compilador */
    public Compilador() {
        this.sintatico=new AnalisadorSintatico();
        this.semantico=new AnalisadorSemantico();
    }
    
    public void compilar(String texto) throws Erro{
        sintatico.analizar(texto);
        //semantico.analizar();
    }
    
}
