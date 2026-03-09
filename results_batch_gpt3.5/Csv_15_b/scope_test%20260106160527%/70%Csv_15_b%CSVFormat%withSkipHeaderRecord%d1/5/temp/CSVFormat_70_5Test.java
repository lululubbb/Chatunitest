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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_70_5Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_noArg() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        // Invoke withSkipHeaderRecord() no-arg method
        CSVFormat result = original.withSkipHeaderRecord();

        // The returned CSVFormat should not be the same instance (immutable style)
        assertNotSame(original, result);

        // The skipHeaderRecord flag should be true in the returned instance
        assertTrue(result.getSkipHeaderRecord());

        // The original instance should remain unchanged (skipHeaderRecord false by default)
        assertFalse(original.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_booleanTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withSkipHeaderRecord(true);

        assertNotSame(original, result);
        assertTrue(result.getSkipHeaderRecord());
        assertFalse(original.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_booleanFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat result = original.withSkipHeaderRecord(false);

        assertNotSame(original, result);
        assertFalse(result.getSkipHeaderRecord());
        assertTrue(original.getSkipHeaderRecord());
    }
}