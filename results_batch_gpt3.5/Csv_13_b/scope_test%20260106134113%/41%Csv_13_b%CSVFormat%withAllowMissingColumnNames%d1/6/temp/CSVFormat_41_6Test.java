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

class CSVFormat_41_6Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withAllowMissingColumnNames(true);

        assertNotNull(modified);
        assertTrue(modified.getAllowMissingColumnNames());
        // Original instance remains unchanged (immutability)
        assertFalse(original.getAllowMissingColumnNames());
        // Calling again returns a new instance with the flag set to true
        CSVFormat modifiedAgain = modified.withAllowMissingColumnNames(true);
        assertTrue(modifiedAgain.getAllowMissingColumnNames());
    }
}