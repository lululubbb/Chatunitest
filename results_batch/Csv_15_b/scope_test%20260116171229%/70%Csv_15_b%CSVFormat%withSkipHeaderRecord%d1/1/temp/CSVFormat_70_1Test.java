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

class CSVFormat_70_1Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_noArg() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Call the focal method
        CSVFormat result = original.withSkipHeaderRecord();

        // The result should not be the same instance (immutability)
        assertNotSame(original, result);

        // The skipHeaderRecord flag should be true in the result
        assertTrue(result.getSkipHeaderRecord());

        // The original instance should not be affected
        assertFalse(original.getSkipHeaderRecord());

        // Calling withSkipHeaderRecord(true) via reflection should produce same result
        Method method = CSVFormat.class.getMethod("withSkipHeaderRecord", boolean.class);
        CSVFormat reflectResult = (CSVFormat) method.invoke(original, true);
        assertEquals(result, reflectResult);

        // Calling withSkipHeaderRecord(false) via reflection disables skipHeaderRecord
        CSVFormat disabled = (CSVFormat) method.invoke(original, false);
        assertFalse(disabled.getSkipHeaderRecord());
    }
}