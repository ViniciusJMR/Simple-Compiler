package test;

import lexical.LexicalAnalysis;
import org.junit.jupiter.api.Test;
import syntax.SyntaxAnalysis;
import syntax.SyntaxError;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxAnalysisTest {

    /*
    * label test
     */
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

    /*
    * command test
     */
    @Test
    public void should_give_command_error() throws SyntaxError {
        String program = """
10 a = 10
99 end""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Comando inválido
Na linha: 1
Na coluna: 4
Token: [41, 1, (1, 4)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

    /*
    * etx test
     */
    @Test
    public void should_give_etx_error() throws SyntaxError {
        String program = """
99 end
""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Fim do arquivo esperado
Na linha: 1
Na coluna: 7
Token: [10, , (1, 7)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

    /*
    * if test
     */
    @Test
    public void should_not_give_conditional_error(){
        String program = """
10 if a == 10 goto 2
99 end""";
        Reader source = new StringReader(program);

        LexicalAnalysis lexical = new LexicalAnalysis();

        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);
        }
    }
    @Test
    public void should_give_conditional_operator_error() throws SyntaxError {
        String program = """
10 if a = 10 goto 10
99 end
""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Operador relacional esperado após a variável
Na linha: 1
Na coluna: 9
Token: [11, , (1, 9)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

    @Test
    public void should_give_item_after_if_error() throws SyntaxError {
        String program = """
10 if == 10 goto 10
99 end
""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Item esperado após if
Na linha: 1
Na coluna: 7
Token: [31, , (1, 7)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

    @Test
    public void should_give_operate_expected_after_relational() throws SyntaxError {
        String program = """
10 if 10 == goto 10
99 end
""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Operando esperado após o operador relacional
Na linha: 1
Na coluna: 13
Token: [65, , (1, 13)]""";
            assertEquals(expected, syntaxError.getMessage());
        }
    }

    @Test
    public void should_give_goto_expected_after_condition() throws SyntaxError {
        String program = """
10 if 10 == 10 10
99 end
""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
goto esperado após a condição
Na linha: 1
Na coluna: 16
Token: [51, 0, (1, 16)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

    @Test
    public void should_give_label_expected_after_goto_condition() throws SyntaxError {
        String program = """
10 if 10 == 10 goto
99 end
""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Label esperado após goto
Na linha: 1
Na coluna: 20
Token: [10, , (1, 20)]""";
            assertEquals(expected, syntaxError.getMessage());
        }

        program = """
10 if 10 == 10 goto a
99 end""";
        source = new StringReader(program);
        LexicalAnalysis newLexical = new LexicalAnalysis();

        if (!newLexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(newLexical.getSymbolTable(), newLexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Label esperado após goto
Na linha: 1
Na coluna: 21
Token: [41, 1, (1, 21)]""";
            assertEquals(expected, syntaxError.getMessage());
        }
    }


    /*
    * rem test
     */
    @Test
    public void should_ignore_rem_token() throws SyntaxError {
        String program = """
10 rem isso é um comentário
15 input a 
99 end""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);
        }
    }

    /*
    * let test
     */

    @Test
    public void should_not_throw_let_error() throws SyntaxError {
        String program = """
15 let a = 10
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);
        }
        program = """
15 let a = -1
99 end""";

        source = new StringReader(program);
        LexicalAnalysis newLexical = new LexicalAnalysis();

        if (!newLexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(newLexical.getSymbolTable(), newLexical.getTokens());
            assertDoesNotThrow(syntax::parse);
        }

        program = """
15 let a = 1 + 3 
99 end""";

        source = new StringReader(program);
        LexicalAnalysis newLexical2 = new LexicalAnalysis();

        if (!newLexical2.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(newLexical2.getSymbolTable(), newLexical2.getTokens());
            assertDoesNotThrow(syntax::parse);
        }

        program = """
15 let a = b / c 
99 end""";

        source = new StringReader(program);
        LexicalAnalysis newLexical3 = new LexicalAnalysis();

        if (!newLexical3.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(newLexical3.getSymbolTable(), newLexical3.getTokens());
            assertDoesNotThrow(syntax::parse);
        }
    }


    @Test
    public void should_ignore_throw_variable_after_let_error() throws SyntaxError {
        String program = """
10 let = 10
99 end
""";
        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Variável esperada após let
Na linha: 1
Na coluna: 8
Token: [11, , (1, 8)]""";

            assertEquals(expected, syntaxError.getMessage());
        }

        program = """
15 let 10 = 10
99 end""";

        source = new StringReader(program);
        LexicalAnalysis newLexical = new LexicalAnalysis();

        if (!newLexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(newLexical.getSymbolTable(), newLexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Variável esperada após let
Na linha: 1
Na coluna: 8
Token: [51, 1, (1, 8)]""";
            assertEquals(expected, syntaxError.getMessage());
        }
    }


    @Test
    public void should_throw_expected_assignment_after_variable_in_let() throws SyntaxError {
        String program = """
15 let a a
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Esperado '=' após a variável em let
Na linha: 1
Na coluna: 10
Token: [41, 1, (1, 10)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

    @Test
    public void should_throw_invalid_item_after_assignment() throws SyntaxError {
        String program = """
15 let a =
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Item inválido.
Na linha: 1
Na coluna: 11
Token: [10, , (1, 11)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

    /*
    * print test
    * input test
     */
    @Test
    public void should_throw_expected_variable_after_print_input() throws SyntaxError {
        String program = """ 
15 input
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Variável esperada.
Na linha: 1
Na coluna: 9
Token: [10, , (1, 9)]""";

            assertEquals(expected, syntaxError.getMessage());
        }

        program = """
15 print 
99 end""";

        source = new StringReader(program);
        LexicalAnalysis newLexical = new LexicalAnalysis();

        if (!newLexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(newLexical.getSymbolTable(), newLexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Variável esperada.
Na linha: 1
Na coluna: 9
Token: [10, , (1, 9)]""";
            assertEquals(expected, syntaxError.getMessage());
        }
    }


    /*
    * goto test
     */

    @Test
    public void should_not_throw_goto_error(){
        String program = """
15 goto 99
99 end""";
        Reader source = new StringReader(program);

        LexicalAnalysis lexical = new LexicalAnalysis();

        if (!lexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            assertDoesNotThrow(syntax::parse);
        }
    }


    @Test
    public void should_throw_goto_error() throws SyntaxError {
        String program = """ 
15 goto 
99 end""";

        Reader source = new StringReader(program);
        LexicalAnalysis lexical = new LexicalAnalysis();
        if (!lexical.parser(source)){
            SyntaxAnalysis syntax = new SyntaxAnalysis(lexical.getSymbolTable(), lexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Label esperado após goto
Na linha: 1
Na coluna: 8
Token: [10, , (1, 8)]""";

            assertEquals(expected, syntaxError.getMessage());
        }

        program = """
15 goto a 
99 end""";

        source = new StringReader(program);
        LexicalAnalysis newLexical = new LexicalAnalysis();

        if (!newLexical.parser(source)) {
            SyntaxAnalysis syntax = new SyntaxAnalysis(newLexical.getSymbolTable(), newLexical.getTokens());
            SyntaxError syntaxError = assertThrows(SyntaxError.class, syntax::parse);

            String expected = """
Label esperado após goto
Na linha: 1
Na coluna: 9
Token: [41, 1, (1, 9)]""";

            assertEquals(expected, syntaxError.getMessage());
        }
    }

}