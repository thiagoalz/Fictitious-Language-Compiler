/*
 * AnalisadorLexico.java
 *
 * Created on 10 de Setembro de 2005, 19:15
 *
 * To change this template, choose Tools | Options and locate the template
under
 * the Source Creation and Management node. Right-click the template and
choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package compilador;

import token.*;
import erros.*;
import java.util.ArrayList;
/**
 *
 * @author  20031000178
 */
public class AnalisadorLexico {
    private String texto,novoLexema;
    private int contLinha, contCaractere;
    private Erro erros;
    private ArrayList tabBoleano,tabReservadas,tabLogico;
    private Tradutor tradutor;
    
    /** Creates a new instance of AnalisadorLexico */
    public AnalisadorLexico(String texto,Erro erros){
        this(erros);
        this.texto=texto+'\n';
    }
    public AnalisadorLexico(Erro erros){
        this.texto="";
        this.contLinha=0;
        this.contCaractere=0;
        this.resetLexema();
        this.erros=erros;
        this.tradutor=new Tradutor();
        //criando tabelas
        tabBoleano=new ArrayList();
        tabBoleano.add("verdadeiro");
        tabBoleano.add("falso");
        
        
        tabLogico=new ArrayList();
        tabLogico.add("e");
        tabLogico.add("ou");
        
        
        tabReservadas=new ArrayList();
        tabReservadas.add("enquanto");
        tabReservadas.add("inicio");
        tabReservadas.add("fim");
        tabReservadas.add("se");
        tabReservadas.add("entao");
        tabReservadas.add("senao");
        tabReservadas.add("constante");
        tabReservadas.add("declare");
        tabReservadas.add("vetor");
        tabReservadas.add("inteiro");
        tabReservadas.add("caracter");
        tabReservadas.add("real");
        tabReservadas.add("logico");
        tabReservadas.add("faca");
        tabReservadas.add("de");
        tabReservadas.add("ate");
        tabReservadas.add("para");
        tabReservadas.add("repita");
        tabReservadas.add("nao");
        tabReservadas.add("escreva");
        tabReservadas.add("leia");
        tabReservadas.add("funcao");
        tabReservadas.add("procedimento");
    }
    
    public void setTexto(String texto){
        this.texto=texto+'\n';
        this.contLinha=0;
        this.contCaractere=0;
        this.resetLexema();
        tradutor.reset();
    }
    
    public boolean isFimArquivo(){
        boolean retorno=true;
        if(contCaractere<this.texto.length()-1){
            retorno=false;
        }
        return retorno;
    }
    private boolean isFimReal(){
        boolean retorno=true;
        if(contCaractere<this.texto.length()){
            retorno=false;
        }
        return retorno;
    }
    
    public String getTraducao(){
    	return tradutor.getTraducao();
    }
    
