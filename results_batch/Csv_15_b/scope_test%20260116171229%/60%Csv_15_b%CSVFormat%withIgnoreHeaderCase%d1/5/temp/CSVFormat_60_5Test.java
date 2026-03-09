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

class CSVFormat_60_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase_noArg_invokesWithIgnoreHeaderCaseTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get the withIgnoreHeaderCase() no-arg method
        Method method = CSVFormat.class.getMethod("withIgnoreHeaderCase");

        Object result = method.invoke(format);

        assertNotNull(result);
        assertTrue(result instanceof CSVFormat);
        CSVFormat newFormat = (CSVFormat) result;
        // The returned format should have ignoreHeaderCase = true
        assertTrue(newFormat.getIgnoreHeaderCase());
        // The original format should remain unchanged
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase_booleanArg_setsIgnoreHeaderCase() {
        CSVFormat format = CSVFormat.DEFAULT;

        // withIgnoreHeaderCase(true) returns a new CSVFormat with ignoreHeaderCase true
        CSVFormat newFormatTrue = format.withIgnoreHeaderCase(true);
        assertNotNull(newFormatTrue);
        assertTrue(newFormatTrue.getIgnoreHeaderCase());
        // Original format unchanged
        assertFalse(format.getIgnoreHeaderCase());

        // withIgnoreHeaderCase(false) returns a new CSVFormat with ignoreHeaderCase false
        CSVFormat newFormatFalse = format.withIgnoreHeaderCase(false);
        assertNotNull(newFormatFalse);
        assertFalse(newFormatFalse.getIgnoreHeaderCase());
        // Original format unchanged
        assertFalse(format.getIgnoreHeaderCase());

        // Calling withIgnoreHeaderCase(true) twice returns equivalent formats
        CSVFormat newFormatTrue2 = newFormatTrue.withIgnoreHeaderCase(true);
        assertEquals(newFormatTrue, newFormatTrue2);
    }
}