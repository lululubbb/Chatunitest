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
import java.lang.reflect.Method;

class CSVFormat_74_3Test {

    @Test
    @Timeout(8000)
    void testWithTrim_noArg() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrim();

        // Check that withTrim() returns a CSVFormat with trim = true
        assertNotNull(result);
        assertTrue(result.getTrim());

        // Check that original is not modified (immutability)
        assertFalse(original.getTrim());

        // Using reflection to invoke public withTrim(boolean)
        Method withTrimBoolMethod = CSVFormat.class.getMethod("withTrim", boolean.class);

        // Call withTrim(true)
        CSVFormat trimmedTrue = (CSVFormat) withTrimBoolMethod.invoke(original, true);
        assertTrue(trimmedTrue.getTrim());

        // Call withTrim(false)
        CSVFormat trimmedFalse = (CSVFormat) withTrimBoolMethod.invoke(original, false);
        assertFalse(trimmedFalse.getTrim());

        // The withTrim() no-arg method should be equivalent to withTrim(true)
        assertEquals(trimmedTrue, result);
    }
}