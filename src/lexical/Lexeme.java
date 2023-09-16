
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

/**
 * Classe responsavel pela representacao de um lexema
 */
public class Lexeme
{
    /**
     * Termo lido do codigo-fonte
     */
    private final StringBuilder term;

    /**
     * Tipo/classe do lexema
     */
    private Symbol type;

    /**
     * Numero da linha no codigo-fonte
     */
    private int line;

    /**
     * Numero da coluna no codigo-fonte
     */
    private int column;

    /**
     * Construtor para inicializar o lexema
     *
     * @param character caractere lido do codigo-fonte
     * @param type tipo/classe do lexema
     * @param line numero da linha no codigo-fonte
     * @param column numero da coluna no codigo-fonte
     */
    public Lexeme(final char character, final Symbol type, final int line, final int column)
    {
        term = new StringBuilder().append(character);

        this.type = type;

        this.line = line;

        this.column = column;
    }

    /**
     * Adicionar um caractere ao termo lido do codigo-fonte
     *
     * @param character caractere lido do codigo-fonte
     * @param type tipo/classe do lexema
     */
    public void append(final char character, final Symbol type)
    {
        term.append(character);

        this.type = type;
    }

    /**
     * Retornar o termo lido do codigo-fonte
     *
     * @return termo lido do codigo-fonte
     */
    public String getTerm()
    {
        return term.toString();
    }

    /**
     * Retornar o tipo/classe do lexema
     *
     * @return tipo/classe do lexema
     */
    public Symbol getType()
    {
        return type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "'" + getTerm() + "' (" + line + ", " + column + ")";
    }

    /**
     * Retornar o token correspondente ao lexema - sem tabela de simbolo
     *
     * @return token correspondente ao lexema - sem tabela de simbolo
     */
    public Token toToken()
    {
        return new Token(type, line, column);
    }

    /**
     * Retornar o token correspondente ao lexema - com tabela de simbolo
     *
     * @param address endereco na tabela de simbolos
     * @return token correspondente ao lexema - com tabela de simbolo
     */
    public Token toToken(final int address)
    {
        return new Token(type, address, line, column);
    }
}
