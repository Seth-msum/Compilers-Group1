package assign4.lexer;

import java.io.* ;
import java.util.* ;

public class Lexer {
    public int line = 1 ;
    private char peek = ' ' ;
    private FileInputStream in ;
    private BufferedInputStream bin ;
    //private Hashtable words = new Hashtable() ;
    private Hashtable<String, Word> words = new Hashtable<String, Word>() ;
    // void reserve(Word t) {
    //     words.put(t.lexeme, t) ;
    // }
    public Lexer() {
        reserve( new Word("true", Tag.TRUE) ) ;
        reserve( new Word("false", Tag.FALSE) ) ;
        setupIOStream() ;
    }
    void reserve(Word t) {
        words.put(t.lexeme, t) ;
    }
    void setupIOStream() {
        try {
            in = new FileInputStream("input.txt") ;
            bin = new BufferedInputStream(in) ;
        }
        catch (IOException e) {
            System.out.println("IOException") ;
        }   
    }
    void readch() throws IOException {
        peek = (char) bin.read() ;
    }
    public Token scan() throws IOException {
        for ( ; ; readch() ) {
            if(peek == ' ' || peek == '\t')
                continue ;
            else if (peek == 13 || peek == 10) //carriage feed vs line feed
                line = line + 1;
            else
                break ;
        }
        if ( Character.isDigit(peek)) {
            int v = 0 ;
            do {
                v = 10 * v + Character.digit(peek, 10) ;
                peek = (char) bin.read() ;
            } while (Character.isDigit(peek)) ;
            return new Num(v) ;
        }
        if (Character.isLetter(peek) ) {
            StringBuffer b = new StringBuffer() ;
            do {
                b.append(peek) ;
                peek = (char) bin.read() ;
            } while ( Character.isLetterOrDigit(peek)) ;
            String s = b.toString() ;
            Word w = (Word)words.get(s) ;
            if (w != null )
                return w ;
            w = new Word(s, Tag.ID) ;
            words.put(s, w) ;
            return w ;
        }
        Token t = new Token(peek) ;
        peek = ' ' ;
        return t ;
    }

}