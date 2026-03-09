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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatIsNullStringSetTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSetWhenNullStringIsNull() {
        // CSVFormat is immutable, so create a new instance with nullString set to null using withNullString
        csvFormat = csvFormat.withNullString(null);

        assertFalse(csvFormat.isNullStringSet(), "Expected isNullStringSet to return false when nullString is null");
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSetWhenNullStringIsNotNull() {
        // CSVFormat is immutable, so create a new instance with nullString set to "NULL" using withNullString
        csvFormat = csvFormat.withNullString("NULL");

        assertTrue(csvFormat.isNullStringSet(), "Expected isNullStringSet to return true when nullString is not null");
    }
}