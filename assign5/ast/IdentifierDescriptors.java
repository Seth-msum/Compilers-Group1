package assign5.ast;

import assign5.lexer.* ;

public class IdentifierDescriptors {
    // any metadata that is needed for an identifiernode will be placed in here.
    public Type type = null ;
    public Boolean isArray = false ;
    public Integer arrayDimentions = 0 ;

    public void printMeta() {
        System.out.println();
        System.out.println("***Printing IdentifierNode metaData***");
        System.out.println("Type: " + this.type.toString()) ;
        if (isArray) {
            System.out.println("id is an Array");
            System.out.println("Array dimentions: " + arrayDimentions);
        }
        else
            System.out.println("id is a single value.");
        
    }
}
