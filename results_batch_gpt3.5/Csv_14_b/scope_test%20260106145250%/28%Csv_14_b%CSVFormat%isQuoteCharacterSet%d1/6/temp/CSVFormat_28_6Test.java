package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_28_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Initialize with a default CSVFormat instance
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsSet() {
        // Create a new CSVFormat instance with quoteCharacter set to '"'
        CSVFormat formatWithQuote = csvFormat.withQuote('"');

        // Check that isQuoteCharacterSet returns true
        assertTrue(formatWithQuote.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsNull() {
        // Create a new CSVFormat instance with quoteCharacter set to null
        CSVFormat formatWithNullQuote = csvFormat.withQuote((Character) null);

        // Check that isQuoteCharacterSet returns false
        assertFalse(formatWithNullQuote.isQuoteCharacterSet());
    }
}