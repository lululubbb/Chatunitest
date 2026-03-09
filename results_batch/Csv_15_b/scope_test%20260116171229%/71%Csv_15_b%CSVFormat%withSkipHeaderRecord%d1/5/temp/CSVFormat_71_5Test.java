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

class CSVFormat_71_5Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordTrue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withSkipHeaderRecord(true);
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
        // Original instance remains unchanged
        assertFalse(base.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordFalse() {
        CSVFormat base = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat result = base.withSkipHeaderRecord(false);
        assertNotNull(result);
        assertFalse(result.getSkipHeaderRecord());
        // Original instance remains unchanged
        assertTrue(base.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecordIdempotent() {
        CSVFormat base = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat result = base.withSkipHeaderRecord(true);
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
        // The method returns a new instance even if the value is the same
        assertNotSame(base, result);
    }
}