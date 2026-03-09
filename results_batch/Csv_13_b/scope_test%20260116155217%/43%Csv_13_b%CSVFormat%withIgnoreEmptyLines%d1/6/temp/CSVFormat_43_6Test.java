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

class CSVFormat_43_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreEmptyLines();

        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreEmptyLines());

        // The original instance should remain unchanged
        assertFalse(format.getIgnoreEmptyLines());

        // Calling withIgnoreEmptyLines(true) explicitly should produce equal format
        CSVFormat explicitFormat = format.withIgnoreEmptyLines(true);
        assertEquals(explicitFormat, newFormat);
    }
}