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

class CSVFormat_43_3Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertTrue(original.getIgnoreEmptyLines()); // DEFAULT has ignoreEmptyLines = true

        CSVFormat modified = original.withIgnoreEmptyLines(false);
        assertNotNull(modified);
        assertFalse(modified.getIgnoreEmptyLines());

        // Ensure original instance is not modified (immutability)
        assertTrue(original.getIgnoreEmptyLines());

        // Ensure calling withIgnoreEmptyLines twice returns expected result
        CSVFormat modifiedTwice = modified.withIgnoreEmptyLines(false);
        assertFalse(modifiedTwice.getIgnoreEmptyLines());
    }
}