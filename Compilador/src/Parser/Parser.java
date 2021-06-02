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
import java.util.LinkedList;

/**
 *
 * @author Igor Medeiros
 */
public class Parser {

    private Scanner scanner; // analisador léxico
    private Token token;   // o token atual
    private int count = 0;
    //semantico
    private int blocoCount = 0;
    private LinkedList<Variable> Variables = new LinkedList<Variable>();
    Variable aux;


    /* o Parser recebe o analisador léxico como parâmetro no construtor
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

    }

    private void decl_var() throws LexicalException {
        aux = new Variable();
        token = scanner.nextToken();
        if (token.getType() == Token.TK_PALAVRA_RESERVADA) {
            Tipo();
            token = scanner.nextToken();
            ID_CriacaoDeVariavel();
            token = scanner.nextToken();
            PV();
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

    private void ID_CriacaoDeVariavel() throws LexicalException {
        System.out.println(token.getText());
        if (token.getType() != Token.TK_IDENTIFIER) {
            throw new SyntaxException(" Identifier Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
        //semantico regra de variavel com mesmo nome

        aux.setName(token.getText());
        aux.setBloco(blocoCount);
        System.out.println(aux);
        System.out.println(Variables.contains(aux));
        if (Variables.contains(aux)) {
            throw new SemanticException("Nome de variavel (" + token.getText() + ") já escolhido at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        } else {
            Variables.add(aux);
            System.out.println(Variables);
        }
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
        expr_arit();
        OPR();
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
            //semantico variavel não existente 
            VariavelExiste();
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
        token = scanner.nextToken();
        termo();
        expr_arit_l();
    }

    private void expr_arit_l() throws LexicalException {
        switch (token.getType()) {
            case Token.TK_OPERATOR_ARITMETICO_MAIS:
                Soma();
                expr_arit();
                break;
            case Token.TK_OPERATOR_ARITMETICO_MENOS:
                Sub();
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
                token = scanner.nextToken();
                termo();
                break;
            case Token.TK_OPERATOR_ARITMETICO_DIVISAO:
                Div();
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
                //Semantico variavel existente
                VariavelExiste();
                break;
            case Token.TK_FLOAT:
                Float();
                break;
            case Token.TK_NUMBER:
                Int();
                break;
            case Token.TK_CHAR:
                Char();
                break;
            default:
                throw new SyntaxException(" Fator or Aritmetic Expression Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
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

    private void While() throws LexicalException {

        if (token.getType() != Token.TK_PALAVRA_RESERVADA || token.getText().compareTo("while") != 0) {
            throw new SyntaxException(" \"while\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
        token = scanner.nextToken();
        AP();
        expr_relacional();
        FP();
        token = scanner.nextToken();
        comando();
    }

    private void IF() throws LexicalException {
        if (token.getType() != Token.TK_PALAVRA_RESERVADA || token.getText().compareTo("if") != 0) {
            throw new SyntaxException(" \"if\" Expected, found " + Token.TK_TEXT[token.getType()] + " (" + token.getText() + ")  at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
        }
        token = scanner.nextToken();
        AP();
        expr_relacional();
        FP();
        token = scanner.nextToken();
        comando();
        if (token.getType() == Token.TK_PALAVRA_RESERVADA && token.getText().compareTo("else") == 0) {
            token = scanner.nextToken();
            comando();
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
    
    //semantico saber se a variavel existe
    private void VariavelExiste() {
        for (int i = 0; i <= Variables.toArray().length; i = i + 1) {
            if (i == Variables.toArray().length) {
                throw new SemanticException("Variavel não existe (" + token.getText() + ") at LINE " + token.getLine() + " and COLUMN " + token.getColumn());
            }
            aux = (Variable) Variables.toArray()[i];
            if (aux.getName().compareTo(token.getText()) == 0) {
                break;
            }
        }
    }
}
