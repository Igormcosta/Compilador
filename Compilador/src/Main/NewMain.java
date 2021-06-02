/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Scanner.Scanner;
import Exceptions.LexicalException;
import Exceptions.SemanticException;
import Exceptions.SyntaxException;
import Parser.Parser;
import java.io.File;

/**
 *
 * @author Igor Medeiros
 */
public class NewMain {

    public static final String RED_BOLD = "\033[1;31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws InterruptedException {
        File arq = new File("D:\\Documentos\\Compilador\\src\\Main\\blabla.txt");
        if (!arq.exists()) {
            arq.mkdir();

        }

        try {
            Scanner sc = new Scanner(arq.getPath());
            Parser pa = new Parser(sc);
            pa.E();
            System.out.println("Compilation Sucessful");

        } catch (LexicalException ex) {
            System.out.println(RED_BOLD + "Lexical Error : " + ex.getMessage() + ANSI_RESET);
        } catch (SyntaxException ex) {
            System.out.println(RED_BOLD + "Syntax Error :" + ex.getMessage() + ANSI_RESET);
        } catch (SemanticException ex) {
            System.out.println(RED_BOLD + "Semantic Error :" + ex.getMessage() + ANSI_RESET);
            
        } catch (InterruptedException ex) {
            System.out.println("FIM");
        }
    }

}
