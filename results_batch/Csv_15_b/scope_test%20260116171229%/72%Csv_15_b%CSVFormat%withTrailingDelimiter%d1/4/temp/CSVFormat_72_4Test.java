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

class CSVFormat_72_4Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_defaultTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrailingDelimiter();
        assertNotNull(result);
        assertTrue(result.getTrailingDelimiter());
        // original instance should remain unchanged (immutable)
        assertFalse(original.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_withBooleanTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Method withTrailingDelimiterBoolean = CSVFormat.class.getMethod("withTrailingDelimiter", boolean.class);
        CSVFormat result = (CSVFormat) withTrailingDelimiterBoolean.invoke(original, true);
        assertNotNull(result);
        assertTrue(result.getTrailingDelimiter());
        assertFalse(original.getTrailingDelimiter());
        // Ensure new instance is different
        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_withBooleanFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        Method withTrailingDelimiterBoolean = CSVFormat.class.getMethod("withTrailingDelimiter", boolean.class);
        CSVFormat result = (CSVFormat) withTrailingDelimiterBoolean.invoke(original, false);
        assertNotNull(result);
        assertFalse(result.getTrailingDelimiter());
        assertTrue(original.getTrailingDelimiter());
        assertNotSame(original, result);
    }
}