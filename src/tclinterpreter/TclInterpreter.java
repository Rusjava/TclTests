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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class interpretes Tcl scripts
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclInterpreter extends AbstractTclInterpreter {

    /**
     * A map for Tcl keywords
     */
    public static final Map<String, Consumer<TclNode>> COMMANDS = new HashMap<>();

    /**
     * A map for variables
     */
    protected final Map<String, String> VARS = new HashMap<>();

    /**
     * Constructor, which sets up the interpreter with an attached parser
     *
     * @param parser
     */
    public TclInterpreter(TclParser parser) {
        super(parser);
    }

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
                VARS.put(readOPNode(node.getChildren().get(0)), readOPNode(node.getChildren().get(1)));
                return;
            }
            VARS.replace(key, readOPNode(node.getChildren().get(1)));

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
            output.append("Tcl> ")
                    .append(readOPNode(node.getChildren().get(0)))
                    .append("\n");
        });
        
        /*
         'Expr' command definition
         */
        COMMANDS.put("expr", node -> {
            //Reading the expression after all allowed substitutions
            String expr=readOPNode(node.getChildren().get(0));
            //The second round of substitutions
            TclNode exprNode = null;
            try {
                exprNode=new TclStringParser(new TclStringLexer(expr)).parse();
            } catch (AbstractTclParser.TclParserError ex) {
                Logger.getLogger(TclInterpreter.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Interpreting the expression
            TclExpressionInterpreter inter=new TclExpressionInterpreter(
                    new TclExpressionParser(new TclExpressionLexer(readOPNode(exprNode))));
            try {
                output.append("Tcl> ")
                        .append(inter.run())
                        .append("\n");
            } catch (AbstractTclParser.TclParserError ex) {
                Logger.getLogger(TclInterpreter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
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
     * Evaluating the value of the operand
     *
     * @param node
     * @return
     */
    protected String readOPNode(TclNode node) {
        StringBuilder str = new StringBuilder("");
        for (TclNode child : node.getChildren()) {
            if (child.type == TclNodeType.NAME) {
                str.append(
                        VARS.get(VARS.keySet().stream().filter(cmd -> {
                            return cmd.equals(child.getValue());
                        }).findFirst().get()));
            } else if (child.type == TclNodeType.QSTRING) {
                str.append(child.getValue());
            } else if (child.type == TclNodeType.CURLYSTRING) {
                str.append(child.getValue());
            } else if (child.type == TclNodeType.PROGRAM) {
                str.append("[").append(child.getValue()).append("]");
            } else if (child.type == TclNodeType.WORD) {
                str.append(child.getValue());
            }
        }
        return str.toString();
    }

    /**
     * Running the script
     *
     * @return
     * @throws tclinterpreter.TclParser.TclParserError
     */
    @Override
    public String run() throws TclParser.TclParserError {
        TclNode root = parser.parse();
        List<TclNode> chld = root.getChildren();
        output.append("Executing ").append(root.getValue()).append("\n");
        for (TclNode node : chld) {
            executeCommand(node);
        }
        return output.toString();
    }

}
