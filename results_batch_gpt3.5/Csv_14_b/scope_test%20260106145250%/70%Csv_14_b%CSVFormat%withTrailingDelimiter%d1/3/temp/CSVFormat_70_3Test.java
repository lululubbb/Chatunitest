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

class CSVFormat_70_3Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_DefaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withTrailingDelimiter();
        assertNotNull(result);
        assertTrue(result.getTrailingDelimiter());
        // Ensure original is unchanged (immutability)
        assertFalse(format.getTrailingDelimiter());
        // Ensure a new instance is returned
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_ExplicitTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        CSVFormat result = format.withTrailingDelimiter(true);
        assertTrue(result.getTrailingDelimiter());
        assertFalse(format.getTrailingDelimiter());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_ExplicitFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat result = format.withTrailingDelimiter(false);
        assertFalse(result.getTrailingDelimiter());
        assertTrue(format.getTrailingDelimiter());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_Idempotent() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat result = format.withTrailingDelimiter(true);
        // Should return the same instance if no change
        assertSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_IdempotentFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        CSVFormat result = format.withTrailingDelimiter(false);
        assertSame(format, result);
    }
}