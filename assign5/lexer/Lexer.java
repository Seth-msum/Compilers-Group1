package assign5.lexer;

import java.io.* ;
import java.util.* ;

public class Lexer {
    public int line = 1 ;
    private char peek = ' ' ;

    private FileInputStream in ;
    private BufferedInputStream bin ;

    private Hashtable<String, Word> words = new Hashtable<String, Word>() ;

    public Lexer() {
   
        reserve( new Word("if", Tag.IF)) ;
        reserve( new Word("else", Tag.ELSE)) ;
        reserve( new Word("while", Tag.WHILE)) ;
        reserve( new Word("do", Tag.DO)) ;
        reserve( new Word("break", Tag.BREAK)) ;

        reserve(Word.True) ;
        reserve(Word.False) ;

        reserve( Type.Int) ;
        reserve( Type.Char) ;
        reserve( Type.Bool ) ;
        reserve( Type.Float) ;
        
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

    boolean readch(char c) throws IOException {

        readch() ;

        if(peek != c) return false ;
        peek = ' ' ;

        return true ;
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

        switch( peek ) {

        case '&':
            if(readch('&'))
                return Word.and ;
            else return new Token('&') ;
        
        case '|':
            if(readch('|') )
                return Word.or ;
            else return new Token('|') ;
        
        case '=':
            if(readch('=') )
                return Word.eq ;
            else return new Token('=') ;
        
        case '!':
            if(readch('=') )
                return Word.ne ;
            else return new Token('!') ;
        
        case '<':
            if(readch('=') )
                return Word.eq ;
            else return new Token('<') ;
        
        case '>':
            if(readch('=') )
                return Word.eq ;
            else return new Token('>') ;
        
        }

        if ( Character.isDigit(peek)) {
            int v = 0 ;
            do {
                v = 10 * v + Character.digit(peek, 10) ;
                peek = (char) bin.read() ;
            } while (Character.isDigit(peek)) ;

            //System.out.println("v: " + v) ;

            if(peek != '.')
                return new Num(v) ;

            float x = v ; float d = 10 ;

            for(;;) {
                readch() ;

                if( ! Character.isDigit(peek)) break;

                x = x + Character.digit(peek, 10) / d; d = d*10 ;
            }
            return new Real(x) ;
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