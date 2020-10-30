import Scanner.*;
import Parser.*;
import AST.*;
import AST.Visitor.*;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java.io.*;
import java.util.*;


public class MiniJava {
    public static void main(String[] args) {
        try {
            // create a scanner on the input file
            ComplexSymbolFactory sf = new ComplexSymbolFactory();
            File filePath = new File(args[1]); 
            InputStream inputStream = new FileInputStream(filePath); 
            Reader in = new BufferedReader(new InputStreamReader(inputStream));
            scanner s = new scanner(in, sf);
            parser p = new parser(s, sf);
            Symbol root = p.parse();

            @SuppressWarnings("unchecked")
            List<Statement> program = (List<Statement>)root.value;
            for (Statement statement: program) {
                statement.accept(new PrettyPrintVisitor());
                System.out.print("\n");
            }
            /*
            Symbol t = s.next_token();
            while (t.sym != sym.EOF) { 
                // print each token that we scan
                System.out.print(s.symbolToString(t) + " ");
                t = s.next_token();
            }
            */
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
