package semantic;

import core.Token;

public class SemanticError extends Exception{

    private Token token;

    public SemanticError(String message, Token token) {
        super(message);
        this.token = token;
    }

    @Override
    public String toString() {
        return getMessage() + "\n" +
                "Na Linha: " + token.getLine() + ". " +
                "Na Coluna: " + token.getColumn() + ".\n";

    }
}
