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

public class CSVFormat_41_2Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getAllowMissingColumnNames());

        CSVFormat updated = original.withAllowMissingColumnNames(true);
        assertNotNull(updated);
        assertTrue(updated.getAllowMissingColumnNames());

        // Verify original is unchanged (immutability)
        assertFalse(original.getAllowMissingColumnNames());

        // Verify chaining returns new instance with correct flag
        CSVFormat chained = updated.withAllowMissingColumnNames(true);
        assertTrue(chained.getAllowMissingColumnNames());
        assertNotSame(original, updated);
        assertNotSame(original, chained);
        assertNotSame(updated, chained);
    }
}