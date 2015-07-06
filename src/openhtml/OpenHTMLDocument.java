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

import java.net.URL;
import java.util.Hashtable;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/*
 * A class extending HTMLEditorKit.Parser to create a new version of ParserDelegator
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class OpenHTMLDocument extends HTMLDocument {

    /**
     * Default constructor
     */
    public OpenHTMLDocument() {
        super();
    }

    /**
     * Constructor with Content and StyleSheet parameters
     *
     * @param c
     * @param styles
     */
    public OpenHTMLDocument(AbstractDocument.Content c, StyleSheet styles) {
        super(c, styles);
    }

    /**
     * Constructor with StyleSheet parameter
     *
     * @param styles
     */
    public OpenHTMLDocument(StyleSheet styles) {
        super(styles);
    }

    @Override
    public HTMLEditorKit.ParserCallback getReader(int pos) {
        Object desc = getProperty(Document.StreamDescriptionProperty);
        if (desc instanceof URL) {
            setBase((URL) desc);
        }
        OpenHTMLReader reader = new OpenHTMLReader(pos);
        return reader;
    }

    public class OpenHTMLReader extends HTMLReader {

        public OpenHTMLReader(int offset) {
            this(offset, 0, 0, null);
        }
        
        public OpenHTMLReader(int offset, int popDepth, int pushDepth,
                          HTML.Tag insertTag) {
            this(offset, popDepth, pushDepth, insertTag, true, false, true);
        }
        
        public OpenHTMLReader(int offset, int popDepth, int pushDepth,
                   HTML.Tag insertTag, boolean insertInsertTag,
                   boolean insertAfterImplied, boolean wantsTrailingNewline) {
            emptyDocument = (getLength() == 0);
            isStyleCSS = "text/css".equals(getDefaultStyleSheetType());
            this.offset = offset;
            threshold = HTMLDocument.this.getTokenThreshold();
            tagMap = new Hashtable<HTML.Tag, TagAction>(57);
            TagAction na = new TagAction();
            TagAction ba = new BlockAction();
            TagAction pa = new ParagraphAction();
            TagAction ca = new CharacterAction();
            TagAction sa = new SpecialAction();
            TagAction fa = new FormAction();
            TagAction ha = new HiddenAction();
            TagAction conv = new HTMLReader.ConvertAction();

            // register handlers for the well known tags
            tagMap.put(HTML.Tag.A, new HTMLReader.AnchorAction());
            tagMap.put(HTML.Tag.ADDRESS, ca);
            tagMap.put(HTML.Tag.APPLET, ha);
            tagMap.put(HTML.Tag.AREA, new HTMLReader.AreaAction());
            tagMap.put(HTML.Tag.B, conv);
            tagMap.put(HTML.Tag.BASE, new HTMLReader.BaseAction());
            tagMap.put(HTML.Tag.BASEFONT, ca);
            tagMap.put(HTML.Tag.BIG, ca);
            tagMap.put(HTML.Tag.BLOCKQUOTE, ba);
            tagMap.put(HTML.Tag.BODY, ba);
            tagMap.put(HTML.Tag.BR, sa);
            tagMap.put(HTML.Tag.CAPTION, ba);
            tagMap.put(HTML.Tag.CENTER, ba);
            tagMap.put(HTML.Tag.CITE, ca);
            tagMap.put(HTML.Tag.CODE, ca);
            tagMap.put(HTML.Tag.DD, ba);
            tagMap.put(HTML.Tag.DFN, ca);
            tagMap.put(HTML.Tag.DIR, ba);
            tagMap.put(HTML.Tag.DIV, ba);
            tagMap.put(HTML.Tag.DL, ba);
            tagMap.put(HTML.Tag.DT, pa);
            tagMap.put(HTML.Tag.EM, ca);
            tagMap.put(HTML.Tag.FONT, conv);
            tagMap.put(HTML.Tag.FORM, new HTMLReader.FormTagAction());
            tagMap.put(HTML.Tag.FRAME, sa);
            tagMap.put(HTML.Tag.FRAMESET, ba);
            tagMap.put(HTML.Tag.H1, pa);
            tagMap.put(HTML.Tag.H2, pa);
            tagMap.put(HTML.Tag.H3, pa);
            tagMap.put(HTML.Tag.H4, pa);
            tagMap.put(HTML.Tag.H5, pa);
            tagMap.put(HTML.Tag.H6, pa);
            tagMap.put(HTML.Tag.HEAD, new HTMLReader.HeadAction());
            tagMap.put(HTML.Tag.HR, sa);
            tagMap.put(HTML.Tag.HTML, ba);
            tagMap.put(HTML.Tag.I, conv);
            tagMap.put(HTML.Tag.IMG, sa);
            tagMap.put(HTML.Tag.INPUT, fa);
            tagMap.put(HTML.Tag.ISINDEX, new IsindexAction());
            tagMap.put(HTML.Tag.KBD, ca);
            tagMap.put(HTML.Tag.LI, ba);
            tagMap.put(HTML.Tag.LINK, new HTMLReader.LinkAction());
            tagMap.put(HTML.Tag.MAP, new HTMLReader.MapAction());
            tagMap.put(HTML.Tag.MENU, ba);
            tagMap.put(HTML.Tag.META, new HTMLReader.MetaAction());
            tagMap.put(HTML.Tag.NOBR, ca);
            tagMap.put(HTML.Tag.NOFRAMES, ba);
            tagMap.put(HTML.Tag.OBJECT, sa);
            tagMap.put(HTML.Tag.OL, ba);
            tagMap.put(HTML.Tag.OPTION, fa);
            tagMap.put(HTML.Tag.P, pa);
            tagMap.put(HTML.Tag.PARAM, new HTMLReader.ObjectAction());
            tagMap.put(HTML.Tag.PRE, new PreAction());
            tagMap.put(HTML.Tag.SAMP, ca);
            tagMap.put(HTML.Tag.SCRIPT, ha);
            tagMap.put(HTML.Tag.SELECT, fa);
            tagMap.put(HTML.Tag.SMALL, ca);
            tagMap.put(HTML.Tag.SPAN, ca);
            tagMap.put(HTML.Tag.STRIKE, conv);
            tagMap.put(HTML.Tag.S, ca);
            tagMap.put(HTML.Tag.STRONG, ca);
            tagMap.put(HTML.Tag.STYLE, new HTMLReader.StyleAction());
            tagMap.put(HTML.Tag.SUB, conv);
            tagMap.put(HTML.Tag.SUP, conv);
            tagMap.put(HTML.Tag.TABLE, ba);
            tagMap.put(HTML.Tag.TD, ba);
            tagMap.put(HTML.Tag.TEXTAREA, fa);
            tagMap.put(HTML.Tag.TH, ba);
            tagMap.put(HTML.Tag.TITLE, new HTMLReader.TitleAction());
            tagMap.put(HTML.Tag.TR, ba);
            tagMap.put(HTML.Tag.TT, ca);
            tagMap.put(HTML.Tag.U, conv);
            tagMap.put(HTML.Tag.UL, ba);
            tagMap.put(HTML.Tag.VAR, ca);

            if (insertTag != null) {
                this.insertTag = insertTag;
                this.popDepth = popDepth;
                this.pushDepth = pushDepth;
                this.insertInsertTag = insertInsertTag;
                foundInsertTag = false;
            }
            else {
                foundInsertTag = true;
            }
            if (insertAfterImplied) {
                this.popDepth = popDepth;
                this.pushDepth = pushDepth;
                this.insertAfterImplied = true;
                foundInsertTag = false;
                midInsert = false;
                this.insertInsertTag = true;
                this.wantsTrailingNewline = wantsTrailingNewline;
            }
            else {
                midInsert = (!emptyDocument && insertTag == null);
                if (midInsert) {
                    generateEndsSpecsForMidInsert();
                }
            }

            /**
             * This block initializes the <code>inParagraph</code> flag.
             * It is left in <code>false</code> value automatically
             * if the target document is empty or future inserts
             * were positioned into the 'body' tag.
             */
            if (!emptyDocument && !midInsert) {
                int targetOffset = Math.max(this.offset - 1, 0);
                Element elem =
                        HTMLDocument.this.getCharacterElement(targetOffset);
                /* Going up by the left document structure path */
                for (int i = 0; i <= this.popDepth; i++) {
                    elem = elem.getParentElement();
                }
                /* Going down by the right document structure path */
                for (int i = 0; i < this.pushDepth; i++) {
                    int index = elem.getElementIndex(this.offset);
                    elem = elem.getElement(index);
                }
                AttributeSet attrs = elem.getAttributes();
                if (attrs != null) {
                    HTML.Tag tagToInsertInto =
                            (HTML.Tag) attrs.getAttribute(StyleConstants.NameAttribute);
                    if (tagToInsertInto != null) {
                        this.inParagraph = tagToInsertInto.isParagraph();
                    }
                }
            }
        }
    }
}
