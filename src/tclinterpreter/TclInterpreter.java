/*
 * Copyright (C) 2015 Ruslan Feshchenko
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

import java.util.HashMap;
import java.util.Map;

/*
 * This class interpretes Tcl scripts
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclInterpreter {

    /**
     * A map for Tcl keywords
     */
    public static final Map<String, TclTokenType> KEY_WORDS = new HashMap<> ();
    
    /**
     * Initializing keywords map
     */
    static {
        KEY_WORDS.put("set", TclTokenType.SET);
        KEY_WORDS.put("unset", TclTokenType.UNSET);
        KEY_WORDS.put("expr", TclTokenType.EXPR);
        KEY_WORDS.put("puts", TclTokenType.PUTS);
    }

    /**
     * Current script 
     */
    protected TCLLexer lex;
    
    /**
     * Constructor, which sets up the interpreter with an attached lexer
     * @param lex
     */
    public TclInterpreter(TCLLexer lex) {
        super();
        this.lex=lex;
        
    }
    
    /**
     * Method, which sets up script
     * @param lex
     */
    public void setLex (TCLLexer lex) {
        this.lex=lex;
        System.out.println(lex.getScript());
    }
    /**
     * Parsing the script
     */
    public void parse () {
        
    }
}
