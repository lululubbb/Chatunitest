package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_55_3Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_NoArg() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withSkipHeaderRecord();
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
        // Original format should remain unchanged
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_BooleanTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withSkipHeaderRecord(true);
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_BooleanFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat result = format.withSkipHeaderRecord(false);
        assertNotNull(result);
        assertFalse(result.getSkipHeaderRecord());
        assertTrue(format.getSkipHeaderRecord());
    }
}