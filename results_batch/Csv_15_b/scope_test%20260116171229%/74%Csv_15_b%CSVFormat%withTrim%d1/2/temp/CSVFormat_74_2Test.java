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

class CSVFormat_74_2Test {

    @Test
    @Timeout(8000)
    void testWithTrim_DefaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat trimmedFormat = format.withTrim();

        assertNotNull(trimmedFormat);
        assertTrue(trimmedFormat.getTrim());
        // Ensure original format is not modified (immutability)
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrim_False() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to invoke public withTrim(boolean)
        java.lang.reflect.Method withTrimBoolMethod = CSVFormat.class.getMethod("withTrim", boolean.class);

        CSVFormat trimmedFalseFormat = (CSVFormat) withTrimBoolMethod.invoke(format, Boolean.FALSE);
        assertNotNull(trimmedFalseFormat);
        assertFalse(trimmedFalseFormat.getTrim());
        // Original remains unchanged
        assertFalse(format.getTrim());

        CSVFormat trimmedTrueFormat = (CSVFormat) withTrimBoolMethod.invoke(format, Boolean.TRUE);
        assertNotNull(trimmedTrueFormat);
        assertTrue(trimmedTrueFormat.getTrim());
    }
}