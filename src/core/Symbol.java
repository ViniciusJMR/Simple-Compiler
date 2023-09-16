
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

/**
 * Interface responsavel pelas constantes utilizadas na fase de analise
 */
public enum Symbol
{
    /**
     * Delimitador de nova linha
     */
    LF (10),

    /**
     * Delimitador de fim de texto
     */
    ETX (3),

    /**
     * Operador de atribuicao (=)
     */
    ASSIGNMENT (11),

    /**
     * Operador aritmetico de adicao (+)
     */
    ADD (21),

    /**
     * Operador aritmetico de subtracao (-)
     */
    SUBTRACT (22),

    /**
     * Operador aritmetico de multiplicacao (*)
     */
    MULTIPLY (23),

    /**
     * Operador aritmetico de divisao inteira (/)
     */
    DIVIDE (24),

    /**
     * Operador aritmetico de resto da divisao inteira (%)
     */
    MODULO (25),

    /**
     * Operador relacional igual a (==)
     */
    EQ (31),

    /**
     * Operador relacional diferente de (!=)
     */
    NE (32),

    /**
     * Operador relacional maior que (>)
     */
    GT (33),

    /**
     * Operador relacional menor que (<)
     */
    LT (34),

    /**
     * Operador relacional maior ou igual a (>=)
     */
    GE (35),

    /**
     * Operador relacional menor ou igual a (<=)
     */
    LE (36),

    /**
     * Identificador
     */
    VARIABLE (41),

    /**
     * Constante numerica inteira
     */
    INTEGER (51),

    /**
     * Palavra reservada rem
     */
    REM (61),

    /**
     * Palavra reservada input
     */
    INPUT (62),

    /**
     * Palavra reservada let
     */
    LET (63),

    /**
     * Palavra reservada print
     */
    PRINT (64),

    /**
     * Palavra reservada goto
     */
    GOTO (65),

    /**
     * Palavra reservada if
     */
    IF (66),

    /**
     * Palavra reservada end
     */
    END (67),

    /**
     * Token nao reconhecido
     */
    ERROR (99);

    /**
     * Identificador do simbolo
     */
    private final int uid;

    /**
     * Inicializar o simbolo
     *
     * @param uid identificador do simbolo
     */
    private Symbol(final int uid)
    {
        this.uid = uid;
    }

    /**
     * Retornar o identificador do simbolo
     *
     * @return identificador do simbolo
     */
    public int getUid()
    {
        return uid;
    }
}
