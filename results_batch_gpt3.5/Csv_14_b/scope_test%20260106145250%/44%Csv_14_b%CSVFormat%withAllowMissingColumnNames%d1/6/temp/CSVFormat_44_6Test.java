package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class CSVFormat_44_6Test {

    private boolean getAllowMissingColumnNamesReflectively(CSVFormat format) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);
        return field.getBoolean(format);
    }

    private CSVFormat withAllowMissingColumnNamesReflectively(CSVFormat format, boolean value) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("withAllowMissingColumnNames", boolean.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(format, value);
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = withAllowMissingColumnNamesReflectively(original, true);

        assertNotSame(original, modified);
        assertTrue(getAllowMissingColumnNamesReflectively(modified));
        // Original should remain unchanged
        assertFalse(getAllowMissingColumnNamesReflectively(original));
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat original = withAllowMissingColumnNamesReflectively(CSVFormat.DEFAULT, true);
        CSVFormat modified = withAllowMissingColumnNamesReflectively(original, false);

        assertNotSame(original, modified);
        assertFalse(getAllowMissingColumnNamesReflectively(modified));
        // Original should remain unchanged
        assertTrue(getAllowMissingColumnNamesReflectively(original));
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesIdempotent() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        boolean originalValue = getAllowMissingColumnNamesReflectively(original);
        CSVFormat modified = withAllowMissingColumnNamesReflectively(original, originalValue);

        // Should create a new instance even if the value is the same
        assertNotSame(original, modified);
        assertEquals(originalValue, getAllowMissingColumnNamesReflectively(modified));
    }
}