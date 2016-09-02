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
 * A special lexer for quote enclosed strings
 * 
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclStringLexer {
    /**
     * TCL script
     */
    protected String script = null;  
    /**
     * Current position in script
     */
    protected int pos = 0;
    /**
     * Current symbol at position of pos
     */
    protected char currentchar = 0;
    
    /**
     * Constructor
     *
     * @param script a TCL script to interpret
     */
    public TclStringLexer(String script) {
        this.script = script;
        currentchar = script.charAt(pos);
    }
}
