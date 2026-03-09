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

class CSVFormat_70_3Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_noArg() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // invoke the no-arg withSkipHeaderRecord()
        CSVFormat result = format.withSkipHeaderRecord();

        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());

        // original format should remain unchanged (immutable style)
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_booleanTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        CSVFormat result = format.withSkipHeaderRecord(true);

        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
        // original format was false
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_booleanFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat result = format.withSkipHeaderRecord(false);

        assertNotNull(result);
        assertFalse(result.getSkipHeaderRecord());
        // original format was true
        assertTrue(format.getSkipHeaderRecord());
    }
}