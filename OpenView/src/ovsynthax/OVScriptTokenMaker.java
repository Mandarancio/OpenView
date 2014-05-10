package ovsynthax;

/* The following code was generated by JFlex 1.5.0-SNAPSHOT */
import java.io.IOException;
import java.io.Reader;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexCTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;

/**
 * This class is a scanner generated by <a href="http://www.jflex.de/">JFlex</a>
 * 1.5.0-SNAPSHOT from the specification file
 * <tt>/home/martino/git/OpenView/OpenView/src/ovsynthax/OVScriptTokenMaker.flex</tt>
 */
public class OVScriptTokenMaker extends AbstractJFlexCTokenMaker {

    /**
     * This character denotes the end of file
     */
    public static final int YYEOF = -1;

    /**
     * initial size of the lookahead buffer
     */
    private static final int ZZ_BUFFERSIZE = 16384;

    /**
     * lexical states
     */
    public static final int YYINITIAL = 0;
    public static final int MLC = 2;

    /**
     * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
     * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l at the
     * beginning of a line l is of the form l = 2*k, k a non negative integer
     */
    private static final int ZZ_LEXSTATE[] = {
        0, 0, 1, 1
    };

    /**
     * Translates characters to character classes
     */
    private static final String ZZ_CMAP_PACKED
            = "\11\0\1\11\1\7\1\13\1\14\1\12\22\0\1\11\1\64\1\4"
            + "\1\10\1\0\1\65\1\67\1\3\2\16\1\65\1\70\1\6\1\71"
            + "\1\20\1\65\2\2\1\56\7\2\1\64\1\6\1\72\1\66\1\73"
            + "\1\64\1\17\1\42\1\50\1\1\1\54\1\51\3\1\1\43\1\1"
            + "\1\52\1\53\1\1\1\47\1\1\1\45\1\1\1\46\1\1\1\44"
            + "\2\1\1\41\3\1\1\16\1\5\1\16\1\64\1\1\1\0\1\36"
            + "\1\55\1\24\1\34\1\31\1\21\1\61\1\40\1\26\2\1\1\32"
            + "\1\62\1\23\1\27\1\60\1\63\1\30\1\33\1\25\1\22\1\35"
            + "\1\37\1\57\2\1\1\15\1\74\1\15\1\64\6\0\1\13\u1fa2\0"
            + "\1\13\1\13\udfd6\0";

    /**
     * Translates characters to character classes
     */
    private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

    /**
     * Translates DFA states to action switch labels.
     */
    private static final int[] ZZ_ACTION = zzUnpackAction();

    private static final String ZZ_ACTION_PACKED_0
            = "\2\0\2\1\1\2\1\3\1\4\1\5\1\6\1\7"
            + "\1\10\1\11\15\1\10\12\1\13\1\3\1\14\1\3"
            + "\1\4\1\15\2\4\1\11\5\0\6\1\1\16\22\1"
            + "\1\3\1\17\1\14\1\3\1\20\1\4\5\0\2\1"
            + "\1\21\6\1\1\21\5\1\4\0\1\1\1\22\3\1"
            + "\1\21\1\0\5\1";

