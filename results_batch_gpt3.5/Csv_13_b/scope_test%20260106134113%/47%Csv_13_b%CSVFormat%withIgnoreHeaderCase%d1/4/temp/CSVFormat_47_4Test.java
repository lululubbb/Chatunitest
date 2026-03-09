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

public class CSVFormat_47_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_noArg() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreHeaderCase();

        // The returned format should not be the same instance (immutable pattern)
        assertNotSame(format, newFormat);

        // The new format should have ignoreHeaderCase true
        assertTrue(newFormat.getIgnoreHeaderCase());

        // The original format should remain unchanged
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_booleanTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);

        CSVFormat newFormat = format.withIgnoreHeaderCase(true);

        assertNotSame(format, newFormat);
        assertTrue(newFormat.getIgnoreHeaderCase());
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_booleanFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);

        CSVFormat newFormat = format.withIgnoreHeaderCase(false);

        assertNotSame(format, newFormat);
        assertFalse(newFormat.getIgnoreHeaderCase());
        assertTrue(format.getIgnoreHeaderCase());
    }
}