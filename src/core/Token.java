
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
package core;

import core.Symbol;

/**
 * Classe responsavel pela representacao de um token
 */
public class Token
{
    /**
     * Tipo/classe do token
     */
    private Symbol type;

    /**
     * Endereco na tabela de simbolos
     */
    private int address;

    /**
     * Numero da linha no codigo-fonte
     */
    private int line;

    /**
     * Numero da coluna no codigo-fonte
     */
    private int column;

    /**
     * Construtor para inicializar o token - sem tabela de simbolo
     *
     * @param type tipo/classe do token
     * @param line numero da linha no codigo-fonte
     * @param column numero da coluna no codigo-fonte
     */
    public Token(final Symbol type, final int line, final int column)
    {
        this(type, -1, line, column);
    }

    /**
     * Construtor para inicializar o token - com tabela de simbolo
     *
     * @param type tipo/classe do token
     * @param address endereco na tabela de simbolos
     * @param line numero da linha no codigo-fonte
     * @param column numero da coluna no codigo-fonte
     */
    public Token(final Symbol type, final int address, final int line, final int column)
    {
        this.type = type;

        this.address = address;

        this.line = line;

        this.column = column;
    }

    /**
     * Retornar o tipo/classe do token
     *
     * @return tipo/classe do token
     */
    public Symbol getType()
    {
        return type;
    }

    /**
     * Retornar o endereco na tabela de simbolos
     *
     * @return endereco na tabela de simbolos
     */
    public int getAddress()
    {
        return address;
    }

    /**
     * Retornar o numero da linha no codigo-fonte
     *
     * @return numero da linha no codigo-fonte
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Retornar o numero da coluna no codigo-fonte
     *
     * @return numero da coluna no codigo-fonte
     */
    public int getColumn()
    {
        return column;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (getAddress() != -1)
        {
            return "[" + getType().getUid() + ", " + getAddress()+ ", ("
                    + getLine() + ", " + getColumn() + ")]" ;
        }
        else
        {
            return "[" + getType().getUid() + ", , ("
                    + getLine() + ", " + getColumn() + ")]" ;
        }
    }
}
