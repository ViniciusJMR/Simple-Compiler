
/*************************************************************************
 * Copyright (C) 2009/2023 - Cristiano Lehrer (cristiano@ybadoo.com.br)  *
 *                  Ybadoo - Solucoes em Software Livre (ybadoo.com.br)  *
 *                                                                       *
 * Permission is granted to copy, distribute and/or modify this document *
 * under the terms of the GNU Free Documentation License, Version 1.3 or *
 * any later version published by the  Free Software Foundation; with no *
 * Invariant Sections,  no Front-Cover Texts, and no Back-Cover Texts. A *
 * A copy of the  license is included in  the section entitled "GNU Free *
 * Documentation License".                                               *
 *                                                                       *
 * Ubuntu 16.10 (GNU/Linux 4.8.0-39-generic)                             *
 * OpenJDK Version "1.8.0_121"                                           *
 * OpenJDK 64-Bit Server VM (build 25.121-b13, mixed mode)               *
 *************************************************************************/
package lexical;

import core.Symbol;
import core.Token;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe responsavel pela analise lexica da linguagem de programacao
 * SIMPLE 15.01
 */
public class LexicalAnalysis
{
    /**
     * Numero da coluna no codigo-fonte
     */
    private int column;

    /**
     * Problemas na analise lexica
     */
    private boolean error;

    /**
     * Lexema em reconhecimento
     */
    private Lexeme lexeme;

    /**
     * Numero da linha no codigo-fonte
     */
    private int line;

    /**
     * Arquivo com o codigo-fonte
     */
    private Reader source;

    /**
     * Tabela de simbolos da analise lexica
     */
    private Map<String, Integer> symbolTable;

    /**
     * Tokens identificados na analise lexica
     */
    private List<Token> tokens;

    /**
     * Construtor padrao
     */
    public LexicalAnalysis()
    {
        line = 1;

        column = 0;

        error = false;
    }

    /**
     * Adicionar os identificadores e as constantes numericas a tabela de
     * simbolos
     *
     * @param lexeme identificador e/ou constante numerica
     * @return posicao do lexema e/ou constante numerica na tabela de
     *         simbolos
     */
    private int addSymbolTable(final String lexeme)
    {
        if (!symbolTable.containsKey(lexeme))
        {
            symbolTable.put(lexeme, symbolTable.size());
        }

        return symbolTable.get(lexeme);
    }

    /**
     * Adicionar o lexema a lista de tokens
     */
    private void addToken()
    {
        if (lexeme.getType() != Symbol.ERROR)
        {
            if (lexeme.getType() == Symbol.INTEGER || lexeme.getType() == Symbol.VARIABLE)
            {
                tokens.add(lexeme.toToken(addSymbolTable(lexeme.getTerm())));
            }
            else
            {
                tokens.add(lexeme.toToken());
            }
        }
        else
        {
            System.out.println("Token não reconhecido: " + lexeme);

            error = true;
        }
    }

    /**
     * Retornar a tabela de simbolos da analise lexica
     *
     * @return tabela de simbolos da analise lexica
     */
    public Map<String, Integer> getSymbolTable()
    {
        return symbolTable;
    }

    /**
     * Retornar os tokens identificados na analise lexica
     *
     * @return tokens identificados na analise lexica
     */
    public List<Token> getTokens()
    {
        return tokens;
    }

    /**
     * Retornar o character do token para analise
     *
     * @return character do token para analise
     */
    private char next()
    {
        int character = 0;

        if (source != null)
        {
            try
            {
                character = source.read();

                if (character == '\r')
                {
                    character = source.read();
                }

                if (character > 0)
                {
                    column = column + 1;

                    return (char) character;
                }
                else
                {
                    source.close();

                    source = null;
                }
            }
            catch (final IOException exception)
            {
                exception.printStackTrace();
            }
        }

        return 0;
    }

    /**
     * Realizar a analise lexica do codigo-fonte
     *
     * @param source arquivo com o codigo-fonte de entrada
     * @return sucesso ou nao na analisa lexica
     */
    public boolean parser(final Reader source)
    {
        this.source = source;

        tokens = new LinkedList<>();

        symbolTable = new LinkedHashMap<>();

        while (this.source != null)
        {
            q0();
        }

        return error;
    }

