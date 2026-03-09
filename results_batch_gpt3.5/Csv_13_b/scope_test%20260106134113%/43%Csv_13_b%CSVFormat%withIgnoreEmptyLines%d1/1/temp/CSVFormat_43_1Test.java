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

class CSVFormat_43_1Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreEmptyLines(true);

        // Should return a new CSVFormat instance
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);

        // The new format should have ignoreEmptyLines = true
        assertTrue(newFormat.getIgnoreEmptyLines());

        // Original format's ignoreEmptyLines should remain unchanged
        assertEquals(format.getIgnoreEmptyLines(), CSVFormat.DEFAULT.getIgnoreEmptyLines());

        // Also test chaining does not modify original
        CSVFormat chained = newFormat.withIgnoreEmptyLines(true);
        assertTrue(chained.getIgnoreEmptyLines());
        assertNotSame(newFormat, chained);
    }
}