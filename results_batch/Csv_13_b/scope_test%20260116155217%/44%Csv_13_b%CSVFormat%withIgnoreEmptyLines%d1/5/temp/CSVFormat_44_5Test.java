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

class CSVFormat_44_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withIgnoreEmptyLines(true);

        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreEmptyLines());
        // Original instance unchanged
        assertEquals(true, baseFormat.getIgnoreEmptyLines());
        assertNotSame(baseFormat, newFormat);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withIgnoreEmptyLines(false);

        assertNotNull(newFormat);
        assertFalse(newFormat.getIgnoreEmptyLines());
        // Original instance unchanged
        assertEquals(true, baseFormat.getIgnoreEmptyLines());
        assertNotSame(baseFormat, newFormat);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesMultipleCalls() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat formatFalse = baseFormat.withIgnoreEmptyLines(false);
        CSVFormat formatTrue = formatFalse.withIgnoreEmptyLines(true);

        assertFalse(formatFalse.getIgnoreEmptyLines());
        assertTrue(formatTrue.getIgnoreEmptyLines());

        // Ensure new instances each time
        assertNotSame(baseFormat, formatFalse);
        assertNotSame(formatFalse, formatTrue);
    }
}