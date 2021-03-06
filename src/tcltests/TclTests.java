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

import tclparser.*;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tclinterpreter.*;
import tcllexer.ParallelTclLexer;

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
         new TclLexer("set var 54; puts [set var;];"));
         try {
         System.out.println(parser.parse());
         } catch (TclParser.TclParserError ex) {
         Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        AbstractTclInterpreter inter = new TclListInterpreter(new TclListParser(
                new ParallelTclLexer("puts {23}; # This is a comment\n puts [set nn [expr {(2.0**10)+3*1};];]; set имя1 0.0; set vr 2; unset vr; set vr(0) 9.6e+6; set vr(1) 3.2e-6; set vr(1) \"3.2e-4\";"
                        + "puts [expr {($vr(0) / (-!$имя\\\n1 * -2)+ 3.5e+10* ($vr(1))*(~~2*0.5) - 4.6e+6/(!$имя1*2-1)*(!$имя1*3-2))};];"
                        + "puts [expr {((1<<0\\\n12)+3)%10};]; puts [expr {\"string\"+\"2\" ne \"string2\"};];"
                        + "puts [set vr(1);]; puts [set vr(0);];"
                        + "puts [if {$vr(0)>1} then {expr [expr 25]} elseif {$vr(0)>1} {expr 35} else {expr 45}];"
                        + "set vv 22; puts [\\\n set vv];"
                        + "set i 0;  while {$i<6} {set i [expr {$i+1}]}; puts $i;"
                        + "for {set i 0; set ii 1;} {$i<6} {set i [expr {$i+1}]} {set ii [expr {$ii*($i+1)}];}; puts $ii;"
                        + "puts [string index \"str1\" 2];"
                        + "puts [string trim \"sttssrrststrrsts\" \"st\"];"
                        + "puts [string wordend \"fgh45t 1e4hjktfd\" \"7\"];"
                        + "puts [string match \"*mk?cm\" \"lhemk3cm\"];"
                        + "puts [expr {2*double(1)}];"
                        + "set mlist [list {23 uh} 45 {{gh ft}};];"
                        + "puts $mlist;"
                        + "puts [llength $mlist;];"
                        + "puts [lindex $mlist 2 0 1;];"
                        + "set sss(0) rt; lappend sss(0) fg gh nt; puts $sss(0);"
                        + "puts [format \"%x %4.2g %f %d %c\" 28 44.2234 5.4 34 h];"
                        + "proc newcom {ar1 ar2} {upvar 1 mlist mlst; set mlst {{kj} {ed2}}; puts $mlst; return [expr {$ar1**2+$ar2**2};]};"
                        + "puts [newcom 12 7;]; puts $mlist;"
                        + "puts [lassign {ghf thk hfg rty} vv1 vv2 vv3;]; puts \"$vv1 $vv2 $vv3\";"
                        + "for {set i 0;} {$i<98} {set i [expr {$i+2}]} {if {$i>22} {continue;} else {puts $i};}; puts $i;")),
                null, true, stream, "cp1251");
        
        try {
            inter.run();
        } catch (AbstractTclParser.TclParserError | AbstractTclInterpreter.TclExecutionException | AbstractTclInterpreter.TclCommandException ex) {
            Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(inter.getOutput());
        try {
            System.out.println(stream.toString("cp1251"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TclTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(inter.getOutput());
    }

}
