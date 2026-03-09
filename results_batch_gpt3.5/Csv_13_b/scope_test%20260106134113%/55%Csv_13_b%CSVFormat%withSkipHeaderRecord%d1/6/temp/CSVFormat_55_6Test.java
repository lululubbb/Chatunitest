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

public class CSVFormat_55_6Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord(), "Default skipHeaderRecord should be false");

        CSVFormat newFormat = format.withSkipHeaderRecord(true);
        assertNotNull(newFormat, "Returned CSVFormat should not be null");
        assertTrue(newFormat.getSkipHeaderRecord(), "skipHeaderRecord should be true after calling withSkipHeaderRecord(true)");

        // Original format should remain unchanged (immutability)
        assertFalse(format.getSkipHeaderRecord(), "Original CSVFormat skipHeaderRecord should remain false");
    }
}