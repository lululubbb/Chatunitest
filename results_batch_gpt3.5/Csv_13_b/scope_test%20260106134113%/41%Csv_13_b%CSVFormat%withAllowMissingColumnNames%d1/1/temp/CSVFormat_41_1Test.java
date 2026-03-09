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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_41_1Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getAllowMissingColumnNames());

        CSVFormat updated = original.withAllowMissingColumnNames(true);
        assertNotNull(updated);
        assertTrue(updated.getAllowMissingColumnNames());

        // Ensure original is not mutated (immutability)
        assertFalse(original.getAllowMissingColumnNames());

        // Calling again on updated returns a CSVFormat with allowMissingColumnNames true
        CSVFormat updatedAgain = updated.withAllowMissingColumnNames(true);
        assertSame(updated, updatedAgain, "Calling withAllowMissingColumnNames(true) on already allowed instance should return same instance");
    }
}