package Scanner;

import Exceptions.LexicalException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Scanner {

    private char[] content;
    private int estado;
    private int pos;
    private int line;
    private int column;

    public Scanner(String filename) {
        try {
            line = 1;
            column = 0;
            String txtConteudo;
            txtConteudo = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            System.out.println("DEBUG --------");
            System.out.println(txtConteudo += "  ");
            System.out.println("--------------");
            content = txtConteudo.toCharArray();
            pos = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Token nextToken() throws LexicalException {
        char currentChar;
        Token token = new Token();
        String buffer = "";
        if (isEOF()) {
            return null;
        }
        estado = 0;
        while (true) {
            currentChar = nextChar();
            column++;
            switch (estado) {
                case 0:
                    if (isLetra(currentChar)) {
                        buffer += currentChar;
                        estado = 1;
                    } else if (isDigit(currentChar)) {
                        estado = 3;
                        buffer += currentChar;
                    } else if (isSpace(currentChar)) {
                        estado = 0;
                    } else if (isOperator_R(currentChar)) {
                        buffer += currentChar;
                        estado = 12;
                    } else if (isOperator_A(currentChar)) {
                        buffer += currentChar;
                        estado = 10;
                    } else if (isChar(currentChar)) {
                        buffer += currentChar;
                        estado = 6;
                    } else if (isIgual(currentChar)) {
                        buffer += currentChar;
                        estado = 13;
                    } else if (isCaracterEspecial(currentChar)) {
                        buffer += currentChar;
                        estado = 14;
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Simbolo desconhecido");
                    }
                    break;
                case 1:
                    if (isLetra(currentChar) || isDigit(currentChar)) {
                        estado = 1;
                        buffer += currentChar;
                    } else if (isSpace(currentChar) || isOperator_R(currentChar)
                            || isOperator_A(currentChar) || isIgual(currentChar)
                            || isCaracterEspecial(currentChar) || isEOF(currentChar)) {

                        estado = 2;
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Váriavel mal formada");
                    }
                    break;
                case 2:
                    back();
                    back();
                    if (isPalavrasReservadas(buffer)) {
                        //palavra reservada
                        return setando(token, 5, buffer);
                    } else {
                        //identificador
                        return setando(token, 0, buffer);
                    }
                case 3:
                    if (isDigit(currentChar)) {
                        estado = 3;
                        buffer += currentChar;
                    } else if (isFloat(currentChar)) {
                        estado = 8;
                        buffer += currentChar;
                    } else if (!isLetra(currentChar)) {
                        estado = 4;
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Número mal formado");
                    }
                    break;
                case 4:
                    back();
                    back();
                    //numero
                    return setando(token, 1, buffer);
                case 5:
                    buffer += currentChar;
                    //relacional
                    return setando(token, 10, buffer);
                case 6:
                    if (isLetra(currentChar) || isDigit(currentChar)
                            || isSpace(currentChar) || isOperator_R(currentChar)
                            || isOperator_A(currentChar) || isIgual(currentChar)
                            || isCaracterEspecial(currentChar) || isEOF(currentChar)) {
                        buffer += currentChar;
                        estado = 7;
                    } else if (isChar(currentChar)) {
                        buffer += currentChar;
                        //char
                        return setando(token, 3, buffer);
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Char mal formada");
                    }
                    break;
                case 7:
                    if (isChar(currentChar)) {
                        buffer += currentChar;
                        //char
                        return setando(token, 3, buffer);
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Char mal formada");
                    }
                case 8:
                    if (isDigit(currentChar)) {
                        buffer += currentChar;
                        estado = 9;
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Float mal formado");
                    }
                    break;
                case 9:
                    if (isDigit(currentChar)) {
                        estado = 9;
                        buffer += currentChar;
                    } else if (isSpace(currentChar) || isOperator_R(currentChar)
                            || isOperator_A(currentChar) || isIgual(currentChar)
                            || isCaracterEspecial(currentChar) || isEOF(currentChar)) {
                        back();
                        //float
                        return setando(token, 4, buffer);
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Float mal formado");
                    }
                    break;
                case 10:
                    if (isSpace(currentChar) || isEOF(currentChar)
                            || isDigit(currentChar) || isLetra(currentChar)
                            || isChar(currentChar)) {
                        back();
                        if (buffer.compareTo("+") == 0) {
                            //aritmetico mais
                            return setando(token, 6, buffer);
                        } else if (buffer.compareTo("-") == 0) {
                            //aritmetico menos
                            return setando(token, 7, buffer);
                        } else if (buffer.compareTo("*") == 0) {
                            //aritmetico multiplicação
                            return setando(token, 9, buffer);
                        } else if (buffer.compareTo("/") == 0) {
                            //aritmetico divisão
                            return setando(token, 10, buffer);
                        } else {
                            //aritmetico igual
                            return setando(token, 8, buffer);
                        }
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Operador Aritmetico mal formado");
                    }

                case 11:
                    if (isIgual(currentChar)) {
                        estado = 12;
                        buffer += currentChar;
                    } else {
                        back();
                        estado = 10;
                    }
                    break;
                case 12:
                    if (isSpace(currentChar) || isEOF(currentChar)
                            || isDigit(currentChar) || isLetra(currentChar)
                            || isChar(currentChar)) {
                        back();
                        if (buffer.compareTo(">") == 0) {
                            //relacional maior
                            return setando(token, 11, buffer);
                        } else if (buffer.compareTo("<") == 0) {
                            //relacional menor
                            return setando(token, 12, buffer);
                        } else if (buffer.compareTo(">=") == 0) {
                            //relacional maior igual
                            return setando(token, 13, buffer);
                        } else if (buffer.compareTo("<=") == 0) {
                            //relacional menor igual
                            return setando(token, 14, buffer);
                        } else if (buffer.compareTo("!=") == 0) {
                            //relacional diferente
                            return setando(token, 15, buffer);
                        } else {
                            //relacional igual
                            return setando(token, 16, buffer);
                        }
                    } else if (isIgual(currentChar)) {
                        estado = 12;
                    } else {
                        buffer += currentChar;
                        System.out.print("\"" + buffer + "\" ");
                        throw new LexicalException("Operador relacional mal formado");
                    }

                case 13:
                    if (isIgual(currentChar)) {
                        estado = 12;
                        buffer += currentChar;
                    } else {
                        back();
                        estado = 10;
                    }
                    break;
                case 14:

                    back();
                    if (buffer.compareTo("(") == 0) {
                        //caracter especial abre parentese
                        return setando(token, 17, buffer);
                    } else if (buffer.compareTo(")") == 0) {
                        //caracter especial fecha parentese
                        return setando(token, 18, buffer);
                    } else if (buffer.compareTo("{") == 0) {
                        //caracter especial abre chaves
                        return setando(token, 19, buffer);
                    } else if (buffer.compareTo("}") == 0) {                      
                        //caracter especial fecha chaves
                        return setando(token, 20, buffer);
                    } else if (buffer.compareTo(";") == 0) {                      
                        //caracter especial ponto e virgula
                        return setando(token, 21, buffer);
                    } else {                        
                        //caracter especial virgula
                        return setando(token, 22, buffer);
                    }

            }
        }

    }

    private Token setando(Token token, int a, String term) {
        token = new Token();
        token.setType(a);
        token.setText(term);
        token.setLine(line);
        token.setColumn(column - term.length());
        return token;
    }


    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isChar(char c) {
        return (c == '\'');
    }

    private boolean isFloat(char c) {
        return (c == '.');
    }

    private boolean isPalavrasReservadas(String c) {
        return c.compareTo("main") == 0 || c.compareTo("if") == 0
                || c.compareTo("else") == 0 || c.compareTo("while") == 0
                || c.compareTo("do") == 0 || c.compareTo("for") == 0
                || c.compareTo("float") == 0 || c.compareTo("char") == 0
                || c.compareTo("int") == 0;
    }

    private boolean isOperator_R(char c) {
        return c == '>' || c == '<' || c == '!';
    }

    private boolean isOperator_A(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isIgual(char c) {
        return c == '=';
    }

    private boolean isCaracterEspecial(char c) {
        return c == ')' || c == '(' || c == '{' || c == '}' || c == ';' || c == ',';
    }

    private boolean isSpace(char c) {
        if (c == '\n') {
            line++;
            column = 0;
        }
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private char nextChar() {
        return content[pos++];
    }

    private boolean isEOF() {
        return pos == content.length;
    }

    private boolean isEOF(char c) {
        return c == '\0';
    }

    private void back() {
        pos--;
    }

}
