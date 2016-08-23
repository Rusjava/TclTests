/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcltests;

import java.util.logging.Level;
import java.util.logging.Logger;
import tclinterpreter.*;

/**
 *
 * @author Samsung
 */
public class TclTests {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TclParser parser = new TclParser(new TclLexer("set name1 n45; set name2 n450;"));
        try {
            System.out.println(parser.parse());
        } catch (TclParser.TclParserError ex) {
            Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
