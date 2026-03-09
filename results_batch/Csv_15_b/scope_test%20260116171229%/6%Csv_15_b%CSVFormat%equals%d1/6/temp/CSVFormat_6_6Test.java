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

public class CSVFormatEqualsTest {

    @Test
    @Timeout(8000)
    public void testEquals_sameObject() {
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
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "delimiter", (char) (format1.getDelimiter() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        QuoteMode differentQuoteMode = format1.getQuoteMode() == QuoteMode.ALL_NON_NULL ? QuoteMode.MINIMAL : QuoteMode.ALL_NON_NULL;
        setField(format2, "quoteMode", differentQuoteMode);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "quoteCharacter", null);
        assertFalse(format1.equals(format2));

        setField(format1, "quoteCharacter", null);
        setField(format2, "quoteCharacter", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        Character qc1 = format1.getQuoteCharacter();
        Character qc2 = (qc1 == null) ? 'Q' : (char) (qc1 + 1);
        setField(format2, "quoteCharacter", qc2);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "commentMarker", null);
        assertFalse(format1.equals(format2));

        setField(format1, "commentMarker", null);
        setField(format2, "commentMarker", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        Character cm1 = format1.getCommentMarker();
        Character cm2 = (cm1 == null) ? 'C' : (char) (cm1 + 1);
        setField(format2, "commentMarker", cm2);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "escapeCharacter", null);
        assertFalse(format1.equals(format2));

        setField(format1, "escapeCharacter", null);
        setField(format2, "escapeCharacter", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        Character ec1 = format1.getEscapeCharacter();
        Character ec2 = (ec1 == null) ? 'E' : (char) (ec1 + 1);
        setField(format2, "escapeCharacter", ec2);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "nullString", null);
        assertFalse(format1.equals(format2));

        setField(format1, "nullString", null);
        setField(format2, "nullString", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        String ns1 = format1.getNullString();
        String ns2 = (ns1 == null) ? "null" : ns1 + "x";
        setField(format2, "nullString", ns2);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        String[] header1 = format1.getHeader();
        String[] header2;
        if (header1 == null) {
            header2 = new String[]{"header1"};
        } else {
            header2 = Arrays.copyOf(header1, header1.length + 1);
            header2[header1.length] = "extra";
        }
        setField(format2, "header", header2);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpaces() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "ignoreSurroundingSpaces", !format1.getIgnoreSurroundingSpaces());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLines() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "ignoreEmptyLines", !format1.getIgnoreEmptyLines());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecord() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "skipHeaderRecord", !format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "recordSeparator", null);
        assertFalse(format1.equals(format2));

        setField(format1, "recordSeparator", null);
        setField(format2, "recordSeparator", null);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        String rs1 = format1.getRecordSeparator();
        String rs2 = (rs1 == null) ? "\n" : rs1 + "x";
        setField(format2, "recordSeparator", rs2);
        assertFalse(format1.equals(format2));
    }

    private CSVFormat copyCSVFormat(CSVFormat original) throws Exception {
        // Use the public with* methods to create a copy with same properties since constructor is private
        CSVFormat copy = CSVFormat.DEFAULT
                .withDelimiter(original.getDelimiter())
                .withQuote(original.getQuoteCharacter())
                .withQuoteMode(original.getQuoteMode())
                .withCommentMarker(original.getCommentMarker())
                .withEscape(original.getEscapeCharacter())
                .withIgnoreSurroundingSpaces(original.getIgnoreSurroundingSpaces())
                .withIgnoreEmptyLines(original.getIgnoreEmptyLines())
                .withRecordSeparator(original.getRecordSeparator())
                .withNullString(original.getNullString())
                .withHeader(original.getHeader() == null ? null : Arrays.copyOf(original.getHeader(), original.getHeader().length))
                .withSkipHeaderRecord(original.getSkipHeaderRecord())
                .withAllowMissingColumnNames(original.getAllowMissingColumnNames())
                .withIgnoreHeaderCase(original.getIgnoreHeaderCase())
                .withTrim(original.getTrim())
                .withTrailingDelimiter(original.getTrailingDelimiter())
                .withAutoFlush(original.getAutoFlush());
        // headerComments is not settable via public method, skip it
        return copy;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}