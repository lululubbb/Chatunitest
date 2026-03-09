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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_70_1Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_noArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrailingDelimiter();
        assertNotNull(result);
        assertTrue(result.getTrailingDelimiter());
        // Original should remain unchanged (immutable)
        assertFalse(original.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_booleanTrue() {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        CSVFormat result = original.withTrailingDelimiter(true);
        assertNotNull(result);
        assertTrue(result.getTrailingDelimiter());
        assertFalse(original.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_booleanFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat result = original.withTrailingDelimiter(false);
        assertNotNull(result);
        assertFalse(result.getTrailingDelimiter());
        assertTrue(original.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_sameValueReturnsSameInstance() {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat result = original.withTrailingDelimiter(true);
        assertSame(original, result);

        CSVFormat originalFalse = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        CSVFormat resultFalse = originalFalse.withTrailingDelimiter(false);
        assertSame(originalFalse, resultFalse);
    }
}