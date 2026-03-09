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
import org.junit.jupiter.api.BeforeEach;

public class CSVFormat_25_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        // Use DEFAULT instance as base
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        // Create a new CSVFormat instance with escapeCharacter = null using withEscape((Character) null)
        csvFormat = csvFormat.withEscape((Character) null);

        boolean result = csvFormat.isEscapeCharacterSet();

        assertFalse(result, "Escape character is null, should return false");
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() throws Exception {
        // Create a new CSVFormat instance with escapeCharacter = '\\'
        csvFormat = csvFormat.withEscape('\\');

        boolean result = csvFormat.isEscapeCharacterSet();

        assertTrue(result, "Escape character is set, should return true");
    }
}