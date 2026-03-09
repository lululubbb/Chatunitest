package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CSVFormat_5_2Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    public void testEquals_SameObject() {
        assertTrue(CSVFormat.DEFAULT.equals(CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullObject() {
        assertFalse(CSVFormat.DEFAULT.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentClass() {
        assertFalse(CSVFormat.DEFAULT.equals("some string"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiter() throws Exception {
        CSVFormat other = createCSVFormat((char) (CSVFormat.DEFAULT.getDelimiter() + 1),
                CSVFormat.DEFAULT.getQuoteCharacter(), CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentQuoteMode() throws Exception {
        QuoteMode differentQuoteMode = CSVFormat.DEFAULT.getQuoteMode() == QuoteMode.ALL ? QuoteMode.MINIMAL : QuoteMode.ALL;
        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(), differentQuoteMode,
                CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(), CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_QuoteCharacterNullVsNonNull() throws Exception {
        // this.quoteCharacter null, other.quoteCharacter non-null
        CSVFormat base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), null,
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), 'Q',
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));
        assertFalse(other.equals(base));

        // both non-null but different
        CSVFormat other2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), 'X',
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat base2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), 'Y',
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base2.equals(other2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_CommentMarkerNullVsNonNull() throws Exception {
        CSVFormat base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), null,
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), '#',
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));
        assertFalse(other.equals(base));

        CSVFormat base2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), 'A',
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), 'B',
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base2.equals(other2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EscapeCharacterNullVsNonNull() throws Exception {
        CSVFormat base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                null, CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                '\\', CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));
        assertFalse(other.equals(base));

        CSVFormat base2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                'X', CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                'Y', CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base2.equals(other2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullStringNullVsNonNull() throws Exception {
        CSVFormat base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                null, CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                "null", CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));
        assertFalse(other.equals(base));

        CSVFormat base2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                "a", CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                "b", CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base2.equals(other2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_HeaderArraysDifferent() throws Exception {
        String[] header1 = new String[] {"a", "b"};
        String[] header2 = new String[] {"a", "c"};
        CSVFormat base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), header1,
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), header2,
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_BooleanFieldsDifferent() throws Exception {
        // ignoreSurroundingSpaces
        CSVFormat base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), true,
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), false,
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));

        // ignoreEmptyLines
        base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                true, CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                false, CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));

        // skipHeaderRecord
        base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                true, CSVFormat.DEFAULT.getAllowMissingColumnNames());

        other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                false, CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_RecordSeparatorNullVsNonNull() throws Exception {
        CSVFormat base = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), null,
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), "\n",
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base.equals(other));
        assertFalse(other.equals(base));

        CSVFormat base2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), "\r",
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        CSVFormat other2 = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(), CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(), "\n",
                CSVFormat.DEFAULT.getNullString(), CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        assertFalse(base2.equals(other2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EqualObjects() throws Exception {
        String[] header = new String[] {"col1", "col2"};
        CSVFormat format1 = createCSVFormat(';', 'Q', QuoteMode.ALL, '#', '\\',
                true, false, "\r\n", "NULL", header, true, true);
        CSVFormat format2 = createCSVFormat(';', 'Q', QuoteMode.ALL, '#', '\\',
                true, false, "\r\n", "NULL", header, true, true);

        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
        assertEquals(format1.hashCode(), format2.hashCode());
    }
}