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

class CSVFormat_72_1Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_noArg() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat resultFormat = baseFormat.withTrailingDelimiter();

        assertNotNull(resultFormat, "Resulting CSVFormat should not be null");
        assertTrue(resultFormat.getTrailingDelimiter(), "Trailing delimiter should be true");
        // Ensure original object is unchanged (immutable pattern)
        assertFalse(baseFormat.getTrailingDelimiter(), "Original CSVFormat trailingDelimiter should remain false");
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_booleanTrue() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Use reflection to invoke public withTrailingDelimiter(boolean)
        Method method = CSVFormat.class.getDeclaredMethod("withTrailingDelimiter", boolean.class);
        method.setAccessible(true);
        CSVFormat resultFormat = (CSVFormat) method.invoke(baseFormat, true);

        assertNotNull(resultFormat, "Resulting CSVFormat should not be null");
        assertTrue(resultFormat.getTrailingDelimiter(), "Trailing delimiter should be true");
        // Original remains unchanged
        assertFalse(baseFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_booleanFalse() throws Exception {
        // Start from a format with trailingDelimiter true
        CSVFormat baseFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true);

        // Use reflection to invoke public withTrailingDelimiter(boolean)
        Method method = CSVFormat.class.getDeclaredMethod("withTrailingDelimiter", boolean.class);
        method.setAccessible(true);
        CSVFormat resultFormat = (CSVFormat) method.invoke(baseFormat, false);

        assertNotNull(resultFormat, "Resulting CSVFormat should not be null");
        assertFalse(resultFormat.getTrailingDelimiter(), "Trailing delimiter should be false");
        // Original remains true
        assertTrue(baseFormat.getTrailingDelimiter());
    }
}