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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

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
    public static final Map<String, Consumer<TclNode>> COMMANDS = new HashMap<>();

    /**
     * A map for variables
     */
    protected final Map<String, String> VARS = new HashMap<>();

    /**
     * A string for the script output
     */
    protected StringBuilder output = new StringBuilder("Tcl> ");

    /**
     * Initializing keywords map
     */
    {
        /*
         'Set' command definition
         */
        COMMANDS.put("set", node -> {
            String key;
            try {
                key = VARS.keySet().stream().filter(cmd -> {
                    return cmd.equals(node.getChildren().get(0).getValue());
                }).findFirst().get();
            } catch (NoSuchElementException ex) {
                VARS.put(node.getChildren().get(0).getValue(), node.getChildren().get(1).getValue());
                return;
            }
            VARS.replace(key, node.getChildren().get(1).getValue());

        });
        /*
         'Unset' command definition
         */
        COMMANDS.put("unset", node -> {
            String key;
            try {
                key = VARS.keySet().stream().filter(cmd -> {
                    return cmd.equals(node.getChildren().get(0).getValue());
                }).findFirst().get();
            } catch (NoSuchElementException ex) {
                return;
            }
            VARS.remove(key); 
        });

        /*
         'Puts' command definition
         */
        COMMANDS.put("puts", node -> {
            String key;   
            output.append("Tcl> ").append(
                    VARS.get(VARS.keySet().stream().filter(cmd -> {
                        return cmd.equals(node.getChildren().get(0).getValue());
                    }).findFirst().get()));
        });
    }

    /**
     * Current parser
     */
    protected TclParser parser;

    /**
     * Constructor, which sets up the interpreter with an attached parser
     *
     * @param parser
     */
    public TclInterpreter(TclParser parser) {
        super();
        this.parser = parser;
    }

    /**
     * Executing a Tcl command
     *
     * @param command
     */
    protected void executeCommand(TclNode command) {
        COMMANDS.get(COMMANDS.keySet().stream().filter(cmd -> {
            return cmd.equals(command.getValue());
        }).findFirst().get()).accept(command);
    }

    /**
     * Method, which sets up script
     *
     * @param parser
     */
    public void setParser(TclParser parser) {
        this.parser = parser;
    }

    /**
     * Returning Tcl parser used
     *
     * @return
     */
    public TclParser getParser() {
        return parser;
    }

    /**
     * Running the script
     *
     * @return
     * @throws tclinterpreter.TclParser.TclParserError
     */
    public String run() throws TclParser.TclParserError {
        TclNode root = parser.parse();
        List<TclNode> chld = root.getChildren();
        output.append("Executing ").append(root.getValue()).append("\n");
        for (TclNode node : chld) {
            executeCommand(node);
        }
        return output.toString();
    }

    /**
     * Getting the script output string
     *
     * @return
     */
    public String getOutput() {
        return output.toString();
    }
}