    public Token getNextToken(){
        Token novoToken=null;
        this.resetLexema();
        int estado=0;
        char caractereAtual;
        /*
         *While representa o Automato que identifica os tokens da linguagem
         *estados:
         *0->estado inicial
         *1/2/111->identificador
         *3/4->constante alfa-numerico
         *5/6->operador aritimetico
         *7/8/9->comentario
         *10/11/12->operador relacional
         *13/14->sinal
         *15/16/17/18/19->numeros
         */
        //while(novoToken==null && (!isFimArquivo() || estado==2|| estado==4|| estado==5|| estado==11 || estado==13|| estado==16|| estado==19)){
        while(novoToken==null && !isFimReal() ){
            caractereAtual=this.verCharAtual();
            switch (estado){
                case 0:
                    if(Character.isWhitespace(caractereAtual)){//estadoinicial
                    	tradutor.add(caractereAtual);
                        estado=0;
                        this.ignoraCharAtual();
                    }else if(Character.isLetter(caractereAtual)){//ï¿½ umidentificador
                        estado=1;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='\''){//ï¿½ uma contante Alfa-Numerica
                        estado=3;
                        this.ignoraCharAtual();
                    }else if(caractereAtual=='+' || caractereAtual=='-'|| caractereAtual=='*'){//ï¿½ um sinal (usar IN aquii conjunto)
                        estado=5;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='/'){//pode ser um operadoraritimetico ou comentario
                        estado=6;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='>'){//operador logico
                        estado=10;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='<'){//operador logico
                        estado=12;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='='){//operador logico
                        estado=11;
                        this.aceitaCharAtual();
                    }else if(caractereAtual==';'
                            || caractereAtual==','
                            || caractereAtual=='('
                            || caractereAtual==')'
                            || caractereAtual=='['
                            || caractereAtual==']'
                            || caractereAtual==':'){//Sinal mudar paraIN (conjunto)
                        estado=13;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='.'){
                        estado=14;
                        this.aceitaCharAtual();
                    }else if(Character.isDigit(caractereAtual)){
                        estado=15;
                        this.aceitaCharAtual();
                    }else{
                        reportarErro("Erro Lexico("+this.contLinha+","+this.contCaractere+")->Caractere nao reconhecido( "+caractereAtual+" )",this.contCaractere);
                        estado=0;
                    }
                    break;
                    
                case 1:
                    if(Character.isLetterOrDigit(caractereAtual) ){
                        estado=1;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='_'){
                        estado=111;
                        this.aceitaCharAtual();
                    }else{
                        estado=2;
                    }
                    break;
                    
                case 111:
                    if(Character.isLetterOrDigit(caractereAtual)){
                        estado=1;
                        this.aceitaCharAtual();
                    }else if(caractereAtual=='_'){
                    	reportarErro("Erro Lexico("+this.contLinha+","+this.contCaractere+")->permitido formar Identificadores com a Substring \"__\"",this.contCaractere);
                    	estado=1;
                        this.aceitaCharAtual();
                    }else{
                    	reportarErro("Erro Lexico("+this.contLinha+","+this.contCaractere+")->Identificadores nao podem ser terminados com \"_\"",this.contCaractere);
                    	estado=1;       
                    }
                    break;
                    
                case 2:
                    //Final
                	novoToken=this.verificaBoleano(novoLexema);
                    if(novoToken==null){
                        novoToken=this.verificaOperadorLogico(novoLexema);
                    }
                    if(novoToken==null){
                        novoToken=this.verificaPalavraReservada(novoLexema);
                    }
                    if(novoToken==null){
                    	novoToken=new Identificador(novoLexema,this.contLinha,this.contCaractere-novoLexema.length());
                    }
                    break;
                    
                case 3:
                    if(caractereAtual=='\''){
                    	estado=4;
                        this.ignoraCharAtual();                        
                    }else if(caractereAtual!='\n'&& caractereAtual!='\r'){
                    	estado=3;
                        this.aceitaCharAtual();
                    }else{
                    	reportarErro("Erro Lexico("+this.contLinha+","+this.contCaractere+")-> Caractere \" ' \" Esperado. Não Foi possivel Concluir a constante Alfa-Numerica",this.contCaractere);
                    	estado=4;
                    }
                    break;
                    
                case 4:
                    //final
                    novoToken=new AlfaNumerico(novoLexema,this.contLinha,this.contCaractere-novoLexema.length());
                    break;
                    
                case 5:
                    //final
                    novoToken=new OperadorAritimetico(novoLexema,this.contLinha,this.contCaractere-novoLexema.length());
                    break;
                    
                case 6:
                    if(caractereAtual=='/'){
                        this.resetLexema();
                        this.ignoraCharAtual();
                        estado=7;
                    }else if(caractereAtual=='*'){
                        this.resetLexema();
                        this.ignoraCharAtual();
                        estado=8;
                    }else{
                        estado=5;
                    }
                    break;
                    
                case 7:
                    if(caractereAtual=='\n'){
                        estado=0;
                    }else{
                        estado=7;
                    }
                    this.ignoraCharAtual();
                    break;
                    
                case 8:
                    if(caractereAtual=='*'){
                        estado=9;
                    }else{
                        estado=8;
                    }
                    this.ignoraCharAtual();
                    break;
                    
                case 9:
                    if(caractereAtual=='/'){
                        estado=0;
                    }else{
                        estado=8;
                    }
                    this.ignoraCharAtual();
                    break;
                    
                case 10:
                    if(caractereAtual=='='){
                        this.aceitaCharAtual();
                        estado=11;
                    }else{
                        estado=11;
                    }
                    break;
                    
                case 11:
                    //final
                    novoToken=new OperadorRelacional(novoLexema,this.contLinha,this.contCaractere-novoLexema.length());
                    break;
                    
                case 12:
                    if(caractereAtual=='=' || caractereAtual=='>' || caractereAtual=='-'){//usar INaqui (coinjunto)
                        this.aceitaCharAtual();
                        estado=11;
                    }else{
                        estado=11;
                    }
                    break;
                    
                case 13:
                    //final
                    novoToken=new Sinal(novoLexema,this.contLinha,this.contCaractere-novoLexema.length());
                    break;
                    
                case 14:
                    if(caractereAtual=='.' ){
                        this.aceitaCharAtual();
                        estado=13;
                    }else{
                        estado=13;
                    }
                    break;
                    
                case 15:
                    if(Character.isDigit(caractereAtual)){
                        this.aceitaCharAtual();
                        estado=15;
                    }else if(caractereAtual=='.'){
                        estado=17;
                    }else{
                        estado=16;
                    }
                    break;
                    
                case 16:
                    //final
                    novoToken=new NumeroInteiro(novoLexema,this.contLinha,this.contCaractere-novoLexema.length());
                    break;
                    
                case 17:
                    if( Character.isDigit(this.verProxChar()) ){//se depoisdo ponto vem um numero
                        this.aceitaCharAtual();//aceita o ponto
                        estado=18;
                    }else{
                        estado=16;
                    }
                    break;
                    
                case 18:
                    if(Character.isDigit(caractereAtual)){
                        this.aceitaCharAtual();
                        estado=18;
                    }else{
                        estado=19;
                    }
                    break;
                    
                case 19:
                    //final
                    novoToken=new NumeroReal(novoLexema,this.contLinha,this.contCaractere-novoLexema.length());
                    break;
            }
        }
        tradutor.add(novoToken);
        return novoToken;
    }
    
