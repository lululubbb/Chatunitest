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

public class CSVFormat_56_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_NoArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withIgnoreEmptyLines();
        assertNotNull(result, "Result should not be null");
        assertTrue(result.getIgnoreEmptyLines(), "IgnoreEmptyLines should be true");

        // The original should remain unchanged (immutable)
        assertFalse(original.getIgnoreEmptyLines(), "Original ignoreEmptyLines should be false");
        assertNotSame(original, result, "Result should be a different instance");
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_True() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        CSVFormat result = original.withIgnoreEmptyLines(true);
        assertNotNull(result);
        assertTrue(result.getIgnoreEmptyLines());
        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_False() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat result = original.withIgnoreEmptyLines(false);
        assertNotNull(result);
        assertFalse(result.getIgnoreEmptyLines());
        assertNotSame(original, result);
    }
}