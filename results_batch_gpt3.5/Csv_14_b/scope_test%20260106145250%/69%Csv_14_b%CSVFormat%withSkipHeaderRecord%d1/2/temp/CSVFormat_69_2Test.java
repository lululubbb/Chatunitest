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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_69_2Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withSkipHeaderRecord(true);
        assertNotSame(original, modified);
        assertTrue(modified.getSkipHeaderRecord());
        // Original remains unchanged
        assertFalse(original.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modified = original.withSkipHeaderRecord(false);
        assertNotSame(original, modified);
        assertFalse(modified.getSkipHeaderRecord());
        // Original remains unchanged
        assertTrue(original.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordIdempotent() {
        CSVFormat original = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modified = original.withSkipHeaderRecord(true);
        // According to CSVFormat implementation, withSkipHeaderRecord returns a new instance even if the value is the same
        assertNotSame(original, modified);
        assertTrue(modified.getSkipHeaderRecord());
        assertTrue(original.getSkipHeaderRecord());
    }
}