    public int getNumeroLinha(){
    	return this.contLinha;
    }
    
    public int getNumeroCaractere(){
    	return this.contCaractere;
    }
    
    public boolean existeErro(){
        return erros.temErro();
    }
    
    private void reportarErro(String novoErro,int caractere){
        if (novoErro.trim().length()!=0)
            erros.addErro(novoErro,caractere);
    }
    
    private Token verificaBoleano(String palavra){
        Token novoToken=null;
        palavra=palavra.toLowerCase();
        if(this.tabBoleano.contains(palavra)){
            novoToken=new Booleano(palavra,this.contLinha,this.contCaractere-novoLexema.length());
        }
        return novoToken;
    }
    
    private Token verificaPalavraReservada(String palavra){
        Token novoToken=null;
        palavra=palavra.toLowerCase();
        if(this.tabReservadas.contains(palavra)){
            novoToken=new PalavraReservada(palavra,this.contLinha,this.contCaractere-novoLexema.length());
        }
        return novoToken;
    }
    
    private Token verificaOperadorLogico(String palavra){
        Token novoToken=null;
        palavra=palavra.toLowerCase();
        if(this.tabLogico.contains(palavra)){
            novoToken=new OperadorLogico(palavra,this.contLinha,this.contCaractere-novoLexema.length());
        }
        return novoToken;
    }
    
    private char verCharAtual(){
        char retorno=' ';
        try{
            retorno=this.texto.charAt(this.contCaractere);
        }catch(StringIndexOutOfBoundsException e){
            //erro nao pode pegar mais
        }
        return retorno;
    }
    
    private char verProxChar(){
        char retorno=' ';
        try{
            retorno=this.texto.charAt(this.contCaractere+1);
        }catch(StringIndexOutOfBoundsException e){
            //erro nao pode pegar mais
        }
        return retorno;
    }
    
    private void resetLexema(){
        this.novoLexema="";
    }
    
    private char aceitaCharAtual(){
        char atual=this.verCharAtual();
        this.novoLexema+=atual;
        this.contCaractere++;
        if(atual=='\n'){
            this.contLinha++;
        }
        return atual;
    }
    
    private char ignoraCharAtual(){
        char atual=this.verCharAtual();
        this.contCaractere++;
        if(atual=='\n'){
            this.contLinha++;
        }
        return atual;
    }
    
}

