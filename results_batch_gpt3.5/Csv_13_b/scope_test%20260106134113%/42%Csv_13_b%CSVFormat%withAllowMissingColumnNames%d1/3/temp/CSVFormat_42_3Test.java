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

class CSVFormat_42_3Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat updated = base.withAllowMissingColumnNames(true);
        assertNotNull(updated);
        assertTrue(updated.getAllowMissingColumnNames());
        // Original instance remains unchanged
        assertFalse(base.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() {
        CSVFormat base = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat updated = base.withAllowMissingColumnNames(false);
        assertNotNull(updated);
        assertFalse(updated.getAllowMissingColumnNames());
        // Original instance remains unchanged
        assertTrue(base.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesDoesNotChangeOtherFields() {
        CSVFormat base = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'').withIgnoreEmptyLines(false);
        CSVFormat updated = base.withAllowMissingColumnNames(true);

        assertEquals(base.getDelimiter(), updated.getDelimiter());
        assertEquals(base.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(base.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
    }

}