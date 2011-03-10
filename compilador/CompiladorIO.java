/*
 * IO.java
 *
 * Created on 23 de Setembro de 2005, 12:31
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package compilador;

import javax.swing.JFileChooser;
import java.io.*;
import javax.swing.JOptionPane;

/**
 *
 * @author John Holiver
 */
public class CompiladorIO {
    private JFileChooser chooser;
    private File file;
    /** Creates a new instance of IO */
    public CompiladorIO() {
        this.chooser = new JFileChooser();
        this.file=null;
        FiltroTxt filter = new FiltroTxt(new String("txt"), "Arquivos de texto");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
    }
    
    public void resetFile(){
        this.file=null;
    }
    
    public void saveAsFile(String texto) throws Exception{
        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            this.file = chooser.getSelectedFile();
            if (!file.getName().equals(null)) {
                if(file.createNewFile()){
                    //this.addExtToFile();
                    gravar(texto);
                } else {
                    returnVal = JOptionPane.showConfirmDialog(null,"Deseja sobrescrever o arquivo?","Salvar",JOptionPane.YES_NO_OPTION);
                    if(returnVal == JOptionPane.YES_OPTION){
                        gravar(texto);
                    } else {
                        throw new Exception("Save command cancelled by user.");
                    }
                }
            }
        } else {
            throw new Exception("Save command cancelled by user.");
        }
    }
    
    public void saveFile(String texto) throws Exception{
        if(file!=null){
            gravar(texto);
        }else{
            saveAsFile(texto);
        }
    }
    
    private void gravar(String texto) throws IOException{
        BufferedWriter BW = new BufferedWriter(new FileWriter(file));
        BW.write(texto);// escreve o texto
        BW.flush();// Limpa o Buffer
        BW.close();
        System.out.println("Saving: " + file.getName() + ";");
    }
    
    public String openFile() throws Exception{
        String texto = "";
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.file = chooser.getSelectedFile();
            BufferedReader BR = new BufferedReader(new FileReader(file));
            char charAtual;
            int numChar=BR.read();
            while(numChar!=-1){
                texto+=(char)numChar;
                numChar=BR.read();
            }
            BR.close();
            System.out.println("Opening: " + file.getName() + ";");
        } else {
            throw new Exception("Open command cancelled by user.");
        }
        return texto;
    }
    
    private void addExtToFile(){
        String extArq = "";
        String nomeArq = this.file.getName();
        if(nomeArq.lastIndexOf(".")!=-1){
            extArq = nomeArq.substring(nomeArq.lastIndexOf(".")+1);
        }
        System.out.println(extArq);
        extArq = extArq.toLowerCase();
        if (!extArq.equals("txt")){
            String path;
            if( file.getPath().lastIndexOf("\\")!=-1){
                path = file.getPath().substring(0, file.getPath().lastIndexOf("\\"))+"\\";
                if(nomeArq.lastIndexOf(".")!=-1){
                    path+=nomeArq.substring(0, nomeArq.lastIndexOf(".")-1);
                } else {
                    path+=nomeArq;
                }
                path+=".txt";
                System.out.println(path);
                if(file.renameTo(new File(path))){
                    System.out.println("Deu certo!");
                } else {
                    System.out.println("Deu não!");
                }
            }
        }
    }
}
