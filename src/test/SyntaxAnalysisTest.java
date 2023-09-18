package test;

import lexical.LexicalAnalysis;
import org.junit.jupiter.api.Test;
import syntax.SyntaxAnalysis;
import syntax.SyntaxError;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxAnalysisTest {

    @Test
    public void should_give_label_error() throws SyntaxError {
        String program =
                "input a\n" +
                "99 end";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            assertEquals("Label esperado no início da linha\n" +
                    "Na linha: 1\n" +
                    "Na coluna: 1\n" +
                    "Token: [62, , (1, 1)]", syntaxError.getMessage());
        }
    }

    @Test
    public void should_give_item_error() throws SyntaxError {
        String program =
                "input a\n" +
                        "99 end";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            assertEquals("Label esperado no início da linha\n" +
                    "Na linha: 1\n" +
                    "Na coluna: 1\n" +
                    "Token: [62, , (1, 1)]", syntaxError.getMessage());
        }
    }

}