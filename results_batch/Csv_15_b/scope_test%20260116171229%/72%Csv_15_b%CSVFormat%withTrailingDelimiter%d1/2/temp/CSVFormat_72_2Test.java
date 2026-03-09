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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_72_2Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        // Call withTrailingDelimiter() which should set trailingDelimiter to true
        CSVFormat newFormat = format.withTrailingDelimiter();

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertTrue(newFormat.getTrailingDelimiter());

        // Also test withTrailingDelimiter(false) via reflection since it's package-private (default)
        try {
            Method method = CSVFormat.class.getDeclaredMethod("withTrailingDelimiter", boolean.class);
            method.setAccessible(true);
            CSVFormat falseFormat = (CSVFormat) method.invoke(format, false);
            assertNotNull(falseFormat);
            assertFalse(falseFormat.getTrailingDelimiter());
            assertNotSame(format, falseFormat);
        } catch (Exception e) {
            fail("Reflection invocation failed: " + e.getMessage());
        }
    }
}