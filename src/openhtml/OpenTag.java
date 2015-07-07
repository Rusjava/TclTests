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
package openhtml;

import static javax.swing.text.html.HTML.Tag;
import static javax.swing.text.html.HTML.getTag;

/**
 * A class extending HTML.Tag to open up methods and add HTML 4.0 tags
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class OpenTag extends Tag {
    
    static {
            // Force HTMLs static initialize to be loaded.
            getTag("html");
        }
    
    /**
     * Constructor without arguments
     */
    public OpenTag () {
        super();
    }
    
    /**
     * Constructor creating a tag with a given name
     * @param id
     */
    public OpenTag (String id) {
        super(id);
    }
    
    /**
     * HTML 4.0 tag ACRONYM
     */
    public static final Tag ACRONYM = new OpenTag("acronym");
    
    /**
     * List of all real HTML 4.0 tags
     */
    public static final Tag allTags[]  = {
            A, ACRONYM, ADDRESS, APPLET, AREA, B, BASE, BASEFONT, BIG,
            BLOCKQUOTE, BODY, BR, CAPTION, CENTER, CITE, CODE,
            DD, DFN, DIR, DIV, DL, DT, EM, FONT, FORM, FRAME,
            FRAMESET, H1, H2, H3, H4, H5, H6, HEAD, HR, HTML,
            I, IMG, INPUT, ISINDEX, KBD, LI, LINK, MAP, MENU,
            META, NOFRAMES, OBJECT, OL, OPTION, P, PARAM,
            PRE, SAMP, SCRIPT, SELECT, SMALL, SPAN, STRIKE, S,
            STRONG, STYLE, SUB, SUP, TABLE, TD, TEXTAREA,
            TH, TITLE, TR, TT, U, UL, VAR
        };
}
