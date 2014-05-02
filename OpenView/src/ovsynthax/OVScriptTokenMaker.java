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

	/** This character denotes the end of file */
	public static final int YYEOF = -1;

	/** initial size of the lookahead buffer */
	private static final int ZZ_BUFFERSIZE = 16384;

	/** lexical states */
	public static final int YYINITIAL = 0;
	public static final int MLC = 2;

	/**
	 * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
	 * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l at the
	 * beginning of a line l is of the form l = 2*k, k a non negative integer
	 */
	private static final int ZZ_LEXSTATE[] = { 0, 0, 1, 1 };

	/**
	 * Translates characters to character classes
	 */
	private static final String ZZ_CMAP_PACKED = "\11\0\1\10\1\11\1\13\1\14\1\12\22\0\1\10\1\62\1\4"
			+ "\1\7\1\0\1\63\1\65\1\3\2\16\1\63\1\66\1\6\1\67"
			+ "\1\6\1\63\2\2\1\54\7\2\1\62\1\6\1\70\1\64\1\71"
			+ "\1\62\1\37\1\41\1\47\1\1\1\52\1\50\3\1\1\42\1\1"
			+ "\1\51\2\1\1\46\1\1\1\44\1\1\1\45\1\1\1\43\2\1"
			+ "\1\40\3\1\1\16\1\5\1\16\1\62\1\1\1\0\1\34\1\53"
			+ "\1\22\1\32\1\27\1\17\1\57\1\36\1\24\2\1\1\30\1\60"
			+ "\1\21\1\25\1\56\1\61\1\26\1\31\1\23\1\20\1\33\1\35"
			+ "\1\55\2\1\1\15\1\72\1\15\1\62\6\0\1\13\u1fa2\0\1\13"
			+ "\1\13\udfd6\0";

	/**
	 * Translates characters to character classes
	 */
	private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

	/**
	 * Translates DFA states to action switch labels.
	 */
	private static final int[] ZZ_ACTION = zzUnpackAction();

	private static final String ZZ_ACTION_PACKED_0 = "\2\0\2\1\1\2\1\3\1\4\1\5\1\6\1\7"
			+ "\13\1\1\10\2\1\10\11\1\12\1\3\1\13\2\3"
			+ "\1\14\4\1\1\15\12\1\4\0\3\1\1\3\1\16"
			+ "\1\3\1\14\1\0\1\17\1\16\1\13\1\3\1\14"
			+ "\1\1\1\20\6\1\4\0\1\1\1\13\1\21\2\1"
			+ "\1\20\3\0\1\10\2\1\1\0\1\1";

	private static int[] zzUnpackAction() {
		int[] result = new int[96];
		int offset = 0;
		offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackAction(String packed, int offset, int[] result) {
		int i = 0; /* index in packed string */
		int j = offset; /* index in unpacked array */
		int l = packed.length();
		while (i < l) {
			int count = packed.charAt(i++);
			int value = packed.charAt(i++);
			do
				result[j++] = value;
			while (--count > 0);
		}
		return j;
	}

	/**
	 * Translates a state to a row index in the transition table
	 */
	private static final int[] ZZ_ROWMAP = zzUnpackRowMap();

	private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\73\0\73\0\166\0\261\0\354\0\u0127\0\u0162"
			+ "\0\73\0\73\0\u019d\0\u01d8\0\u0213\0\u024e\0\u0289\0\u02c4"
			+ "\0\u02ff\0\u033a\0\u0375\0\u03b0\0\u03eb\0\u0426\0\u0461\0\u049c"
			+ "\0\73\0\u04d7\0\u0512\0\u054d\0\u0588\0\u05c3\0\u05fe\0\u0639"
			+ "\0\u0674\0\u06af\0\u06ea\0\u0725\0\u0760\0\u079b\0\u07d6\0\u0811"
			+ "\0\u084c\0\u0887\0\166\0\u08c2\0\u08fd\0\u0938\0\u0973\0\u09ae"
			+ "\0\u09e9\0\u0a24\0\u0a5f\0\u0a9a\0\u0ad5\0\u0b10\0\u0b4b\0\u0b86"
			+ "\0\u0bc1\0\u0bfc\0\u0c37\0\u0c72\0\u0cad\0\u06ea\0\u0ce8\0\u0d23"
			+ "\0\u06ea\0\u06ea\0\73\0\u0d23\0\u0d5e\0\u0d99\0\u0dd4\0\166"
			+ "\0\u0e0f\0\u0e4a\0\u0e85\0\u0ec0\0\u0efb\0\u0f36\0\u0f71\0\u0fac"
			+ "\0\u0fe7\0\u1022\0\u105d\0\73\0\73\0\u1098\0\u10d3\0\u110e"
			+ "\0\u1149\0\u1184\0\u11bf\0\73\0\u11fa\0\u1235\0\u1270\0\u12ab";

	private static int[] zzUnpackRowMap() {
		int[] result = new int[96];
		int offset = 0;
		offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackRowMap(String packed, int offset, int[] result) {
		int i = 0; /* index in packed string */
		int j = offset; /* index in unpacked array */
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

	private static final String ZZ_TRANS_PACKED_0 = "\1\3\1\4\1\5\1\6\3\3\1\7\1\10\1\11"
			+ "\2\0\1\10\2\12\1\13\2\4\1\14\1\15\1\16"
			+ "\1\4\1\17\1\20\1\21\1\22\1\4\1\23\1\24"
			+ "\1\25\1\4\1\26\14\4\1\5\1\4\1\27\1\4"
			+ "\1\30\1\4\1\31\2\32\1\33\1\34\1\35\1\36"
			+ "\1\37\1\40\74\0\2\4\14\0\20\4\1\0\22\4"
			+ "\11\0\2\41\1\5\2\0\1\41\1\0\1\41\3\0"
			+ "\1\41\1\0\1\41\1\0\35\41\1\5\5\41\11\0"
			+ "\3\42\1\43\1\44\1\45\3\42\1\46\61\42\11\7"
			+ "\4\0\56\7\10\0\1\10\3\0\1\10\57\0\2\4"
			+ "\14\0\1\4\1\47\4\4\1\50\11\4\1\0\22\4"
			+ "\12\0\2\4\14\0\6\4\1\51\11\4\1\0\22\4"
			+ "\12\0\2\4\14\0\15\4\1\52\2\4\1\0\22\4"
			+ "\12\0\2\4\14\0\1\53\17\4\1\0\22\4\12\0"
			+ "\2\4\14\0\10\4\1\54\4\4\1\55\2\4\1\0"
			+ "\22\4\12\0\2\4\14\0\2\4\1\56\6\4\1\57"
			+ "\6\4\1\0\15\4\1\60\4\4\12\0\2\4\14\0"
			+ "\6\4\1\61\11\4\1\0\22\4\12\0\2\4\14\0"
			+ "\5\4\1\52\12\4\1\0\21\4\1\62\12\0\2\4"
			+ "\14\0\15\4\1\50\2\4\1\0\22\4\12\0\2\4"
			+ "\14\0\3\4\1\14\1\63\5\4\1\64\5\4\1\0"
			+ "\13\4\1\51\6\4\12\0\2\4\14\0\17\4\1\65"
			+ "\1\0\22\4\51\0\1\66\3\0\1\67\2\0\1\70"
			+ "\1\71\23\0\2\4\14\0\6\4\1\72\1\73\10\4"
			+ "\1\0\22\4\12\0\2\4\14\0\5\4\1\52\7\4"
			+ "\1\74\2\4\1\0\22\4\75\0\1\31\73\0\1\31"
			+ "\71\0\1\31\1\0\1\31\70\0\1\31\2\0\1\31"
			+ "\73\0\1\32\73\0\1\32\73\0\1\31\3\41\2\0"
			+ "\1\41\1\0\1\41\3\0\1\41\1\0\1\41\1\0"
			+ "\43\41\11\0\3\75\1\76\1\77\1\45\3\75\1\100"
			+ "\61\75\3\101\1\102\2\0\3\101\1\0\61\101\3\77"
			+ "\1\103\1\77\1\45\3\77\1\100\64\77\1\104\5\77"
			+ "\1\0\3\105\56\77\3\100\1\103\1\100\1\106\65\100"
			+ "\1\0\2\4\14\0\2\4\1\107\15\4\1\0\22\4"
			+ "\12\0\2\4\14\0\7\4\1\53\10\4\1\0\22\4"
			+ "\12\0\2\4\14\0\12\4\1\110\5\4\1\0\22\4"
			+ "\12\0\2\4\14\0\2\4\1\110\15\4\1\0\22\4"
			+ "\12\0\2\4\14\0\4\4\1\111\13\4\1\0\22\4"
			+ "\12\0\2\4\14\0\2\4\1\112\15\4\1\0\22\4"
			+ "\12\0\2\4\14\0\13\4\1\53\4\4\1\0\22\4"
			+ "\12\0\2\4\14\0\5\4\1\16\4\4\1\113\5\4"
			+ "\1\0\22\4\12\0\2\4\14\0\20\4\1\0\16\4"
			+ "\1\110\3\4\12\0\2\4\14\0\20\4\1\0\17\4"
			+ "\1\110\2\4\12\0\2\4\14\0\7\4\1\114\10\4"
			+ "\1\0\22\4\12\0\2\4\14\0\15\4\1\115\2\4"
			+ "\1\0\22\4\12\0\2\4\14\0\5\4\1\52\12\4"
			+ "\1\0\22\4\12\0\2\4\14\0\5\4\1\116\12\4"
			+ "\1\0\22\4\52\0\1\117\76\0\1\120\72\0\1\121"
			+ "\73\0\1\122\25\0\2\4\14\0\16\4\1\110\1\4"
			+ "\1\0\22\4\12\0\2\4\14\0\5\4\1\123\12\4"
			+ "\1\0\22\4\12\0\2\4\14\0\20\4\1\0\15\4"
			+ "\1\110\4\4\11\0\3\75\1\43\1\77\1\45\3\75"
			+ "\1\100\61\75\3\77\1\124\1\77\1\45\3\77\1\100"
			+ "\61\77\3\100\1\125\1\100\1\106\65\100\3\105\1\124"
			+ "\5\105\1\0\61\105\11\100\4\0\56\100\1\0\2\4"
			+ "\14\0\3\4\1\126\14\4\1\0\22\4\12\0\2\4"
			+ "\14\0\1\4\1\127\16\4\1\0\22\4\12\0\2\4"
			+ "\14\0\13\4\1\110\4\4\1\0\22\4\12\0\2\4"
			+ "\14\0\10\4\1\53\7\4\1\0\22\4\12\0\2\4"
			+ "\14\0\4\4\1\110\13\4\1\0\22\4\12\0\2\4"
			+ "\14\0\2\4\1\130\15\4\1\0\22\4\12\0\2\4"
			+ "\14\0\11\4\1\113\6\4\1\0\22\4\53\0\1\131"
			+ "\72\0\1\132\100\0\1\133\74\0\1\134\21\0\2\4"
			+ "\14\0\2\4\1\114\15\4\1\0\22\4\12\0\2\4"
			+ "\14\0\4\4\1\135\13\4\1\0\22\4\12\0\2\4"
			+ "\14\0\7\4\1\136\10\4\1\0\22\4\12\0\2\4"
			+ "\14\0\20\4\1\0\14\4\1\110\5\4\54\0\1\134"
			+ "\75\0\1\131\65\0\1\137\32\0\2\4\14\0\5\4"
			+ "\1\140\12\4\1\0\22\4\12\0\2\4\14\0\2\4"
			+ "\1\53\15\4\1\0\22\4\62\0\1\134\22\0\2\4"
			+ "\14\0\6\4\1\136\11\4\1\0\22\4\11\0";

	private static int[] zzUnpackTrans() {
		int[] result = new int[4838];
		int offset = 0;
		offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackTrans(String packed, int offset, int[] result) {
		int i = 0; /* index in packed string */
		int j = offset; /* index in unpacked array */
		int l = packed.length();
		while (i < l) {
			int count = packed.charAt(i++);
			int value = packed.charAt(i++);
			value--;
			do
				result[j++] = value;
			while (--count > 0);
		}
		return j;
	}

	/* error codes */
	private static final int ZZ_UNKNOWN_ERROR = 0;
	private static final int ZZ_NO_MATCH = 1;
	private static final int ZZ_PUSHBACK_2BIG = 2;

	/* error messages for the codes above */
	private static final String ZZ_ERROR_MSG[] = {
			"Unkown internal scanner error", "Error: could not match input",
			"Error: pushback value was too large" };

	/**
	 * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
	 */
	private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

	private static final String ZZ_ATTRIBUTE_PACKED_0 = "\1\0\1\10\1\11\5\1\2\11\16\1\1\11\34\1"
			+ "\4\0\7\1\1\0\1\1\1\11\13\1\4\0\1\1"
			+ "\2\11\3\1\3\0\1\11\2\1\1\0\1\1";

	private static int[] zzUnpackAttribute() {
		int[] result = new int[96];
		int offset = 0;
		offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
		return result;
	}

	private static int zzUnpackAttribute(String packed, int offset, int[] result) {
		int i = 0; /* index in packed string */
		int j = offset; /* index in unpacked array */
		int l = packed.length();
		while (i < l) {
			int count = packed.charAt(i++);
			int value = packed.charAt(i++);
			do
				result[j++] = value;
			while (--count > 0);
		}
		return j;
	}

	/** the input device */
	private java.io.Reader zzReader;

	/** the current state of the DFA */
	private int zzState;

	/** the current lexical state */
	private int zzLexicalState = YYINITIAL;

	/**
	 * this buffer contains the current text to be matched and is the source of
	 * the yytext() string
	 */
	private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

	/** the textposition at the last accepting state */
	private int zzMarkedPos;

	/** the current text position in the buffer */
	private int zzCurrentPos;

	/** startRead marks the beginning of the yytext() string in the buffer */
	private int zzStartRead;

	/**
	 * endRead marks the last character in the buffer, that has been read from
	 * input
	 */
	private int zzEndRead;

	/** zzAtEOF == true <=> the scanner is at the EOF */
	private boolean zzAtEOF;

	

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
	 * @param tokenType
	 *            The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos - 1, tokenType);
	}

	/**
	 * Adds the token specified to the current linked list of tokens.
	 * 
	 * @param tokenType
	 *            The token's type.
	 * @see #addHyperlinkToken(int, int, int)
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start, end, tokenType, so, false);
	}

	/**
	 * Adds the token specified to the current linked list of tokens.
	 * 
	 * @param array
	 *            The character array.
	 * @param start
	 *            The starting offset in the array.
	 * @param end
	 *            The ending offset in the array.
	 * @param tokenType
	 *            The token's type.
	 * @param startOffset
	 *            The offset in the document at which this token occurs.
	 * @param hyperlink
	 *            Whether this token is a hyperlink.
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
		return new String[] { "//", null };
	}

	/**
	 * Returns the first token in the linked list of tokens generated from
	 * <code>text</code>. This method must be implemented by subclasses so they
	 * can correctly implement syntax highlighting.
	 * 
	 * @param text
	 *            The text from which to get tokens.
	 * @param initialTokenType
	 *            The token type we should start with.
	 * @param startOffset
	 *            The offset into the document at which <code>text</code>
	 *            starts.
	 * @return The first <code>Token</code> in a linked list representing the
	 *         syntax highlighted text.
	 */
	public Token getTokenList(Segment text, int initialTokenType,
			int startOffset) {

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
	 * @return <code>true</code> if EOF was reached, otherwise
	 *         <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos >= s.offset + s.count;
	}

	/**
	 * Resets the scanner to read from a new input stream. Does not close the
	 * old reader.
	 * 
	 * All internal variables are reset, the old input stream <b>cannot</b> be
	 * reused (internal buffer is discarded and lost). Lexical state is set to
	 * <tt>YY_INITIAL</tt>.
	 * 
	 * @param reader
	 *            the new input stream
	 */
	public final void yyreset(Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill no
		 * longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array). So, we assign zzEndRead here.
		 */
		// zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtEOF = false;
	}

	/**
	 * Creates a new scanner There is also a java.io.InputStream version of this
	 * constructor.
	 * 
	 * @param in
	 *            the java.io.Reader to read input from.
	 */
	public OVScriptTokenMaker(java.io.Reader in) {
		this.zzReader = in;
	}

	/**
	 * Creates a new scanner. There is also java.io.Reader version of this
	 * constructor.
	 * 
	 * @param in
	 *            the java.io.Inputstream to read input from.
	 */
	public OVScriptTokenMaker(java.io.InputStream in) {
		this(new java.io.InputStreamReader(in,
				java.nio.charset.Charset.forName("UTF-8")));
	}

	/**
	 * Unpacks the compressed character translation table.
	 * 
	 * @param packed
	 *            the packed character translation table
	 * @return the unpacked character translation table
	 */
	private static char[] zzUnpackCMap(String packed) {
		char[] map = new char[0x10000];
		int i = 0; /* index in packed string */
		int j = 0; /* index in unpacked array */
		while (i < 184) {
			int count = packed.charAt(i++);
			char value = packed.charAt(i++);
			do
				map[j++] = value;
			while (--count > 0);
		}
		return map;
	}

	/**
	 * Closes the input stream.
	 */
	public final void yyclose() throws java.io.IOException {
		zzAtEOF = true; /* indicate end of file */
		zzEndRead = zzStartRead; /* invalidate buffer */

		if (zzReader != null)
			zzReader.close();
	}

	/**
	 * Resets the scanner to read from a new input stream. Does not close the
	 * old reader.
	 * 
	 * All internal variables are reset, the old input stream <b>cannot</b> be
	 * reused (internal buffer is discarded and lost). Lexical state is set to
	 * <tt>ZZ_INITIAL</tt>.
	 * 
	 * Internal scan buffer is resized down to its initial length, if it has
	 * grown.
	 * 
	 * @param reader
	 *            the new input stream
	 */
	public final void yyreset1(java.io.Reader reader) {
		zzReader = reader;
		zzAtEOF = false;
		zzEndRead = zzStartRead = 0;
		zzCurrentPos = zzMarkedPos = 0;
		zzLexicalState = YYINITIAL;
		if (zzBuffer.length > ZZ_BUFFERSIZE)
			zzBuffer = new char[ZZ_BUFFERSIZE];
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
	 * @param newState
	 *            the new lexical state
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
	 * @param pos
	 *            the position of the character to fetch. A value from 0 to
	 *            yylength()-1.
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
	 * @param errorCode
	 *            the code of the errormessage to display
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
	 * @param number
	 *            the number of characters to be read again. This number must
	 *            not be greater than yylength()!
	 */
	public void yypushback(int number) {
		if (number > yylength())
			zzScanError(ZZ_PUSHBACK_2BIG);

		zzMarkedPos -= number;
	}

	/**
	 * Resumes scanning until the next regular expression is matched, the end of
	 * input is encountered or an I/O-Error occurs.
	 * 
	 * @return the next token
	 * @exception java.io.IOException
	 *                if any I/O-Error occurs
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

			zzForAction: {
				while (true) {

					if (zzCurrentPosL < zzEndReadL)
						zzInput = zzBufferL[zzCurrentPosL++];
					else if (zzAtEOF) {
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
					int zzNext = zzTransL[zzRowMapL[zzState] + zzCMapL[zzInput]];
					if (zzNext == -1)
						break zzForAction;
					zzState = zzNext;

					zzAttributes = zzAttrL[zzState];
					if ((zzAttributes & 1) == 1) {
						zzAction = zzState;
						zzMarkedPosL = zzCurrentPosL;
						if ((zzAttributes & 8) == 8)
							break zzForAction;
					}

				}
			}

			// store back cached position
			zzMarkedPos = zzMarkedPosL;

			switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
			case 1: {
				addToken(Token.IDENTIFIER);
			}
			case 18:
				break;
			case 2: {
				addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
			}
			case 19:
				break;
			case 3: {
				addToken(Token.ERROR_CHAR);
				addNullToken();
				return firstToken;
			}
			case 20:
				break;
			case 4: {
				addToken(Token.COMMENT_EOL);
				addNullToken();
				return firstToken;
			}
			case 21:
				break;
			case 5: {
				addToken(Token.WHITESPACE);
			}
			case 22:
				break;
			case 6: {
				addNullToken();
				return firstToken;
			}
			case 23:
				break;
			case 7: {
				addToken(Token.SEPARATOR);
			}
			case 24:
				break;
			case 8: {
				addToken(Token.PREPROCESSOR);
			}
			case 25:
				break;
			case 9: {
				addToken(Token.OPERATOR);
			}
			case 26:
				break;
			case 10: {
				addToken(Token.ERROR_NUMBER_FORMAT);
			}
			case 27:
				break;
			case 11: {
				addToken(Token.ERROR_CHAR);
			}
			case 28:
				break;
			case 12: {
				addToken(Token.ERROR_STRING_DOUBLE);
				addNullToken();
				return firstToken;
			}
			case 29:
				break;
			case 13: {
				addToken(Token.RESERVED_WORD);
			}
			case 30:
				break;
			case 14: {
				addToken(Token.LITERAL_CHAR);
			}
			case 31:
				break;
			case 15: {
				addToken(Token.LITERAL_STRING_DOUBLE_QUOTE);
			}
			case 32:
				break;
			case 16: {
				addToken(Token.FUNCTION);
			}
			case 33:
				break;
			case 17: {
				addToken(Token.ERROR_STRING_DOUBLE);
			}
			case 34:
				break;
			default:
				if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
					zzAtEOF = true;
					switch (zzLexicalState) {
					case YYINITIAL: {
						addNullToken();
						return firstToken;
					}
					case 97:
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