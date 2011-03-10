/*
 * Erro.java
 *
 * Created on 06 de Setembro de 2005, 19:39
 */

package erros;

import java.util.ArrayList;

/**
 *
 * @author  20031000178
 */
public class Erro extends Exception{
    protected String mensagem;
    protected ArrayList erros;
    /** Creates a new instance of Erro */
    public Erro(String erro) {
        mensagem=erro;
        erros=new ArrayList();
    }
    
    public void addErro(String novoErro,int local){
    	mensagem=mensagem+novoErro + "\n";
    	erros.add(local+"");
    }
    
    public String getMessage(){
    	return mensagem;
    }
    
    public boolean temErro(){
    	boolean retorno=false;
    	if(getMessage().length()>1){
    		retorno=true;
    	}
    	return retorno;
    }
    
    public int getQtdErros(){
    	erros.trimToSize();
    	return erros.size();
    }
    
    public int getLocal(int pos){
    	String aux=(String)erros.get(pos);
    	int retorno=Integer.parseInt(aux);
    	return retorno;
    }
    
    public void limpaErros(){
    	mensagem="";
    	erros=new ArrayList();
    }

}
