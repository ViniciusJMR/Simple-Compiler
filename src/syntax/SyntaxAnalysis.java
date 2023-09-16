package syntax;

import core.Token;

import java.util.List;
import java.util.Map;

public class SyntaxAnalysis {

    private Map<String, Integer> symbolTable;
    private List<Token> tokens;
    private Token currentToken;
    private int currentIndex;

    public SyntaxAnalysis(Map<String, Integer> symbolTable, Token currentToken, int currentIndex) {
        this.symbolTable = symbolTable;
        this.currentToken = currentToken;
        this.currentIndex = currentIndex;
    }

    // Parse Program

    // Parse Label

    // Parse Command

    // Parse Expression

    // Parse  Operator
    // Parse Conditional
    // Parse Relational
    // Parse Item
    // Parse Integer
    // Parse Digit
    // Parse Variable




}
