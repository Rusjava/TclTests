/*
 * Copyright (C) 2016 Ruslan Feshchenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tclinterpreter;

/**
 * An enumeration for token types
 *
 * @author вапрол
 * @version 0.1
 */
public enum TCLTokenType {

    NUMBER("number"),
    PLUS("plus"),
    MINUS("minus"),
    MUL("product"),
    DIV("division"),
    LEFTPAR("left_paranthesis"),
    RIGHTPAR("right_paranthesis"),
    LEFTBR("left_bracket"),
    RIGHTBR("right_bracket"),
    LEFTQ("left_quote"),
    RIGHTQ("right_quote"),
    LEFTCURL("left_curly_bracket"),
    RIGHTCURL("right_curly_bracket"),
    PUTS("output"),
    EXPR("expression"),
    UNSET("deletion"),
    SET("assign"),
    SEMI("semicolon"),
    NAME("name");

    private String value;
    /*
     Constructor
     */

    private TCLTokenType(String type) {
        this.value = type;
    }

    /**
     * Returning a string representation of token value
     *
     * @return
     */
    public String type() {
        return value;
    }
    /**
     * Setting value for numbers only
     * @param v 
     */
    public void setValue(String v) {
        if (this == NUMBER) {
            value = v;
        }
    }
}
