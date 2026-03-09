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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter, Character quoteCharacter, QuoteMode quoteMode,
                                      Character commentMarker, Character escapeCharacter,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set private final fields
        setField(format, "delimiter", delimiter);
        setField(format, "quoteCharacter", quoteCharacter);
        setField(format, "quoteMode", quoteMode);
        setField(format, "commentMarker", commentMarker);
        setField(format, "escapeCharacter", escapeCharacter);
        setField(format, "ignoreSurroundingSpaces", ignoreSurroundingSpaces);
        setField(format, "ignoreEmptyLines", ignoreEmptyLines);
        setField(format, "recordSeparator", recordSeparator);
        setField(format, "nullString", nullString);
        setField(format, "header", header);
        setField(format, "skipHeaderRecord", skipHeaderRecord);

        return format;
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

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
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(';', null, QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', null, QuoteMode.ALL, null, null, false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacterDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\'', QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, null, null, false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', null, false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarkerDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', null, false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '!', null, false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', null, false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacterDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '/', false, false, null, null, null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullStringDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "null", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_headerDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", new String[]{"a","b"}, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", new String[]{"a","c"}, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpacesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', true, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLinesDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, true, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecordDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", null, true);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, null, "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, "\n", "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparatorDifferent() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, "\r\n", "NULL", null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', false, false, "\n", "NULL", null, false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_allFieldsEqual() throws Exception {
        String[] header = new String[]{"col1","col2"};
        CSVFormat format1 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', true, true, "\r\n", "NULL", header, true);
        CSVFormat format2 = createCSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\', true, true, "\r\n", "NULL", header.clone(), true);
        assertTrue(format1.equals(format2));
    }
}