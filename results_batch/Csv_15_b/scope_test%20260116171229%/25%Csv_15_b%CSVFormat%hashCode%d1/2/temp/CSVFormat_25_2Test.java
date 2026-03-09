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
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_defaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expectedHashCode = computeExpectedHashCode(format);
        assertEquals(expectedHashCode, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_customInstance_allFieldsSet() throws Exception {
        CSVFormat format = createCSVFormatWithAllFields();
        int expectedHashCode = computeExpectedHashCode(format);
        assertEquals(expectedHashCode, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullFields() throws Exception {
        CSVFormat format = createCSVFormatWithNullFields();
        int expectedHashCode = computeExpectedHashCode(format);
        assertEquals(expectedHashCode, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_booleanFlagsTrue() throws Exception {
        CSVFormat format = createCSVFormatWithBooleanFlags(true, true, true, true);
        int expectedHashCode = computeExpectedHashCode(format);
        assertEquals(expectedHashCode, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_booleanFlagsFalse() throws Exception {
        CSVFormat format = createCSVFormatWithBooleanFlags(false, false, false, false);
        int expectedHashCode = computeExpectedHashCode(format);
        assertEquals(expectedHashCode, format.hashCode());
    }

    private CSVFormat createCSVFormatWithAllFields() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuoteMode(QuoteMode.ALL)
                .withQuote('\'')
                .withCommentMarker('#')
                .withEscape('\\')
                .withNullString("NULL")
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true)
                .withRecordSeparator("\n")
                .withHeader("a", "b", "c");

        // Using reflection to set private fields header explicitly to ensure coverage
        setField(format, "header", new String[]{"a", "b", "c"});
        return format;
    }

    private CSVFormat createCSVFormatWithNullFields() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null)
                .withQuoteMode(null)
                .withCommentMarker(null)
                .withEscape((Character) null)
                .withNullString(null)
                .withRecordSeparator(null)
                .withHeader((String[]) null);

        // Using reflection to set private fields header explicitly to null to ensure coverage
        setField(format, "header", null);
        return format;
    }

    private CSVFormat createCSVFormatWithBooleanFlags(boolean ignoreSurroundingSpaces,
                                                      boolean ignoreHeaderCase,
                                                      boolean ignoreEmptyLines,
                                                      boolean skipHeaderRecord) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withIgnoreSurroundingSpaces(ignoreSurroundingSpaces)
                .withIgnoreHeaderCase(ignoreHeaderCase)
                .withIgnoreEmptyLines(ignoreEmptyLines)
                .withSkipHeaderRecord(skipHeaderRecord);

        // Using reflection to set private fields header explicitly to ensure coverage
        setField(format, "header", new String[]{"header1"});
        return format;
    }

    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;

        result = prime * result + getCharField(format, "delimiter");
        result = prime * result + getObjectHashCode(format, "quoteMode");
        result = prime * result + getObjectHashCode(format, "quoteCharacter");
        result = prime * result + getObjectHashCode(format, "commentMarker");
        result = prime * result + getObjectHashCode(format, "escapeCharacter");
        result = prime * result + getObjectHashCode(format, "nullString");
        result = prime * result + (getBooleanField(format, "ignoreSurroundingSpaces") ? 1231 : 1237);
        result = prime * result + (getBooleanField(format, "ignoreHeaderCase") ? 1231 : 1237);
        result = prime * result + (getBooleanField(format, "ignoreEmptyLines") ? 1231 : 1237);
        result = prime * result + (getBooleanField(format, "skipHeaderRecord") ? 1231 : 1237);
        result = prime * result + getObjectHashCode(format, "recordSeparator");
        result = prime * result + Arrays.hashCode(getStringArrayField(format, "header"));

        return result;
    }

    private char getCharField(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getChar(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getObjectHashCode(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(format);
            return value == null ? 0 : value.hashCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getBooleanField(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getBoolean(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String[] getStringArrayField(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String[]) field.get(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(CSVFormat format, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(format, value);
    }
}