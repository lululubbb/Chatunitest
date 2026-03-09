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

public class CSVFormat_55_1Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord_noArg_returnsWithTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withSkipHeaderRecord();
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord_booleanArg_true() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        CSVFormat result = format.withSkipHeaderRecord(true);
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
        // Original instance remains unchanged
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord_booleanArg_false() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat result = format.withSkipHeaderRecord(false);
        assertNotNull(result);
        assertFalse(result.getSkipHeaderRecord());
        // Original instance remains unchanged
        assertTrue(format.getSkipHeaderRecord());
    }

}