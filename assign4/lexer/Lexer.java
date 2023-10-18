package assign4.lexer ;

import java.io.* ;
import java.util.* ;

public class Lexer {

    public int line = 1 ;
    private char peek = ' ' ;

    private FileInputStream in ;
    private BufferedInputStream in_b ;
    private Hashtable<String, Word> words = new Hashtable<String, Word>() ;
    

    public Lexer () {

        // Note that there might be more key words to add by reserve.
        // You would need to reserve and add to the Word class.
        reserve(new Word("true",  Tag.TRUE)) ;
        reserve(new Word("false", Tag.FALSE)) ;

        setInput() ; 
    }

   

    public Token scan() throws IOException {

        //System.out.println("scan() in Lexer") ;

        /*
         * The scan first checks for digit,
         * then checks for if it is a letter - supports more than one letter.
         * if neither, then it defaults to an operator
         */

        ignoreWhiteLines() ; // Removes white space and counds new lines.

        //looks for digit but language only supports integers
        //  possible rewrite function to support floats.
        if (Character.isDigit(peek)) {

            int v = 0 ;

            do {

                v = 10 * v + Character.digit(peek, 10) ;
                readch() ;

            } while (Character.isDigit(peek)) ;

            // System.out.println("v: " + v) ;

            return new Num(v) ;
        }


        if (Character.isLetter(peek)) {

            StringBuffer b = new StringBuffer() ;

            do {

                b.append(peek) ;
                readch();

            } while (Character.isLetterOrDigit(peek)) ;

            String s = b.toString() ;
            //System.out.println("s: " + s) ;
            Word w = (Word) words.get(s) ;

            if (w != null)
                return w ;
            
            w = new Word(s, Tag.ID) ;
            words.put(s, w) ;

            // System.out.println("w: " + w.toString()) ;

            return w ;
        }

        Token t = new Token(peek) ; 
        // System.out.println("t: " + t.toString()) ;
        peek = ' ' ;

        return t ;
    }

    
    void readch() {
        try {
            peek = (char)in_b.read() ;
        } 
        catch (IOException e) {
            System.out.println("input buffer was called while closed.") ;
            System.exit(1) ;
        } 
    }
    
    void ignoreWhiteLines() {
        for ( ; ; readch()) {
            if (peek == ' ' || peek == '\t') 
                continue ;
            else if (peek == '\n') 
                line = line + 1 ;
            else 
                break ;
        }
    }

    void reserve (Word w) {
        //Adds to the symbol table.
        words.put(w.lexeme, w) ;
    }
    
    void setInput() {
        // Fills in File and Buffered Input Stream
          try {
            in = new FileInputStream("input.txt") ;
            in_b = new BufferedInputStream(in) ;
        }
        catch (IOException e){
            System.out.println("input.txt did not open properly");
            e.printStackTrace();
            System.exit(1);

        }
    }
}
