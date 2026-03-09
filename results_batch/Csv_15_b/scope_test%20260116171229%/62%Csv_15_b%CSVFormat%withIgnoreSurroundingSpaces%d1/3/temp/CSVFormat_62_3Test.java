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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_62_3Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_noArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withIgnoreSurroundingSpaces();
        assertNotNull(result);
        // The returned instance should have ignoreSurroundingSpaces true
        assertTrue(result.getIgnoreSurroundingSpaces());
        // Original instance should remain unchanged
        assertFalse(original.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_booleanArg_true() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("withIgnoreSurroundingSpaces", boolean.class);
        CSVFormat result = (CSVFormat) method.invoke(original, true);
        assertNotNull(result);
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertFalse(original.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_booleanArg_false() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        Method method = CSVFormat.class.getMethod("withIgnoreSurroundingSpaces", boolean.class);
        CSVFormat result = (CSVFormat) method.invoke(original, false);
        assertNotNull(result);
        assertFalse(result.getIgnoreSurroundingSpaces());
        // Original had ignoreSurroundingSpaces true
        assertTrue(original.getIgnoreSurroundingSpaces());
    }
}