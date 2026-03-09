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

public class CSVFormat_72_6Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_NoArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrailingDelimiter();

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertTrue(result.getTrailingDelimiter(), "Trailing delimiter should be true");

        // Original instance should remain unchanged (immutable)
        assertFalse(original.getTrailingDelimiter(), "Original CSVFormat trailingDelimiter should be false");
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_WithTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        // Use reflection to invoke public withTrailingDelimiter(boolean)
        Method method = CSVFormat.class.getMethod("withTrailingDelimiter", boolean.class);

        CSVFormat resultTrue = (CSVFormat) method.invoke(original, true);
        assertNotNull(resultTrue);
        assertTrue(resultTrue.getTrailingDelimiter());
        assertFalse(original.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_WithFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        // Use reflection to invoke public withTrailingDelimiter(boolean)
        Method method = CSVFormat.class.getMethod("withTrailingDelimiter", boolean.class);

        CSVFormat resultFalse = (CSVFormat) method.invoke(original, false);
        assertNotNull(resultFalse);
        assertFalse(resultFalse.getTrailingDelimiter());

        // Original had trailingDelimiter true
        assertTrue(original.getTrailingDelimiter());
    }
}