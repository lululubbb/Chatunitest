package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CSVFormat_6_3Test {

    @Test
    @Timeout(8000)
    public void testEquals_sameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_equalObjects() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);
        setField(format2, "delimiter", (char)(format1.getDelimiter() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);
        QuoteMode differentQuoteMode = format1.getQuoteMode() == QuoteMode.ALL_NON_NULL ? QuoteMode.MINIMAL : QuoteMode.ALL_NON_NULL;
        setField(format2, "quoteMode", differentQuoteMode);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacterNullity() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);

        // quoteCharacter null in format1, non-null in format2
        setField(format1, "quoteCharacter", null);
        setField(format2, "quoteCharacter", Character.valueOf('Q'));
        assertFalse(format1.equals(format2));

        // quoteCharacter non-null in format1, null in format2
        setField(format1, "quoteCharacter", Character.valueOf('Q'));
        setField(format2, "quoteCharacter", null);
        assertFalse(format1.equals(format2));

        // quoteCharacter different non-null values
        setField(format1, "quoteCharacter", Character.valueOf('Q'));
        setField(format2, "quoteCharacter", Character.valueOf('R'));
        assertFalse(format1.equals(format2));

        // quoteCharacter same non-null values
        setField(format1, "quoteCharacter", Character.valueOf('Q'));
        setField(format2, "quoteCharacter", Character.valueOf('Q'));
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarkerNullity() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);

        setField(format1, "commentMarker", null);
        setField(format2, "commentMarker", Character.valueOf('C'));
        assertFalse(format1.equals(format2));

        setField(format1, "commentMarker", Character.valueOf('C'));
        setField(format2, "commentMarker", null);
        assertFalse(format1.equals(format2));

        setField(format1, "commentMarker", Character.valueOf('C'));
        setField(format2, "commentMarker", Character.valueOf('D'));
        assertFalse(format1.equals(format2));

        setField(format1, "commentMarker", Character.valueOf('C'));
        setField(format2, "commentMarker", Character.valueOf('C'));
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacterNullity() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);

        setField(format1, "escapeCharacter", null);
        setField(format2, "escapeCharacter", Character.valueOf('E'));
        assertFalse(format1.equals(format2));

        setField(format1, "escapeCharacter", Character.valueOf('E'));
        setField(format2, "escapeCharacter", null);
        assertFalse(format1.equals(format2));

        setField(format1, "escapeCharacter", Character.valueOf('E'));
        setField(format2, "escapeCharacter", Character.valueOf('F'));
        assertFalse(format1.equals(format2));

        setField(format1, "escapeCharacter", Character.valueOf('E'));
        setField(format2, "escapeCharacter", Character.valueOf('E'));
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullStringNullity() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);

        setField(format1, "nullString", null);
        setField(format2, "nullString", "null");
        assertFalse(format1.equals(format2));

        setField(format1, "nullString", "null");
        setField(format2, "nullString", null);
        assertFalse(format1.equals(format2));

        setField(format1, "nullString", "null");
        setField(format2, "nullString", "other");
        assertFalse(format1.equals(format2));

        setField(format1, "nullString", "null");
        setField(format2, "nullString", "null");
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_headerArrays() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);

        setField(format1, "header", new String[] {"a", "b"});
        setField(format2, "header", new String[] {"a", "b"});
        assertTrue(format1.equals(format2));

        setField(format2, "header", new String[] {"a", "c"});
        assertFalse(format1.equals(format2));

        setField(format2, "header", null);
        setField(format1, "header", new String[] {"a", "b"});
        assertFalse(format1.equals(format2));

        setField(format1, "header", null);
        setField(format2, "header", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_booleanFields() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);

        setField(format2, "ignoreSurroundingSpaces", !format1.getIgnoreSurroundingSpaces());
        assertFalse(format1.equals(format2));
        setField(format2, "ignoreSurroundingSpaces", format1.getIgnoreSurroundingSpaces());

        setField(format2, "ignoreEmptyLines", !format1.getIgnoreEmptyLines());
        assertFalse(format1.equals(format2));
        setField(format2, "ignoreEmptyLines", format1.getIgnoreEmptyLines());

        setField(format2, "skipHeaderRecord", !format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
        setField(format2, "skipHeaderRecord", format1.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparatorNullity() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneFormat(format1);

        setField(format1, "recordSeparator", null);
        setField(format2, "recordSeparator", "\n");
        assertFalse(format1.equals(format2));

        setField(format1, "recordSeparator", "\n");
        setField(format2, "recordSeparator", null);
        assertFalse(format1.equals(format2));

        setField(format1, "recordSeparator", "\n");
        setField(format2, "recordSeparator", "\r");
        assertFalse(format1.equals(format2));

        setField(format1, "recordSeparator", "\n");
        setField(format2, "recordSeparator", "\n");
        assertTrue(format1.equals(format2));
    }

    // Utility method to clone CSVFormat by reflection copying all fields
    private CSVFormat cloneFormat(CSVFormat original) throws Exception {
        CSVFormat clone = CSVFormat.DEFAULT.withAllowMissingColumnNames(original.getAllowMissingColumnNames())
                .withCommentMarker(original.getCommentMarker())
                .withDelimiter(original.getDelimiter())
                .withEscape(original.getEscapeCharacter())
                .withHeader(original.getHeader() != null ? original.getHeader() : new String[0])
                .withIgnoreEmptyLines(original.getIgnoreEmptyLines())
                .withIgnoreHeaderCase(original.getIgnoreHeaderCase())
                .withIgnoreSurroundingSpaces(original.getIgnoreSurroundingSpaces())
                .withNullString(original.getNullString())
                .withQuote(original.getQuoteCharacter())
                .withQuoteMode(original.getQuoteMode())
                .withRecordSeparator(original.getRecordSeparator())
                .withSkipHeaderRecord(original.getSkipHeaderRecord())
                .withTrailingDelimiter(original.getTrailingDelimiter())
                .withTrim(original.getTrim())
                .withAutoFlush(original.getAutoFlush());

        // headerComments is Object[] but is not used in equals so omitted
        return clone;
    }

    // Utility method to set private fields by reflection
    private void setField(CSVFormat format, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(format, value);
    }
}