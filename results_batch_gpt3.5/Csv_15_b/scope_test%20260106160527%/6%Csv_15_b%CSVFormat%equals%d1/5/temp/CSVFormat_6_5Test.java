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
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormatEqualsTest {

    private CSVFormat createCSVFormatWithFields(char delimiter, QuoteMode quoteMode, Character quoteCharacter,
                                                Character commentMarker, Character escapeCharacter, String nullString,
                                                String[] header, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                                boolean skipHeaderRecord, String recordSeparator) throws Exception {
        // Use the DEFAULT instance and modify fields via reflection since CSVFormat is final and fields are private final
        CSVFormat format = CSVFormat.DEFAULT;

        setFinalField(format, "delimiter", delimiter);
        setFinalField(format, "quoteMode", quoteMode);
        setFinalField(format, "quoteCharacter", quoteCharacter);
        setFinalField(format, "commentMarker", commentMarker);
        setFinalField(format, "escapeCharacter", escapeCharacter);
        setFinalField(format, "nullString", nullString);
        setFinalField(format, "header", header);
        setFinalField(format, "ignoreSurroundingSpaces", ignoreSurroundingSpaces);
        setFinalField(format, "ignoreEmptyLines", ignoreEmptyLines);
        setFinalField(format, "skipHeaderRecord", skipHeaderRecord);
        setFinalField(format, "recordSeparator", recordSeparator);

        // Other fields are not involved in equals, so no need to set

        return format;
    }

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testEquals_SameObject_ReturnsTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullObject_ReturnsFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentClass_ReturnsFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiter_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(';', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentQuoteMode_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.MINIMAL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_QuoteCharacterNullMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, null, '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_QuoteCharacterNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '\'', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_CommentMarkerNullMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', null, '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_CommentMarkerNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '*', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EscapeCharacterNullMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', null, "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EscapeCharacterNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '*', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullStringNullMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', null,
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullStringNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null1",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null2",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_HeaderNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header2"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_IgnoreSurroundingSpacesNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, false, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_IgnoreEmptyLinesNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, false, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_SkipHeaderRecordNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_RecordSeparatorNullMismatch_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, null);
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_RecordSeparatorNotEqual_ReturnsFalse() throws Exception {
        CSVFormat format1 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\r\n");
        CSVFormat format2 = createCSVFormatWithFields(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\', "null",
                new String[]{"header1"}, true, true, true, "\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_AllFieldsEqual_ReturnsTrue() throws Exception {
        String[] header = new String[]{"header1", "header2"};
        CSVFormat format1 = createCSVFormatWithFields(';', QuoteMode.MINIMAL, '\'', '*', '/', "null",
                header, false, false, false, "\n");
        CSVFormat format2 = createCSVFormatWithFields(';', QuoteMode.MINIMAL, '\'', '*', '/', "null",
                header, false, false, false, "\n");
        assertTrue(format1.equals(format2));
    }
}