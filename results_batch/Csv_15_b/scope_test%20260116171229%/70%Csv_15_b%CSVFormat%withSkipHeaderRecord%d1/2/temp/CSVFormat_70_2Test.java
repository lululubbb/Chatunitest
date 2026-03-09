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

class CSVFormat_70_2Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_noArg() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getSkipHeaderRecord(), "Default skipHeaderRecord should be false");

        CSVFormat result = original.withSkipHeaderRecord();

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertTrue(result.getSkipHeaderRecord(), "skipHeaderRecord should be true after withSkipHeaderRecord()");

        // Original instance should remain unchanged (immutability)
        assertFalse(original.getSkipHeaderRecord(), "Original CSVFormat should remain unchanged");
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_booleanArg_true() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getSkipHeaderRecord(), "Default skipHeaderRecord should be false");

        CSVFormat result = original.withSkipHeaderRecord(true);

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertTrue(result.getSkipHeaderRecord(), "skipHeaderRecord should be true when true is passed");

        // Original instance should remain unchanged (immutability)
        assertFalse(original.getSkipHeaderRecord(), "Original CSVFormat should remain unchanged");
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_booleanArg_false() throws Exception {
        // Create a CSVFormat with skipHeaderRecord = true first
        CSVFormat formatWithSkip = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(formatWithSkip.getSkipHeaderRecord(), "skipHeaderRecord should be true initially");

        CSVFormat result = formatWithSkip.withSkipHeaderRecord(false);

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertFalse(result.getSkipHeaderRecord(), "skipHeaderRecord should be false when false is passed");

        // Original instance should remain unchanged (immutability)
        assertTrue(formatWithSkip.getSkipHeaderRecord(), "Original CSVFormat should remain unchanged");
    }
}