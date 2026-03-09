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

class CSVFormat_58_1Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreHeaderCase();

        assertNotNull(modified, "Result should not be null");
        assertTrue(modified.getIgnoreHeaderCase(), "ignoreHeaderCase should be true");
        assertNotSame(original, modified, "withIgnoreHeaderCase should return a new instance");

        // Verify original is unchanged using reflection
        Method method = CSVFormat.class.getDeclaredMethod("getIgnoreHeaderCase");
        method.setAccessible(true);
        boolean originalIgnoreHeaderCase = (boolean) method.invoke(original);
        assertFalse(originalIgnoreHeaderCase, "Original ignoreHeaderCase should be false");
    }
}