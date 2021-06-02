package Scanner;

public class Token {

    public static final int TK_IDENTIFIER = 0;
    public static final int TK_NUMBER = 1;
    public static final int TK_ASSIGN = 2;
    public static final int TK_CHAR = 3;
    public static final int TK_FLOAT = 4;
    public static final int TK_PALAVRA_RESERVADA = 5;

    public static final int TK_OPERATOR_ARITMETICO_MAIS = 6;
    public static final int TK_OPERATOR_ARITMETICO_MENOS = 7;
    public static final int TK_OPERATOR_ARITMETICO_IGUAL = 8;
    public static final int TK_OPERATOR_ARITMETICO_MULTIPLICACAO = 9;
    public static final int TK_OPERATOR_ARITMETICO_DIVISAO = 10;

    public static final int TK_OPERATOR_RELACIONAL_MAIOR = 11;
    public static final int TK_OPERATOR_RELACIONAL_MENOR = 12;
    public static final int TK_OPERATOR_RELACIONAL_MAIOR_IGUAL = 13;
    public static final int TK_OPERATOR_RELACIONAL_MENOR_IGUAL = 14;
    public static final int TK_OPERATOR_RELACIONAL_DIFERENTE = 15;
    public static final int TK_OPERATOR_RELACIONAL_IGUAL_IGUAL = 16;

    public static final int TK_CARACTER_ESPECIAL_ABRE_PARENTESE = 17;
    public static final int TK_CARACTER_ESPECIAL_FECHA_PARENTESE = 18;
    public static final int TK_CARACTER_ESPECIAL_ABRE_CHAVES = 19;
    public static final int TK_CARACTER_ESPECIAL_FECHA_CHAVES = 20;
    public static final int TK_CARACTER_ESPECIAL_PONTO_VIRGULA = 21;
    public static final int TK_CARACTER_ESPECIAL_VIRGULA = 22;
    
    public static final String TK_TEXT[] = {
        "IDENTIFIER", "NUMBER", "ASSIGN", "CHAR", "FLOAT", "PALAVRA_RESERVADA", "OPERATOR_ARITMETICO_MAIS","OPERATOR_ARITMETICO_MENOS",
        "OPERATOR_ARITMETICO_IGUAL","OPERATOR_ARITMETICO_MULTIPLICACAO","OPERATOR_ARITMETICO_DIVISAO","OPERATOR_RELACIONAL_MAIOR","OPERATOR_RELACIONAL_MENOR",
        "OPERATOR_RELACIONAL_MAIOR_IGUAL","OPERATOR_RELACIONAL_MENOR_IGUAL","OPERATOR_RELACIONAL_DIFERENTE","OPERATOR_RELACIONAL_IGUAL_IGUAL",
        "CARACTER_ESPECIAL_ABRE_PARENTESE","CARACTER_ESPECIAL_FECHA_PARENTESE", "CARACTER_ESPECIAL_ABRE_CHAVES", "CARACTER_ESPECIAL_FECHA_CHAVES",
        "CARACTER_ESPECIAL_PONTO_VIRGULA", "CARACTER_ESPECIAL_VIRGULA"
    };
    
    private int type;
    private String text;
    private int line;
    private int column;

    public Token(int type, String text) {
        super();
        this.type = type;
        this.text = text;

    }

    public Token() {
        super();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token{" + "type = " + type + ", text = " + text + " }";
    }

}
