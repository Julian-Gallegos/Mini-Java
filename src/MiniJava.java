import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import SemanticsAndTypes.GeneratorVisitor;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java.io.*;
import java.util.*;


public class MiniJava {
    public static void main(String[] args) {
        try {
            // create a scanner on the input file
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            File filePath = new File(args[args.length - 1]); 
            InputStream inputStream = new FileInputStream(filePath); 
            Reader in = new BufferedReader(new InputStreamReader(inputStream));
            scanner s = new scanner(in, sf);
            parser p = new parser(s, sf);
            Symbol root = p.parse();

            if (args.length > 2) {
                System.exit(1); 
            }
            String option = args[0];
            if (option.equals("-T")) {
                Program program = (Program) root.value;
                GeneratorVisitor gv = new GeneratorVisitor(program);
                System.out.println(gv.symbolTable.toString());
            } else if (option.equals("-A")) {
                @SuppressWarnings("unchecked")
                Program program = (Program) root.value; 
                program.accept(new ASTPrintVisitor());
                System.out.println("\n"); 
            } else if (option.equals("-P")) {
                @SuppressWarnings("unchecked")
                Program program = (Program)root.value;
                program.accept(new PrettyPrintVisitor());
                System.out.print("\n");

            } else if (option.equals("-S")) {
                Symbol t = s.next_token();
                while (t.sym != sym.EOF) { 
                    // print each token that we scan
                    System.out.print(s.symbolToString(t) + " ");
                    t = s.next_token();
                }
            } else {
                System.exit(1);   
            }
            System.exit(0);
        } catch (Exception e) {
            // yuck: some kind of error in the compiler implementation
            // that we're not expecting (a bug!)
            System.err.println("Unexpected internal compiler error: " + 
                        e.toString());
            // print out a stack dump
            e.printStackTrace();
            System.exit(1); 
        }
    }
}
