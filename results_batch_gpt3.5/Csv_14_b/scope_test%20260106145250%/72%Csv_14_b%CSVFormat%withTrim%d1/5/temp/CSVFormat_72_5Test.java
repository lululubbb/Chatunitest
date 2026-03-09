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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_72_5Test {

    @Test
    @Timeout(8000)
    void testWithTrim() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat trimmedFormat = format.withTrim();

        assertNotNull(trimmedFormat);
        assertTrue(trimmedFormat.getTrim());
        // Original format should remain unchanged if immutable
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrimUsingReflection() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method withTrimBooleanMethod = CSVFormat.class.getDeclaredMethod("withTrim", boolean.class);
        withTrimBooleanMethod.setAccessible(true);

        CSVFormat trimmedTrue = (CSVFormat) withTrimBooleanMethod.invoke(format, true);
        CSVFormat trimmedFalse = (CSVFormat) withTrimBooleanMethod.invoke(format, false);

        assertNotNull(trimmedTrue);
        assertNotNull(trimmedFalse);
        assertTrue(trimmedTrue.getTrim());
        assertFalse(trimmedFalse.getTrim());

        // Confirm immutability of original
        assertFalse(format.getTrim());
    }
}