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

public class CSVFormat_72_6Test {

    @Test
    @Timeout(8000)
    public void testWithTrim_NoArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat trimmed = original.withTrim();

        assertNotNull(trimmed);
        assertTrue(trimmed.getTrim());
        // Original should remain unchanged (immutable)
        assertFalse(original.getTrim());
        // withTrim(true) should produce equal result
        CSVFormat trimmedExplicit = original.withTrim(true);
        assertEquals(trimmed, trimmedExplicit);
    }

    @Test
    @Timeout(8000)
    public void testWithTrim_ReflectionInvoke() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method withTrimNoArg = CSVFormat.class.getDeclaredMethod("withTrim");
        withTrimNoArg.setAccessible(true);
        Object result = withTrimNoArg.invoke(format);

        assertNotNull(result);
        assertTrue(result instanceof CSVFormat);
        CSVFormat resultFormat = (CSVFormat) result;
        assertTrue(resultFormat.getTrim());
    }
}