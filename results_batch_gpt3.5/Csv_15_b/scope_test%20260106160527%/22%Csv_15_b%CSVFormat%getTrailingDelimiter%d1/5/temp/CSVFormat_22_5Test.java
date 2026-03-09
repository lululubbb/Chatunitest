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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_22_5Test {

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_TrueViaWithTrailingDelimiter() throws Exception {
        CSVFormat formatWithTrailing = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        assertTrue(formatWithTrailing.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_FalseViaWithTrailingDelimiter() throws Exception {
        CSVFormat formatWithTrailing = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        assertFalse(formatWithTrailing.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_ReflectionAccess() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);

        // Get the constructor with all parameters
        Constructor<?> constructor = null;
        for (Constructor<?> c : CSVFormat.class.getDeclaredConstructors()) {
            if (c.getParameterCount() == 17) {
                constructor = c;
                break;
            }
        }
        assertNotNull(constructor, "Expected constructor with 17 parameters not found");
        constructor.setAccessible(true);

        Object[] params = new Object[constructor.getParameterCount()];

        params[0] = getFieldValue(format, "delimiter");
        params[1] = getFieldValue(format, "quoteCharacter");
        params[2] = getFieldValue(format, "quoteMode");
        params[3] = getFieldValue(format, "commentMarker");
        params[4] = getFieldValue(format, "escapeCharacter");
        params[5] = getFieldValue(format, "ignoreSurroundingSpaces");
        params[6] = getFieldValue(format, "ignoreEmptyLines");
        params[7] = getFieldValue(format, "recordSeparator");
        params[8] = getFieldValue(format, "nullString");
        params[9] = getFieldValue(format, "headerComments");
        params[10] = getFieldValue(format, "header");
        params[11] = getFieldValue(format, "skipHeaderRecord");
        params[12] = getFieldValue(format, "allowMissingColumnNames");
        params[13] = getFieldValue(format, "ignoreHeaderCase");
        params[14] = getFieldValue(format, "trim");
        params[15] = false; // trailingDelimiter set to false here
        params[16] = getFieldValue(format, "autoFlush");

        CSVFormat modifiedFormat = (CSVFormat) constructor.newInstance(params);

        assertFalse(modifiedFormat.getTrailingDelimiter());
    }

    private Object getFieldValue(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(format);
    }

}