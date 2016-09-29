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
package tcltests;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tclinterpreter.*;

/**
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class TclTests {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        /*TclInterpreter inter = new TclInterpreter(new TclParser(
         new TclLexer("set name1 \\\n n45;\n\t set name2 \"gh \\\n\t n450\"; puts $name2; puts $name1$name2[set name3 $name1]; ")), null, true);
         try {
         System.out.println(inter.run());
         } catch (TclParser.TclParserError ex) {
         Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
         }*/
 /*TclParser parser = new TclParser(
         new TclLexer("puts имя; puts [set nn 1;]; set имя1 1.0; set vr 2; unset vr; set vr 9.6e+6; set vrr 3.2e-6; set vrr \"3.2e-4\";"
         + "puts [expr {($vr / ($имя1 * 2)+ 3.5e+10* ($vrr)*(2*0.5) - 4.6e+6/($имя1*2-1)*($имя1*3-2))};];"));
         try {
         System.out.println(parser.parse());
         } catch (TclParser.TclParserError ex) {
         Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        AbstractTclInterpreter inter = new TclInterpreter(new TclParser(
                new TclLexer("puts имя; puts [set nn [expr {(2.0**10)+3*1};];]; set имя1 0; set vr 2; unset vr; set vr 9.6e+6; set vrr 3.2e-6; set vrr \"3.2e-4\";"
                        + "puts [expr {($vr / (-!$имя1 * -2)+ 3.5e+10* ($vrr)*(~~2*0.5) - 4.6e+6/(!$имя1*2-1)*(!$имя1*3-2))};];"
                        + "puts [expr {1<<5};];")),
                null, true, stream, "cp1251");
        try {
            inter.run();
        } catch (Exception ex) {
            Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(inter.getOutput());
        try {
            System.out.println(stream.toString("cp1251"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
        AbstractTclInterpreter inter = new TclInterpreter(new TclParser(
                new TclLexer("puts \"Octal number: \\073; Hex number:\\\n\n\t\n \\x19FA; Unicode: \\u0030\"; puts \\u0030hhh\\\n\n\thg;")),
                null, true, stream, "cp1251");
        try {
            inter.run();
        } catch (TclParser.TclParserError ex) {
            Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(inter.getOutput());
        try {
            System.out.println(stream.toString("cp1251"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

}