    private static int[] zzUnpackAction() {
        int[] result = new int[114];
        int offset = 0;
        offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAction(String packed, int offset, int[] result) {
        int i = 0;       /* index in packed string  */

        int j = offset;  /* index in unpacked array */

        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }

    /**
     * Translates a state to a row index in the transition table
     */
    private static final int[] ZZ_ROWMAP = zzUnpackRowMap();

    private static final String ZZ_ROWMAP_PACKED_0
            = "\0\0\0\75\0\75\0\172\0\267\0\364\0\u0131\0\75"
            + "\0\u016e\0\u01ab\0\75\0\u01e8\0\u0225\0\u0262\0\u029f\0\u02dc"
            + "\0\u0319\0\u0356\0\u0393\0\u03d0\0\u040d\0\u044a\0\u0487\0\u04c4"
            + "\0\u0501\0\75\0\u053e\0\u057b\0\u05b8\0\u05f5\0\u0632\0\u066f"
            + "\0\u06ac\0\u06e9\0\u0726\0\75\0\u0763\0\u07a0\0\75\0\u07dd"
            + "\0\u081a\0\75\0\u0857\0\u0894\0\u08d1\0\u090e\0\u094b\0\u0988"
            + "\0\u09c5\0\u0a02\0\u0a3f\0\u0a7c\0\u0ab9\0\172\0\u0af6\0\u0b33"
            + "\0\u0b70\0\u0bad\0\u0bea\0\u0c27\0\u0c64\0\u0ca1\0\u0cde\0\u0d1b"
            + "\0\u0d58\0\u0d95\0\u0dd2\0\u0e0f\0\u0e4c\0\u0e89\0\u0ec6\0\u0f03"
            + "\0\u0f40\0\75\0\u07a0\0\u0f7d\0\75\0\u0fba\0\u0ff7\0\u1034"
            + "\0\u1071\0\u10ae\0\u10eb\0\u1128\0\u1165\0\172\0\u11a2\0\u11df"
            + "\0\u121c\0\u1259\0\u1296\0\u12d3\0\u11df\0\u1310\0\u134d\0\u138a"
            + "\0\u13c7\0\u1404\0\u1441\0\u147e\0\u14bb\0\u14f8\0\u1535\0\172"
            + "\0\u1572\0\u15af\0\u15ec\0\u1629\0\u1666\0\u16a3\0\u16e0\0\u171d"
            + "\0\u175a\0\u1797";

    private static int[] zzUnpackRowMap() {
        int[] result = new int[114];
        int offset = 0;
        offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackRowMap(String packed, int offset, int[] result) {
        int i = 0;  /* index in packed string  */

        int j = offset;  /* index in unpacked array */

        int l = packed.length();
        while (i < l) {
            int high = packed.charAt(i++) << 16;
            result[j++] = high | packed.charAt(i++);
        }
        return j;
    }

    /**
     * The transition table of the DFA
     */
    private static final int[] ZZ_TRANS = zzUnpackTrans();

    private static final String ZZ_TRANS_PACKED_0
            = "\1\3\1\4\1\5\1\6\1\7\2\3\1\10\1\11"
            + "\1\12\2\0\1\12\2\13\1\14\1\3\1\15\2\4"
            + "\1\16\1\17\1\20\1\4\1\21\1\22\1\23\1\24"
            + "\1\4\1\25\1\26\1\27\16\4\1\5\1\4\1\30"
            + "\1\4\1\31\1\4\1\32\2\33\1\34\1\35\1\36"
            + "\1\37\1\40\1\41\76\0\2\4\16\0\43\4\11\0"
            + "\2\42\1\5\2\0\1\42\2\0\1\42\2\0\1\42"
            + "\1\0\1\42\1\0\1\42\1\0\35\42\1\5\5\42"
            + "\11\0\3\43\1\44\1\43\1\45\1\43\1\46\65\43"
            + "\4\7\1\47\1\50\1\7\1\51\65\7\7\11\1\0"
            + "\2\11\3\0\60\11\11\0\1\12\2\0\1\12\100\0"
            + "\1\52\20\0\1\53\1\54\2\0\1\55\2\0\1\56"
            + "\1\57\24\0\2\4\16\0\1\4\1\60\4\4\1\61"
            + "\6\4\1\62\25\4\12\0\2\4\16\0\6\4\1\63"
            + "\34\4\12\0\2\4\16\0\7\4\1\64\5\4\1\65"
            + "\25\4\12\0\2\4\16\0\1\66\40\4\1\67\1\4"
            + "\12\0\2\4\16\0\10\4\1\70\4\4\1\71\25\4"
            + "\12\0\2\4\16\0\2\4\1\72\6\4\1\73\24\4"
            + "\1\74\4\4\12\0\2\4\16\0\6\4\1\75\1\4"
            + "\1\76\32\4\12\0\2\4\16\0\5\4\1\65\34\4"
            + "\1\77\12\0\2\4\16\0\15\4\1\61\25\4\12\0"
            + "\2\4\16\0\3\4\1\16\1\100\4\4\1\101\1\102"
            + "\1\103\20\4\1\63\6\4\12\0\2\4\16\0\15\4"
            + "\1\104\1\4\1\105\23\4\12\0\2\4\16\0\6\4"
            + "\1\106\1\107\33\4\12\0\2\4\16\0\5\4\1\65"
            + "\7\4\1\110\25\4\77\0\1\32\75\0\1\32\73\0"
            + "\1\32\1\0\1\32\72\0\1\32\2\0\1\32\75\0"
            + "\1\33\75\0\1\33\75\0\1\32\3\42\2\0\1\42"
            + "\2\0\1\42\2\0\1\42\1\0\1\42\1\0\1\42"
            + "\1\0\43\42\11\0\3\111\1\112\1\111\1\45\1\111"
            + "\1\46\70\111\1\113\3\111\1\0\2\111\3\114\60\111"
            + "\3\46\1\115\1\46\1\116\67\46\7\51\1\0\2\51"
            + "\3\0\64\51\1\115\1\50\67\51\42\0\1\117\105\0"
            + "\1\120\67\0\1\121\74\0\1\122\75\0\1\123\26\0"
            + "\2\4\16\0\2\4\1\124\40\4\12\0\2\4\16\0"
            + "\7\4\1\66\33\4\12\0\2\4\16\0\11\4\1\125"
            + "\31\4\12\0\2\4\16\0\12\4\1\126\30\4\12\0"
            + "\2\4\16\0\1\4\1\127\41\4\12\0\2\4\16\0"
            + "\2\4\1\126\40\4\12\0\2\4\16\0\37\4\1\130"
            + "\3\4\12\0\2\4\16\0\4\4\1\131\34\4\1\132"
            + "\1\4\12\0\2\4\16\0\2\4\1\103\40\4\12\0"
            + "\2\4\16\0\13\4\1\66\27\4\12\0\2\4\16\0"
            + "\5\4\1\133\4\4\1\134\30\4\12\0\2\4\16\0"
            + "\37\4\1\135\3\4\12\0\2\4\16\0\40\4\1\126"
            + "\2\4\12\0\2\4\16\0\2\4\1\136\40\4\12\0"
            + "\2\4\16\0\7\4\1\137\33\4\12\0\2\4\16\0"
            + "\15\4\1\140\25\4\12\0\2\4\16\0\10\4\1\77"
            + "\32\4\12\0\2\4\16\0\5\4\1\65\35\4\12\0"
            + "\2\4\16\0\13\4\1\126\27\4\12\0\2\4\16\0"
            + "\5\4\1\137\35\4\12\0\2\4\16\0\5\4\1\141"
            + "\35\4\12\0\2\4\16\0\16\4\1\126\24\4\12\0"
            + "\2\4\16\0\5\4\1\142\35\4\12\0\2\4\16\0"
            + "\36\4\1\126\4\4\11\0\3\111\1\44\1\111\1\45"
            + "\1\111\1\46\65\111\3\114\1\44\3\114\1\0\65\114"
            + "\7\46\1\0\2\46\3\0\60\46\43\0\1\143\102\0"
            + "\1\144\66\0\1\145\102\0\1\146\77\0\1\52\21\0"
            + "\2\4\16\0\3\4\1\147\37\4\12\0\2\4\16\0"
            + "\12\4\1\127\30\4\12\0\2\4\16\0\10\4\1\150"
            + "\32\4\12\0\2\4\16\0\6\4\1\77\34\4\12\0"
            + "\2\4\16\0\1\4\1\151\41\4\12\0\2\4\16\0"
            + "\6\4\1\152\34\4\12\0\2\4\16\0\1\66\42\4"
            + "\12\0\2\4\16\0\10\4\1\66\32\4\12\0\2\4"
            + "\16\0\40\4\1\153\2\4\12\0\2\4\16\0\4\4"
            + "\1\126\36\4\12\0\2\4\16\0\2\4\1\154\40\4"
            + "\12\0\2\4\16\0\11\4\1\134\31\4\12\0\2\4"
            + "\16\0\2\4\1\137\40\4\55\0\1\52\76\0\1\143"
            + "\75\0\1\143\67\0\1\155\33\0\2\4\16\0\4\4"
            + "\1\156\36\4\12\0\2\4\16\0\7\4\1\157\33\4"
            + "\12\0\2\4\16\0\14\4\1\160\26\4\12\0\2\4"
            + "\16\0\4\4\1\161\36\4\12\0\2\4\16\0\35\4"
            + "\1\126\5\4\63\0\1\52\23\0\2\4\16\0\5\4"
            + "\1\162\35\4\12\0\2\4\16\0\2\4\1\66\40\4"
            + "\12\0\2\4\16\0\10\4\1\126\32\4\12\0\2\4"
            + "\16\0\17\4\1\126\23\4\12\0\2\4\16\0\6\4"
            + "\1\157\34\4\11\0";

    private static int[] zzUnpackTrans() {
        int[] result = new int[6100];
        int offset = 0;
        offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackTrans(String packed, int offset, int[] result) {
        int i = 0;       /* index in packed string  */

        int j = offset;  /* index in unpacked array */

        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            value--;
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }


    /* error codes */
    private static final int ZZ_UNKNOWN_ERROR = 0;
    private static final int ZZ_NO_MATCH = 1;
    private static final int ZZ_PUSHBACK_2BIG = 2;

    /* error messages for the codes above */
    private static final String ZZ_ERROR_MSG[] = {
        "Unkown internal scanner error",
        "Error: could not match input",
        "Error: pushback value was too large"
    };

    /**
     * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
     */
    private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

    private static final String ZZ_ATTRIBUTE_PACKED_0
            = "\1\0\1\10\1\11\4\1\1\11\2\1\1\11\16\1"
            + "\1\11\11\1\1\11\2\1\1\11\2\1\1\11\5\0"
            + "\32\1\1\11\2\1\1\11\1\1\5\0\17\1\4\0"
            + "\6\1\1\0\5\1";

    private static int[] zzUnpackAttribute() {
        int[] result = new int[114];
        int offset = 0;
        offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAttribute(String packed, int offset, int[] result) {
        int i = 0;       /* index in packed string  */

        int j = offset;  /* index in unpacked array */

        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do {
                result[j++] = value;
            } while (--count > 0);
        }
        return j;
    }

    /**
     * the input device
     */
    private java.io.Reader zzReader;

    /**
     * the current state of the DFA
     */
    private int zzState;

    /**
     * the current lexical state
     */
    private int zzLexicalState = YYINITIAL;

    /**
     * this buffer contains the current text to be matched and is the source of
     * the yytext() string
     */
    private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

    /**
     * the textposition at the last accepting state
     */
    private int zzMarkedPos;

    /**
     * the current text position in the buffer
     */
    private int zzCurrentPos;

    /**
     * startRead marks the beginning of the yytext() string in the buffer
     */
    private int zzStartRead;

    /**
     * endRead marks the last character in the buffer, that has been read from
     * input
     */
    private int zzEndRead;

    /**
     * number of newlines encountered up to the start of the matched text
     */
    private int yyline;

    /**
     * the number of characters up to the start of the matched text
     */
    private int yychar;

    /**
     * the number of characters from the last newline up to the start of the
     * matched text
     */
    private int yycolumn;

    /**
     * zzAtBOL == true <=> the scanner is currently at the beginning of a line
     */
    private boolean zzAtBOL = true;

    /**
     * zzAtEOF == true <=> the scanner is at the EOF
     */
    private boolean zzAtEOF;

    /**
     * denotes if the user-EOF-code has already been executed
     */
    private boolean zzEOFDone;

    /* user code: */
    /**
     * Constructor. This must be here because JFlex does not generate a
     * no-parameter constructor.
     */
    public OVScriptTokenMaker() {
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param tokenType The token's type.
     * @see #addToken(int, int, int)
     */
    private void addHyperlinkToken(int start, int end, int tokenType) {
        int so = start + offsetShift;
        addToken(zzBuffer, start, end, tokenType, so, true);
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param tokenType The token's type.
     */
    private void addToken(int tokenType) {
        addToken(zzStartRead, zzMarkedPos - 1, tokenType);
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param tokenType The token's type.
     * @see #addHyperlinkToken(int, int, int)
     */
    private void addToken(int start, int end, int tokenType) {
        int so = start + offsetShift;
        addToken(zzBuffer, start, end, tokenType, so, false);
    }

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param array The character array.
     * @param start The starting offset in the array.
     * @param end The ending offset in the array.
     * @param tokenType The token's type.
     * @param startOffset The offset in the document at which this token occurs.
     * @param hyperlink Whether this token is a hyperlink.
     */
    public void addToken(char[] array, int start, int end, int tokenType,
            int startOffset, boolean hyperlink) {
        super.addToken(array, start, end, tokenType, startOffset, hyperlink);
        zzStartRead = zzMarkedPos;
    }

    /**
     * Returns the text to place at the beginning and end of a line to "comment"
     * it in a this programming language.
     *
     * @return The start and end strings to add to a line to "comment" it out.
     */
    public String[] getLineCommentStartAndEnd() {
        return new String[]{"//", null};
    }

    /**
     * Returns the first token in the linked list of tokens generated from
     * <code>text</code>. This method must be implemented by subclasses so they
     * can correctly implement syntax highlighting.
     *
     * @param text The text from which to get tokens.
     * @param initialTokenType The token type we should start with.
     * @param startOffset The offset into the document at which
     * <code>text</code> starts.
     * @return The first <code>Token</code> in a linked list representing the
     * syntax highlighted text.
     */
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

        resetTokenList();
        this.offsetShift = -text.offset + startOffset;

        // Start off in the proper state.
        int state = Token.NULL;
        switch (initialTokenType) {
            case Token.COMMENT_MULTILINE:
                state = MLC;
                start = text.offset;
                break;

            /* No documentation comments */
            default:
                state = Token.NULL;
        }

        s = text;
        try {
            yyreset(zzReader);
            yybegin(state);
            return yylex();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new TokenImpl();
        }

    }

    /**
     * Refills the input buffer.
     *
     * @return      <code>true</code> if EOF was reached, otherwise
     * <code>false</code>.
     */
    private boolean zzRefill() {
        return zzCurrentPos >= s.offset + s.count;
    }

    /**
     * Resets the scanner to read from a new input stream. Does not close the
     * old reader.
     *
     * All internal variables are reset, the old input stream
     * <b>cannot</b> be reused (internal buffer is discarded and lost). Lexical
     * state is set to <tt>YY_INITIAL</tt>.
     *
     * @param reader the new input stream
     */
    public final void yyreset(Reader reader) {
        // 's' has been updated.
        zzBuffer = s.array;
        /*
         * We replaced the line below with the two below it because zzRefill
         * no longer "refills" the buffer (since the way we do it, it's always
         * "full" the first time through, since it points to the segment's
         * array).  So, we assign zzEndRead here.
         */
        //zzStartRead = zzEndRead = s.offset;
        zzStartRead = s.offset;
        zzEndRead = zzStartRead + s.count - 1;
        zzCurrentPos = zzMarkedPos  = s.offset;
        zzLexicalState = YYINITIAL;
        zzReader = reader;
        zzAtBOL = true;
        zzAtEOF = false;
    }

    /**
     * Creates a new scanner There is also a java.io.InputStream version of this
     * constructor.
     *
     * @param in the java.io.Reader to read input from.
     */
    public OVScriptTokenMaker(java.io.Reader in) {
        this.zzReader = in;
    }

    /**
     * Creates a new scanner. There is also java.io.Reader version of this
     * constructor.
     *
     * @param in the java.io.Inputstream to read input from.
     */
    public OVScriptTokenMaker(java.io.InputStream in) {
        this(new java.io.InputStreamReader(in, java.nio.charset.Charset.forName("UTF-8")));
    }

    /**
     * Unpacks the compressed character translation table.
     *
     * @param packed the packed character translation table
     * @return the unpacked character translation table
     */
    private static char[] zzUnpackCMap(String packed) {
        char[] map = new char[0x10000];
        int i = 0;  /* index in packed string  */

        int j = 0;  /* index in unpacked array */

        while (i < 186) {
            int count = packed.charAt(i++);
            char value = packed.charAt(i++);
            do {
                map[j++] = value;
            } while (--count > 0);
        }
        return map;
    }

    /**
     * Refills the input buffer.
     *
     * @return      <code>false</code>, iff there was new input.
     *
     * @exception java.io.IOException if any I/O-Error occurs
     */
    private boolean zzzRefill() throws java.io.IOException {

        /* first: make room (if you can) */
        if (zzStartRead > 0) {
            System.arraycopy(zzBuffer, zzStartRead,
                    zzBuffer, 0,
                    zzEndRead - zzStartRead);

            /* translate stored positions */
            zzEndRead -= zzStartRead;
            zzCurrentPos -= zzStartRead;
            zzMarkedPos -= zzStartRead;
            zzStartRead = 0;
        }

        /* is the buffer big enough? */
        if (zzCurrentPos >= zzBuffer.length) {
            /* if not: blow it up */
            char newBuffer[] = new char[zzCurrentPos * 2];
            System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
            zzBuffer = newBuffer;
        }

        /* finally: fill the buffer with new input */
        int numRead = zzReader.read(zzBuffer, zzEndRead,
                zzBuffer.length - zzEndRead);

        if (numRead > 0) {
            zzEndRead += numRead;
            return false;
        }
        // unlikely but not impossible: read 0 characters, but not at end of stream    
        if (numRead == 0) {
            int c = zzReader.read();
            if (c == -1) {
                return true;
            } else {
                zzBuffer[zzEndRead++] = (char) c;
                return false;
            }
        }

        // numRead < 0
        return true;
    }

    /**
     * Closes the input stream.
     */
    public final void yyclose() throws java.io.IOException {
        zzAtEOF = true;            /* indicate end of file */

        zzEndRead = zzStartRead;  /* invalidate buffer    */

        if (zzReader != null) {
            zzReader.close();
        }
    }

    /**
     * Resets the scanner to read from a new input stream. Does not close the
     * old reader.
     *
     * All internal variables are reset, the old input stream
     * <b>cannot</b> be reused (internal buffer is discarded and lost). Lexical
     * state is set to <tt>ZZ_INITIAL</tt>.
     *
     * Internal scan buffer is resized down to its initial length, if it has
     * grown.
     *
     * @param reader the new input stream
     */
    public final void yyyreset(java.io.Reader reader) {
        zzReader = reader;
        zzAtBOL = true;
        zzAtEOF = false;
        zzEOFDone = false;
        zzEndRead = zzStartRead = 0;
        zzCurrentPos = zzMarkedPos = 0;
        yyline = yychar = yycolumn = 0;
        zzLexicalState = YYINITIAL;
        if (zzBuffer.length > ZZ_BUFFERSIZE) {
            zzBuffer = new char[ZZ_BUFFERSIZE];
        }
    }

    /**
     * Returns the current lexical state.
     */
    public final int yystate() {
        return zzLexicalState;
    }

    /**
     * Enters a new lexical state
     *
     * @param newState the new lexical state
     */
    public final void yybegin(int newState) {
        zzLexicalState = newState;
    }

    /**
     * Returns the text matched by the current regular expression.
     */
    public final String yytext() {
        return new String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead);
    }

    /**
     * Returns the character at position <tt>pos</tt> from the matched text.
     *
     * It is equivalent to yytext().charAt(pos), but faster
     *
     * @param pos the position of the character to fetch. A value from 0 to
     * yylength()-1.
     *
     * @return the character at position pos
     */
    public final char yycharat(int pos) {
        return zzBuffer[zzStartRead + pos];
    }

    /**
     * Returns the length of the matched text region.
     */
    public final int yylength() {
        return zzMarkedPos - zzStartRead;
    }

    /**
     * Reports an error that occured while scanning.
     *
     * In a wellformed scanner (no or only correct usage of yypushback(int) and
     * a match-all fallback rule) this method will only be called with things
     * that "Can't Possibly Happen". If this method is called, something is
     * seriously wrong (e.g. a JFlex bug producing a faulty scanner etc.).
     *
     * Usual syntax/scanner level error handling should be done in error
     * fallback rules.
     *
     * @param errorCode the code of the errormessage to display
     */
    private void zzScanError(int errorCode) {
        String message;
        try {
            message = ZZ_ERROR_MSG[errorCode];
        } catch (ArrayIndexOutOfBoundsException e) {
            message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
        }

        throw new Error(message);
    }

    /**
     * Pushes the specified amount of characters back into the input stream.
     *
     * They will be read again by then next call of the scanning method
     *
     * @param number the number of characters to be read again. This number must
     * not be greater than yylength()!
     */
    public void yypushback(int number) {
        if (number > yylength()) {
            zzScanError(ZZ_PUSHBACK_2BIG);
        }

        zzMarkedPos -= number;
    }

    /**
     * Resumes scanning until the next regular expression is matched, the end of
     * input is encountered or an I/O-Error occurs.
     *
     * @return the next token
     * @exception java.io.IOException if any I/O-Error occurs
     */
    public org.fife.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
        int zzInput;
        int zzAction;

        // cached fields:
        int zzCurrentPosL;
        int zzMarkedPosL;
        int zzEndReadL = zzEndRead;
        char[] zzBufferL = zzBuffer;
        char[] zzCMapL = ZZ_CMAP;

        int[] zzTransL = ZZ_TRANS;
        int[] zzRowMapL = ZZ_ROWMAP;
        int[] zzAttrL = ZZ_ATTRIBUTE;

        while (true) {
            zzMarkedPosL = zzMarkedPos;

            zzAction = -1;

            zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

            zzState = ZZ_LEXSTATE[zzLexicalState];

            // set up zzAction for empty match case:
            int zzAttributes = zzAttrL[zzState];
            if ((zzAttributes & 1) == 1) {
                zzAction = zzState;
            }

            zzForAction:
            {
                while (true) {

                    if (zzCurrentPosL < zzEndReadL) {
                        zzInput = zzBufferL[zzCurrentPosL++];
                    } else if (zzAtEOF) {
                        zzInput = YYEOF;
                        break zzForAction;
                    } else {
                        // store back cached positions
                        zzCurrentPos = zzCurrentPosL;
                        zzMarkedPos = zzMarkedPosL;
                        boolean eof = zzRefill();
                        // get translated positions and possibly new buffer
                        zzCurrentPosL = zzCurrentPos;
                        zzMarkedPosL = zzMarkedPos;
                        zzBufferL = zzBuffer;
                        zzEndReadL = zzEndRead;
                        if (eof) {
                            zzInput = YYEOF;
                            break zzForAction;
                        } else {
                            zzInput = zzBufferL[zzCurrentPosL++];
                        }
                    }
                    int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput]];
                    if (zzNext == -1) {
                        break zzForAction;
                    }
                    zzState = zzNext;

                    zzAttributes = zzAttrL[zzState];
                    if ((zzAttributes & 1) == 1) {
                        zzAction = zzState;
                        zzMarkedPosL = zzCurrentPosL;
                        if ((zzAttributes & 8) == 8) {
                            break zzForAction;
                        }
                    }

                }
            }

            // store back cached position
            zzMarkedPos = zzMarkedPosL;

            switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
                case 1: {
                    addToken(Token.IDENTIFIER);
                }
                case 19:
                    break;
                case 2: {
                    addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
                }
                case 20:
                    break;
                case 3: {
                    addToken(Token.ERROR_CHAR);
                    addNullToken();
                    return firstToken;
                }
                case 21:
                    break;
                case 4: {
                    addToken(Token.ERROR_STRING_DOUBLE);
                    addNullToken();
                    return firstToken;
                }
                case 22:
                    break;
                case 5: {
                    addNullToken();
                    return firstToken;
                }
                case 23:
                    break;
                case 6: {
                    addToken(Token.COMMENT_EOL);
                    addNullToken();
                    return firstToken;
                }
                case 24:
                    break;
                case 7: {
                    addToken(Token.WHITESPACE);
                }
                case 25:
                    break;
                case 8: {
                    addToken(Token.SEPARATOR);
                }
                case 26:
                    break;
                case 9: {
                    addToken(Token.PREPROCESSOR);
                }
                case 27:
                    break;
                case 10: {
                    addToken(Token.OPERATOR);
                }
                case 28:
                    break;
                case 11: {
                    addToken(Token.ERROR_NUMBER_FORMAT);
                }
                case 29:
                    break;
                case 12: {
                    addToken(Token.ERROR_CHAR);
                }
                case 30:
                    break;
                case 13: {
                    addToken(Token.LITERAL_STRING_DOUBLE_QUOTE);
                }
                case 31:
                    break;
                case 14: {
                    addToken(Token.RESERVED_WORD);
                }
                case 32:
                    break;
                case 15: {
                    addToken(Token.LITERAL_CHAR);
                }
                case 33:
                    break;
                case 16: {
                    addToken(Token.ERROR_STRING_DOUBLE);
                }
                case 34:
                    break;
                case 17: {
                    addToken(Token.FUNCTION);
                }
                case 35:
                    break;
                case 18: {
                    addToken(Token.LITERAL_BOOLEAN);
                }
                case 36:
                    break;
                default:
                    if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                        zzAtEOF = true;
                        switch (zzLexicalState) {
                            case YYINITIAL: {
                                addNullToken();
                                return firstToken;
                            }
                            case 115:
                                break;
                            default:
                                return null;
                        }
                    } else {
                        zzScanError(ZZ_NO_MATCH);
                    }
            }
        }
    }

}
