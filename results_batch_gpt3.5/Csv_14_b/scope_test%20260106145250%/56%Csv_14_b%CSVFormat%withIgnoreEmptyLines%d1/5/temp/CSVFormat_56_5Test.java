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

class CSVFormat_56_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines_noArg() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat resultFormat = baseFormat.withIgnoreEmptyLines();

        assertNotNull(resultFormat);
        assertTrue(resultFormat.getIgnoreEmptyLines());
        // The original instance should remain unchanged (immutability)
        assertFalse(baseFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines_true() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        CSVFormat resultFormat = baseFormat.withIgnoreEmptyLines(true);

        assertNotNull(resultFormat);
        assertTrue(resultFormat.getIgnoreEmptyLines());
        assertFalse(baseFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines_false() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat resultFormat = baseFormat.withIgnoreEmptyLines(false);

        assertNotNull(resultFormat);
        assertFalse(resultFormat.getIgnoreEmptyLines());
        assertTrue(baseFormat.getIgnoreEmptyLines());
    }
}