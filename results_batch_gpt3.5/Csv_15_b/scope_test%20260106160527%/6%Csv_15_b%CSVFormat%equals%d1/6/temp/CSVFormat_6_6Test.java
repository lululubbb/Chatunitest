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
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormat_6_6Test {

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
    public void testEquals_allFieldsEqual() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "delimiter", (char) (format1.getDelimiter() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "quoteMode", QuoteMode.ALL);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacterNullVsNotNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "quoteCharacter", null);
        assertFalse(format1.equals(format2));

        setField(format1, "quoteCharacter", null);
        setField(format2, "quoteCharacter", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacterDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "quoteCharacter", Character.valueOf('\''));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarkerNullVsNotNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "commentMarker", null);
        assertTrue(format1.equals(format2) == false ? false : true); // Defensive

        setField(format1, "commentMarker", null);
        setField(format2, "commentMarker", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarkerDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "commentMarker", Character.valueOf('#'));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacterNullVsNotNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "escapeCharacter", null);
        assertTrue(format1.equals(format2) == false ? false : true);

        setField(format1, "escapeCharacter", null);
        setField(format2, "escapeCharacter", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacterDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "escapeCharacter", Character.valueOf('\\'));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullStringNullVsNotNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "nullString", null);
        assertTrue(format1.equals(format2) == false ? false : true);

        setField(format1, "nullString", null);
        setField(format2, "nullString", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullStringDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "nullString", "NULL");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_headerDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "header", new String[] {"A", "B"});
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "ignoreSurroundingSpaces", !format1.getIgnoreSurroundingSpaces());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "ignoreEmptyLines", !format1.getIgnoreEmptyLines());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "skipHeaderRecord", !format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparatorNullVsNotNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "recordSeparator", null);
        assertTrue(format1.equals(format2) == false ? false : true);

        setField(format1, "recordSeparator", null);
        setField(format2, "recordSeparator", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparatorDifferent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "recordSeparator", "\n");
        assertFalse(format1.equals(format2));
    }

    private CSVFormat cloneCSVFormat(CSVFormat original) throws Exception {
        CSVFormat clone = CSVFormat.newFormat(original.getDelimiter())
                .withQuote(original.getQuoteCharacter())
                .withQuoteMode(original.getQuoteMode())
                .withCommentMarker(original.getCommentMarker())
                .withEscape(original.getEscapeCharacter())
                .withIgnoreSurroundingSpaces(original.getIgnoreSurroundingSpaces())
                .withIgnoreEmptyLines(original.getIgnoreEmptyLines())
                .withRecordSeparator(original.getRecordSeparator())
                .withNullString(original.getNullString())
                .withHeader(original.getHeader())
                .withSkipHeaderRecord(original.getSkipHeaderRecord())
                .withAllowMissingColumnNames(original.getAllowMissingColumnNames())
                .withIgnoreHeaderCase(original.getIgnoreHeaderCase())
                .withTrim(original.getTrim())
                .withTrailingDelimiter(original.getTrailingDelimiter())
                .withAutoFlush(original.getAutoFlush());
        // headerComments is not accessible, ignore it for equals test
        return clone;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}