/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import DataStructures.Variable;
import Exceptions.LexicalException;
import Exceptions.SemanticException;
import Exceptions.SyntaxException;
import Scanner.Scanner;
import Scanner.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Igor Medeiros
 */
public class Parser {

    private Scanner scanner; // analisador lÃ©xico
    private Token token;   // o token atual
    private int countIF = 1;
    //semantico
    private int blocoCount = 0;
    private LinkedList<Variable> Variables = new LinkedList<Variable>();
    Variable aux;
    //gerador de codigo
    String valorAux = new String();
    String valorAuxIF = new String();
    private int countT = 0;
    private String id;

    /* o Parser recebe o analisador lÃ©xico como parÃ¢metro no construtor
	 * pois a cada procedimento invoca-o sob demanda
     */
    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public void E() throws InterruptedException, LexicalException {
        prog_int();
        token = scanner.nextToken();
        bloco();

    }

    public void prog_main() throws InterruptedException, LexicalException {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_PALAVRA_RESERVADA || token.getText().compareTo("main") != 0) {
            throw new SyntaxException(" \"main\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }

    }

    private void prog_int() throws LexicalException, InterruptedException {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_PALAVRA_RESERVADA || token.getText().compareTo("int") != 0) {
            throw new SyntaxException(" \"int\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
        prog_main();
        token = scanner.nextToken();
        AP();
        token = scanner.nextToken();
        FP();
    }

    private void AP() throws LexicalException {
        if (token.getType() != Token.TK_CARACTER_ESPECIAL_ABRE_PARENTESE) {
            throw new SyntaxException(" \"(\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }

    }

    private void FP() throws LexicalException {
        if (token.getType() != Token.TK_CARACTER_ESPECIAL_FECHA_PARENTESE) {
            throw new SyntaxException(" \")\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void AC() throws LexicalException {

        if (token.getType() != Token.TK_CARACTER_ESPECIAL_ABRE_CHAVES) {
            throw new SyntaxException(" \"{\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void FC() throws LexicalException {
        if (token.getType() != Token.TK_CARACTER_ESPECIAL_FECHA_CHAVES) {
            throw new SyntaxException(" \"}\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void bloco() throws LexicalException {
        blocoCount += 1;
        AC();
        decl_var();
        comando();
        FC();
        if (this.token != null) {
            token = scanner.nextToken();
        }

    }

    private void decl_var() throws LexicalException {
        aux = new Variable();
        token = scanner.nextToken();
        if (token.getType() == Token.TK_PALAVRA_RESERVADA) {
            Tipo();
            token = scanner.nextToken();
            ID();
            VariavelMesmoNome();
            token = scanner.nextToken();
            PV();
            System.out.println(aux);
            decl_var();
        }
    }

    private void comando() throws LexicalException {
        if ((token.getType() == Token.TK_PALAVRA_RESERVADA && (token.getText().compareTo("if") == 0 || token.getText().compareTo("while") == 0))
                || (token.getType() == Token.TK_IDENTIFIER)
                || (token.getType() == Token.TK_CARACTER_ESPECIAL_ABRE_CHAVES)) {
            if (token.getType() == Token.TK_IDENTIFIER || token.getType() == Token.TK_CARACTER_ESPECIAL_ABRE_CHAVES) {
                comando_basico();
            } else if (token.getText().compareTo("while") == 0) {
                interacao();
            } else {
                IF();
            }
            comando();

        }
    }

    private void Tipo() throws LexicalException {

        if (token.getType() != Token.TK_PALAVRA_RESERVADA || (token.getText().compareTo("int") != 0 && token.getText().compareTo("float") != 0 && token.getText().compareTo("char") != 0)) {
            throw new SyntaxException(" Type Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
        //semantico adiquirindo tipo
        aux.setType(token.getText());
    }

    private void PV() throws LexicalException {
        if (token.getType() != Token.TK_CARACTER_ESPECIAL_PONTO_VIRGULA) {
            throw new SyntaxException(" \";\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void comando_basico() throws LexicalException {
        if (token.getType() == Token.TK_IDENTIFIER) {
            atribuicao();
        } else {
            bloco();
        }
    }

    private void interacao() throws LexicalException {
        While();
    }

    private void expr_relacional() throws LexicalException {
        valorAuxIF += token.getText();
        expr_arit();
        OPR();
        valorAuxIF += token.getText();
        expr_arit();
    }

    private void OPR() {
        if (token.getType() != Token.TK_OPERATOR_RELACIONAL_DIFERENTE
                && token.getType() != Token.TK_OPERATOR_RELACIONAL_IGUAL_IGUAL
                && token.getType() != Token.TK_OPERATOR_RELACIONAL_MAIOR
                && token.getType() != Token.TK_OPERATOR_RELACIONAL_MENOR
                && token.getType() != Token.TK_OPERATOR_RELACIONAL_MAIOR_IGUAL
                && token.getType() != Token.TK_OPERATOR_RELACIONAL_MENOR_IGUAL) {
            throw new SyntaxException(" Relacional Operator Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void atribuicao() throws LexicalException {
        aux = new Variable();
        if (token.getType() == Token.TK_IDENTIFIER) {
            ID();
            //semantico variavel nÃ£o existente 
            aux = VariavelExiste();
            IA();
            expr_arit();
            PV();
            token = scanner.nextToken();
        }
    }

    private void ID() throws LexicalException {
        if (token.getType() != Token.TK_IDENTIFIER) {
            throw new SyntaxException(" Identifier Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }

    }

    private void IA() throws LexicalException {
        token = scanner.nextToken();
        if (token.getType() != Token.TK_OPERATOR_ARITMETICO_IGUAL) {
            throw new SyntaxException(" \"=\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void expr_arit() throws LexicalException {
        //valor aux Ã© utilizado na geraÃ§Ã£o de codigo
        token = scanner.nextToken();
        termo();
        expr_arit_l();
        gerador(valorAux);
        valorAux = new String();
    }

    private void expr_arit_l() throws LexicalException {
        switch (token.getType()) {
            case Token.TK_OPERATOR_ARITMETICO_MAIS:
                Soma();
                valorAux += token.getText();
                valorAuxIF += token.getText();
                expr_arit();
                break;
            case Token.TK_OPERATOR_ARITMETICO_MENOS:
                Sub();
                valorAux += token.getText();
                valorAuxIF += token.getText();
                expr_arit();
                break;
            default:
        }
    }

    private void termo() throws LexicalException {

        Fator();
        termo_l();
    }

    private void termo_l() throws LexicalException {
        switch (token.getType()) {
            case Token.TK_OPERATOR_ARITMETICO_MULTIPLICACAO:
                Multi();
                valorAux += token.getText();
                token = scanner.nextToken();
                termo();
                break;
            case Token.TK_OPERATOR_ARITMETICO_DIVISAO:
                Div();
                valorAux += token.getText();
                token = scanner.nextToken();
                termo();
                break;
            default:
        }
    }

    private void Fator() throws LexicalException {

        switch (token.getType()) {
            case Token.TK_CARACTER_ESPECIAL_ABRE_PARENTESE:
                AP();
                expr_arit();
                FP();
                break;
            case Token.TK_IDENTIFIER:
                ID();
                //Semantico mesmo tipo
                MesmoTipoOperacao(VariavelExiste());
                break;
            case Token.TK_FLOAT:
                Float();
                MesmoTipo();
                break;
            case Token.TK_NUMBER:
                Int();
                MesmoTipo();
                break;
            case Token.TK_CHAR:
                Char();
                MesmoTipo();
                break;
            default:
                throw new SyntaxException(" Fator or Aritmetic Expression Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }

        valorAux += token.getText();
        valorAuxIF += token.getText();
        token = scanner.nextToken();
    }

    private void Multi() {
        if (token.getType() != Token.TK_OPERATOR_ARITMETICO_MULTIPLICACAO) {
            throw new SyntaxException(" \"*\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void Div() {
        if (token.getType() != Token.TK_OPERATOR_ARITMETICO_DIVISAO) {
            throw new SyntaxException(" \"/\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void Soma() {
        if (token.getType() != Token.TK_OPERATOR_ARITMETICO_MAIS) {
            throw new SyntaxException(" \"+\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void Sub() {
        if (token.getType() != Token.TK_OPERATOR_ARITMETICO_MENOS) {
            throw new SyntaxException(" \"-\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void While() throws LexicalException {

        if (token.getType() != Token.TK_PALAVRA_RESERVADA || token.getText().compareTo("while") != 0) {
            throw new SyntaxException(" \"while\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
        token = scanner.nextToken();
        AP();
        expr_relacional();
        FP();
        token = scanner.nextToken();
        bloco();
    }

    private void IF() throws LexicalException {

        valorAuxIF = new String();
        if (token.getType() != Token.TK_PALAVRA_RESERVADA || token.getText().compareTo("if") != 0) {
            throw new SyntaxException(" \"if\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
        token = scanner.nextToken();
        AP();
        expr_relacional();
        valorAuxIF += token.getText();
        FP();
        token = scanner.nextToken();
        mipsIfT(valorAuxIF);
        bloco();
        mipsComandoLabel();
        if (token.getType() == Token.TK_PALAVRA_RESERVADA && token.getText().compareTo("else") == 0) {
            token = scanner.nextToken();
            comando();
        }
    }

    private void mipsIfT(String ER) {
        System.out.println("t" + this.countT + " = " + ER);
        mipsIF();
    }

    private void mipsIF() {
        System.out.println("if_false t" + +this.countT + " goTo " + "L" + countIF);

    }

    private void mipsComandoLabel() {
        System.out.println("L" + countIF + ":");
        countIF++;
    }

    private void Float() {
        if (token.getType() != Token.TK_FLOAT) {
            throw new SyntaxException(" float Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void Int() {
        if (token.getType() != Token.TK_NUMBER) {
            throw new SyntaxException(" int Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    private void Char() {
        if (token.getType() != Token.TK_CHAR) {
            throw new SyntaxException(" char Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

/////////////essa parte so as operaÃ§Ãµes semanticas///////////////
    //semantico saber se a variavel existe
    private Variable VariavelExiste() {
        Variable a;
        for (int i = 0; i < Variables.toArray().length; i = i + 1) {
            a = (Variable) Variables.toArray()[i];
            if (a.getName().compareTo(token.getText()) == 0) {
                return a;
            }
        }
        throw new SemanticException("Variavel nÃ£o existe (" + token.getText() + ") at LINE " + token.getLine() + " and COLUMN " + token.getColumn());

    }

    //semantico regra de variavel com mesmo nome
    private void VariavelMesmoNome() {
        aux.setName(token.getText());
        aux.setBloco(blocoCount);
        if (Variables.contains(aux)) {
            throw new SemanticException("Nome de variavel (" + token.getText() + ") jÃ¡ escolhido at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        } else {
            Variables.add(aux);
        }
    }

    //semantico para tipo de variavel dentro de operaÃ§Ã£o
    private void MesmoTipoOperacao(Variable a) {
        if (aux.getTypeID() != a.getTypeID() && !(aux.getTypeID() == 4 && a.getTypeID() == 1)) {
            throw new SemanticException("AtribuiÃ§Ã£o errada. Tipo (" + aux.getType().toUpperCase() + ") esperado, encontrado:(" + a.getType() + ") at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    //semantico para entrar int em float
    private void MesmoTipo() {
        if (aux.getTypeID() != token.getType() && !(aux.getTypeID() == 4 && token.getType() == 1)) {
            throw new SemanticException("AtribuiÃ§Ã£o errada. Tipo (" + aux.getType().toUpperCase() + ") esperado, encontrado:(" + Token.TK_TEXT[token.getType()] + ") at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
    }

    //////////////////////essa parte Ã© do gerador de codigo//////////////////////////////////////
    private void gerador(String a) {
        int i, j, opc = 0;
        char token;
        boolean[] testeDeExpr;
        String[][] operadores = new String[10][2];
        String expr = a, temp;
        testeDeExpr = new boolean[expr.length()];
        for (i = 0; i < testeDeExpr.length; i++) {
            testeDeExpr[i] = false;
        }
        for (i = 0; i < expr.length(); i++) {
            token = expr.charAt(i);
            for (j = 0; j < OrdemAritmetica.length; j++) {
                if (token == OrdemAritmetica[j][0]) {
                    operadores[opc][0] = token + "";
                    operadores[opc][1] = i + "";
                    opc++;
                    break;
                }
            }
        }
        //Ordem
        for (i = opc - 1; i >= 0; i--) {
            for (j = 0; j < i; j++) {
                if (Ordem(operadores[j][0]) > Ordem(operadores[j + 1][0])) {
                    temp = operadores[j][0];
                    operadores[j][0] = operadores[j + 1][0];
                    operadores[j + 1][0] = temp;
                    temp = operadores[j][1];
                    operadores[j][1] = operadores[j + 1][1];
                    operadores[j + 1][1] = temp;
                }
            }
        }

        for (i = 0; i < opc; i++) {
            j = Integer.parseInt(operadores[i][1] + "");
            String op1 = "", op2 = "";
            if (testeDeExpr[j - 1] == true) {
                if (Ordem(operadores[i - 1][0]) == Ordem(operadores[i][0])) {
                    op1 = "t" + i;
                } else {
                    for (int x = 0; x < opc; x++) {
                        if ((j - 2) == Integer.parseInt(operadores[x][1])) {
                            op1 = "t" + (x + 1) + "";
                        }
                    }
                }
            } else {
                op1 = expr.charAt(j - 1) + "";
            }
            if (testeDeExpr[j + 1] == true) {
                for (int x = 0; x < opc; x++) {
                    if ((j + 2) == Integer.parseInt(operadores[x][1])) {
                        op2 = "t" + (i) + "";
                    }
                }
            } else {
                op2 = expr.charAt(j + 1) + "";
            }
            this.id = "t" + (i + 1);
            System.out.println(this.id + " = " + op1 + operadores[i][0] + op2);
            testeDeExpr[j] = testeDeExpr[j - 1] = testeDeExpr[j + 1] = true;
        }
        countT++;
    }

    private final char[][] OrdemAritmetica = {
        {'/', '1'},
        {'*', '1'},
        {'+', '2'},
        {'-', '2'}
    };

    private int Ordem(String t) {
        char token = t.charAt(0);
        for (int i = 0; i < OrdemAritmetica.length; i++) {
            if (token == OrdemAritmetica[i][0]) {
                return Integer.parseInt(OrdemAritmetica[i][1] + "");
            }
        }
        return -1;
    }
}
