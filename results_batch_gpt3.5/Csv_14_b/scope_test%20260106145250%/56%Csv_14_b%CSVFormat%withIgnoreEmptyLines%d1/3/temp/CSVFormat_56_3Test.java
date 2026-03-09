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

import java.lang.reflect.Method;

class CSVFormat_56_3Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines_noArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines();
        assertNotNull(modified);
        assertTrue(modified.getIgnoreEmptyLines());
        // Ensure original is unchanged (immutability)
        assertFalse(original.getIgnoreEmptyLines());
        assertNotSame(original, modified);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines_booleanTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Method withIgnoreEmptyLinesBool = CSVFormat.class.getMethod("withIgnoreEmptyLines", boolean.class);
        CSVFormat result = (CSVFormat) withIgnoreEmptyLinesBool.invoke(original, true);
        assertNotNull(result);
        assertTrue(result.getIgnoreEmptyLines());
        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines_booleanFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        Method withIgnoreEmptyLinesBool = CSVFormat.class.getMethod("withIgnoreEmptyLines", boolean.class);
        CSVFormat result = (CSVFormat) withIgnoreEmptyLinesBool.invoke(original, false);
        assertNotNull(result);
        assertFalse(result.getIgnoreEmptyLines());
        assertNotSame(original, result);
    }
}