    /**
     * Estado inicial do automato finito deterministico
     */
    private void q0()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                q04();
                break;
            case '\n' :
                q03();
                break;
            case ' ' :
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            case '+' :
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            default :
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q99();
        }
    }

    /**
     * Estado responsavel pelo reconhecimento da constante numerica inteira
     */
    private void q01()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                lexeme.append(character, Symbol.INTEGER);
                q01();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q01();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do identificador
     */
    private void q02()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q02();
        }
    }

    /**
     * Estado responsavel pelo reconhecimento do delimitador de nova linha
     */
    private void q03()
    {
        lexeme = new Lexeme('\n', Symbol.LF, line, column);

        addToken();

        line = line + 1;

        column = 0;
    }

    /**
     * Estado responsavel pelo reconhecimento do delimitador de fim de texto
     */
    private void q04()
    {
        lexeme = new Lexeme('\0', Symbol.ETX, line, column);

        addToken();
    }

    /**
     * Estado responsavel pelo reconhecido dos operadores aritmeticos
     *   adicao (+)
     *   subtracao (-)
     *   multiplicacao (*)
     *   divisao inteira (/)
     *   resto da divisao inteira (%)
     */
    private void q05()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q05();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador de atribuicao (=)
     */
    private void q06()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            case '=' :
                lexeme.append(character, Symbol.EQ);
                q09();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q06();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador relacional
     * menor que (<)
     */
    private void q07()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            case '=' :
                lexeme.append(character, Symbol.LE);
                q10();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q07();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador relacional
     * maior que (>)
     */
    private void q08()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            case '=' :
                lexeme.append(character, Symbol.GE);
                q11();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q08();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador relacional
     * igual a (==)
     */
    private void q09()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q09();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador relacional
     * maior ou igual a (>=)
     */
    private void q10()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q10();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador relacional
     * menor ou igual a (<=)
     */
    private void q11()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q11();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador relacional
     * diferente de (!=)
     */
    private void q12()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q12();
        }
    }

    /**
     * Estado responsavel pelo reconhecido do operador relacional
     * diferente de (!=)
     */
    private void q13()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '=' :
                lexeme.append(character, Symbol.NE);
                q12();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q13();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada rem
     */
    private void q14()
    {
        char character = next();

        switch(character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            case 'e' :
                lexeme.append(character, Symbol.ERROR);
                q15();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q14();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada rem
     */
    private void q15()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 'm' :
                lexeme.append(character, Symbol.REM);
                q31();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q15();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada if
     */
    private void q16()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            case 'f' :
                lexeme.append(character, Symbol.IF);
                q32();
                break;
            case 'n' :
                lexeme.append(character, Symbol.ERROR);
                q17();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q16();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada input
     */
    private void q17()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 'p' :
                lexeme.append(character, Symbol.ERROR);
                q18();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q17();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada input
     */
    private void q18()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 'u' :
                lexeme.append(character, Symbol.ERROR);
                q19();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q18();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada input
     */
    private void q19()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 't' :
                lexeme.append(character, Symbol.INPUT);
                q32();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q19();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada let
     */
    private void q20()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            case 'e' :
                lexeme.append(character, Symbol.ERROR);
                q21();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q20();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada let
     */
    private void q21()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 't' :
                lexeme.append(character, Symbol.LET);
                q32();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q21();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada print
     */
    private void q22()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            case 'r' :
                lexeme.append(character, Symbol.ERROR);
                q23();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q22();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada print
     */
    private void q23()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 'i' :
                lexeme.append(character, Symbol.ERROR);
                q24();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q23();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada print
     */
    private void q24()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 'n' :
                lexeme.append(character, Symbol.ERROR);
                q25();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q24();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada print
     */
    private void q25()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 't' :
                lexeme.append(character, Symbol.PRINT);
                q32();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q25();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada goto
     */
    private void q26()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            case 'o' :
                lexeme.append(character, Symbol.ERROR);
                q27();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q26();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada goto
     */
    private void q27()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 't' :
                lexeme.append(character, Symbol.ERROR);
                q28();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q27();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada goto
     */
    private void q28()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 'o' :
                lexeme.append(character, Symbol.GOTO);
                q32();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q28();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada end
     */
    private void q29()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            case 'n' :
                lexeme.append(character, Symbol.ERROR);
                q30();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q29();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada end
     */
    private void q30()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            case 'd' :
                lexeme.append(character, Symbol.END);
                q32();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q30();
        }
    }

    /**
     * Estado responsavel pelo reconhecido da palavra reservada rem
     */
    private void q31()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            default :
                q31();
        }
    }

    /**
     * Estado responsavel pelo reconhecido das palavras reservadas
     * end
     * goto
     * if
     * input
     * let
     * print
     */
    private void q32()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                addToken();
                q04();
                break;
            case '\n' :
                addToken();
                q03();
                break;
            case ' ' :
                addToken();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q32();
        }
    }

    /**
     * Estado responsavel pelo reconhecimento do erro
     */
    private void q99()
    {
        final char character = next();

        switch (character)
        {
            case 0 :
                q04();
                break;
            case '\n' :
                q03();
                break;
            case ' ' :
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                addToken();
                lexeme = new Lexeme(character, Symbol.INTEGER, line, column);
                q01();
                break;
            case 'a' :
            case 'b' :
            case 'c' :
            case 'd' :
            case 'f' :
            case 'h' :
            case 'j' :
            case 'k' :
            case 'm' :
            case 'n' :
            case 'o' :
            case 'q' :
            case 's' :
            case 't' :
            case 'u' :
            case 'v' :
            case 'w' :
            case 'x' :
            case 'y' :
            case 'z' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q02();
                break;
            case 'r' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q14();
                break;
            case 'i' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q16();
                break;
            case 'l' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q20();
                break;
            case 'p' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q22();
                break;
            case 'g' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q26();
                break;
            case 'e' :
                addToken();
                lexeme = new Lexeme(character, Symbol.VARIABLE, line, column);
                q29();
                break;
            case '+' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ADD, line, column);
                q05();
                break;
            case '-' :
                addToken();
                lexeme = new Lexeme(character, Symbol.SUBTRACT, line, column);
                q05();
                break;
            case '*' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MULTIPLY, line, column);
                q05();
                break;
            case '/' :
                addToken();
                lexeme = new Lexeme(character, Symbol.DIVIDE, line, column);
                q05();
                break;
            case '%' :
                addToken();
                lexeme = new Lexeme(character, Symbol.MODULO, line, column);
                q05();
                break;
            case '=' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ASSIGNMENT, line, column);
                q06();
                break;
            case '<' :
                addToken();
                lexeme = new Lexeme(character, Symbol.LT, line, column);
                q07();
                break;
            case '>' :
                addToken();
                lexeme = new Lexeme(character, Symbol.GT, line, column);
                q08();
                break;
            case '!' :
                addToken();
                lexeme = new Lexeme(character, Symbol.ERROR, line, column);
                q13();
                break;
            default :
                lexeme.append(character, Symbol.ERROR);
                q99();
        }
    }
}