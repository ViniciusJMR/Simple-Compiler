package test;

import lexical.LexicalAnalysis;
import org.junit.jupiter.api.Test;
import semantic.SemanticAnalysis;
import semantic.SemanticError;
import syntax.SyntaxAnalysis;
import syntax.SyntaxError;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class SemanticAnalysisTest {

    @Test
    public void should_not_throw_variable_not_defined_let(){
        String program = """
10 let a = 1 - 2
15 let b = 30
20 let c = b - a
25 let d = c
99 end""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                assertDoesNotThrow(() -> semantic.analyze(syntax.getNodes()));
            }
        }
    }

    @Test
    public void should_throw_variable_not_declared(){
        String program = """
15 let a = c - 1
99 end""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Variavel 'c' não definido""";

                assertEquals(expected, error.getMessage());
            }
        }

        program = """
15 let a = c
99 end""";
        source = new StringReader(program);
        lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Variavel 'c' não definido""";

                assertEquals(expected, error.getMessage());
            }
        }

        program = """
15 let a = 1 + d
99 end""";
        source = new StringReader(program);
        lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Variavel 'd' não definido""";

                assertEquals(expected, error.getMessage());
            }
        }
    }

    @Test
    public void should_throw_variable_not_defined_print(){
        String program = """
20 print a
99 end""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Variável 'a' não definido""";

                assertEquals(expected, error.getMessage());
            }
        }
    }

    @Test
    public void should_not_throw_variable_not_declared_print(){
        String program = """
10 input a
20 print a
99 end""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                assertDoesNotThrow(() -> semantic.analyze(syntax.getNodes()));
            }
        }
    }

    @Test
    public void should_not_throw_variable_not_declared_if() {
        String program = """
05 input a
06 input b
10 if a == b goto 10
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                assertDoesNotThrow(() -> semantic.analyze(syntax.getNodes()));
            }
        }
    }
    @Test
    public void should_not_throw_variable_not_declared_goto() {
        String program = """
10 if 1 == 1 goto 10
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                assertDoesNotThrow(() -> semantic.analyze(syntax.getNodes()));
            }
        }
    }

    @Test
    public void should_throw_variable_not_declared_goto() {
        String program = """
10 if 1 == 1 goto 05
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Linha '05' não existe""";

                assertEquals(expected, error.getMessage());
            }
        }

    }

    @Test
    public void should_throw_variable_not_declared_first_item_if() {
        String program = """
10 if a == 1 goto 10 
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Variável 'a' não definido""";

                assertEquals(expected, error.getMessage());
            }
        }

    }

    @Test
    public void should_throw_variable_not_declared_second_item_if() {
        String program = """
10 if 1 == b goto 10 
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Variável 'b' não definido""";

                assertEquals(expected, error.getMessage());
            }
        }
    }

    @Test
    public void should_throw_line_doesnt_exist() {
        String program = """
10 goto 15
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                SemanticError error = assertThrows(SemanticError.class, () -> semantic.analyze(syntax.getNodes()));

                String expected = """
Linha '15' não existe""";

                assertEquals(expected, error.getMessage());
            }
        }
    }

    @Test
    public void should_not_throw_line_error_goto() {
        String program = """
05 goto 10
10 input a
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);

            if(!syntax.getError()){
                final SemanticAnalysis semantic = new SemanticAnalysis(syntax.getReversedSymbolTable());
                assertDoesNotThrow(() -> semantic.analyze(syntax.getNodes()));
            }
        }
    }

}