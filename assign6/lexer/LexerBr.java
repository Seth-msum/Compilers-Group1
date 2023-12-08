package assign6.lexer;

import java.io.* ;
import java.util.* ;

public class Lexer {
    public int line = 1 ;
    private char peek = ' ' ;
    public int tokenCount = 0 ;

    private FileInputStream in ;
    private BufferedInputStream bin ;

    // private boolean negflag = false; // use to force 4-5 to return subtraction operator
    private boolean negnum  = false; // used to make number negative value.
    private char prepeek ;
    private Hashtable<String, Word> words = new Hashtable<String, Word>() ;

    public Lexer() {
   
        reserve( new Word("if", Tag.IF)) ;
        reserve( new Word("else", Tag.ELSE)) ;
        reserve( new Word("while", Tag.WHILE)) ;
        reserve( new Word("do", Tag.DO)) ;
        reserve( new Word("break", Tag.BREAK)) ;

        reserve(Word.True) ;
        reserve(Word.False) ;

        reserve(Word.eof) ;

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
        prepeek = peek ;
        peek = (char) bin.read() ;
    }

    boolean readch(char c) throws IOException {

        readch() ;

        if(peek != c) return false ;
        peek = ' ' ;

        return true ;
    }

    public Token scan() throws IOException {

        tokenCount++ ;

        // this removes cases where there is no negative immediatly after number
        // if(negflag)
        //     if(peek != '-')
        //         negflag = false ;



        for ( ; ; readch() ) { // This loads peek for scan with non white character

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
                return Word.le ;
            else return new Token('<') ;
        
        case '>':
            if(readch('=') )
                return Word.ge ;
            else return new Token('>') ;

        case '{':
                peek = ' ' ;
                return Word.bst ;

        case '}':
                peek = ' ' ;
                return Word.bet ;
        case '-':
            if (prepeek == ' ') {
                readch();
                if(Character.isDigit(peek))
                    negnum = true ;
                else
                
            }
                
                return Word.minus ;
            // if(negflag)// its imediatly after number so it had to be operator.
            //     return Word.minus ;
            // if(readch(' ')) //There is space so its an operator.
            //     return Word.minus ;
            // else {
            //     if(Character.isDigit(peek)) //next to number so it makes it negative.
            //         negnum = true ;
            //     else
            //         return Word.minus ; //not next to number so its an operator
            // }
        }

        if ( Character.isDigit(peek)) {
            int v = 0 ;
            do {
                v = 10 * v + Character.digit(peek, 10) ;
                peek = (char) bin.read() ;
            } while (Character.isDigit(peek)) ;

            //System.out.println("v: " + v) ;

            if(peek != '.') {
                // if(negnum) {
                //     negnum = false ;
                //     v = v * -1 ;
                // }
                // if (readch('-')) { //if a negative follows, set negflag.
                    //negflag = true ; // tells the scan() to first check for '-'
                // }
                return new Num(v) ;
            }
               

            float x = v ; float d = 10 ;

            for(;;) {
                readch() ;

                if( ! Character.isDigit(peek)) break;

                x = x + Character.digit(peek, 10) / d; d = d*10 ;
            }
            // if(negnum) {
            //     negnum = false ;
            //     x = x * -1 ;
            // }
            //if (readch('-')) { //if a negative follows, set negflag.
            //negflag = true ;
            //}
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
            //negflag = true ;
            if (w != null )
                return w ;
            w = new Word(s, Tag.ID) ;
            words.put(s, w) ;
            return w ;
        }

        if ((int)peek == 65535) { // 65535 is eof

            System.out.println("@@@@@@@@@ EOF reached...") ;

            return Word.eof ;
        }

        Token t = new Token(peek) ;
        peek = ' ' ;
        return t ;
    }



}