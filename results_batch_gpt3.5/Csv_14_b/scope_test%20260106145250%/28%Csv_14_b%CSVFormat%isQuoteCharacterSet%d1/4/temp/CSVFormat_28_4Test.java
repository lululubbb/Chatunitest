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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class CSVFormat_28_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsSet() {
        // Create a new CSVFormat instance with quoteCharacter set to '"'
        csvFormat = csvFormat.withQuote('"');

        boolean result = csvFormat.isQuoteCharacterSet();

        assertTrue(result, "Expected isQuoteCharacterSet() to return true when quoteCharacter is set");
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsNull() {
        // Create a new CSVFormat instance with quoteCharacter set to null using withQuote((Character) null)
        csvFormat = csvFormat.withQuote((Character) null);

        boolean result = csvFormat.isQuoteCharacterSet();

        assertFalse(result, "Expected isQuoteCharacterSet() to return false when quoteCharacter is null");
    }
}