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
    public void testHashCode_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = calculateExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormatWithAllFields();
        int expected = calculateExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullFields() throws Exception {
        CSVFormat format = createCSVFormatWithNullFields();
        int expected = calculateExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_BooleanCombinations() throws Exception {
        CSVFormat format1 = createCSVFormatWithBooleans(true, true, true, true);
        CSVFormat format2 = createCSVFormatWithBooleans(false, false, false, false);
        assertEquals(calculateExpectedHashCode(format1), format1.hashCode());
        assertEquals(calculateExpectedHashCode(format2), format2.hashCode());
        // Different boolean values produce different hashCodes
        assert(format1.hashCode() != format2.hashCode());
    }

    private CSVFormat createCSVFormatWithAllFields() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        setField(format, "delimiter", (char) ';');
        setField(format, "quoteMode", QuoteMode.ALL);
        setField(format, "quoteCharacter", Character.valueOf('\''));
        setField(format, "commentMarker", Character.valueOf('#'));
        setField(format, "escapeCharacter", Character.valueOf('\\'));
        setField(format, "nullString", "NULL");
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", true);
        setField(format, "ignoreEmptyLines", true);
        setField(format, "skipHeaderRecord", true);
        setField(format, "recordSeparator", "\n");
        setField(format, "header", new String[]{"A", "B", "C"});
        return format;
    }

    private CSVFormat createCSVFormatWithNullFields() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        setField(format, "delimiter", (char) ',');
        setField(format, "quoteMode", null);
        setField(format, "quoteCharacter", null);
        setField(format, "commentMarker", null);
        setField(format, "escapeCharacter", null);
        setField(format, "nullString", null);
        setField(format, "ignoreSurroundingSpaces", false);
        setField(format, "ignoreHeaderCase", false);
        setField(format, "ignoreEmptyLines", false);
        setField(format, "skipHeaderRecord", false);
        setField(format, "recordSeparator", null);
        setField(format, "header", null);
        return format;
    }

    private CSVFormat createCSVFormatWithBooleans(boolean ignoreSurroundingSpaces,
                                                  boolean ignoreHeaderCase,
                                                  boolean ignoreEmptyLines,
                                                  boolean skipHeaderRecord) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        setField(format, "ignoreSurroundingSpaces", ignoreSurroundingSpaces);
        setField(format, "ignoreHeaderCase", ignoreHeaderCase);
        setField(format, "ignoreEmptyLines", ignoreEmptyLines);
        setField(format, "skipHeaderRecord", skipHeaderRecord);
        return format;
    }

    private int calculateExpectedHashCode(CSVFormat format) {
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
        result = prime * result + getArrayHashCode(format, "header");

        return result;
    }

    private int getCharField(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getChar(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getObjectHashCode(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(format);
            return (obj == null) ? 0 : obj.hashCode();
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

    private int getArrayHashCode(CSVFormat format, String fieldName) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object[] arr = (Object[]) field.get(format);
            return Arrays.hashCode(arr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(CSVFormat format, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.getType() == char.class && value instanceof Character) {
            field.setChar(format, (Character) value);
        } else if (field.getType() == char.class && value instanceof Integer) {
            field.setChar(format, (char) ((Integer) value).intValue());
        } else if (field.getType() == boolean.class && value instanceof Boolean) {
            field.setBoolean(format, (Boolean) value);
        } else {
            field.set(format, value);
        }
    }
}