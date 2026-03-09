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
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_26_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.newFormat(',').withQuote('"').withHeader("Header1", "Header2").withHeaderComments("Comment1", "Comment2");
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_WhenEscapeCharacterIsNotNull_ExpectTrue() {
        // Given
        csvFormat = csvFormat.withEscape('|');

        // When
        boolean result = csvFormat.isEscapeCharacterSet();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_WhenEscapeCharacterIsNull_ExpectFalse() {
        // Given
        csvFormat = csvFormat.withEscape(null);

        // When
        boolean result = csvFormat.isEscapeCharacterSet();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_WhenQuoteCharacterIsNotNull_ExpectTrue() {
        // Given
        csvFormat = csvFormat.withQuote('"');

        // When
        boolean result = csvFormat.isQuoteCharacterSet();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_WhenQuoteCharacterIsNull_ExpectFalse() {
        // Given
        csvFormat = csvFormat.withQuote(null);

        // When
        boolean result = csvFormat.isQuoteCharacterSet();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WhenCommentMarkerIsNotNull_ExpectTrue() {
        // Given
        csvFormat = csvFormat.withCommentMarker('#');

        // When
        boolean result = csvFormat.isCommentMarkerSet();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WhenCommentMarkerIsNull_ExpectFalse() {
        // Given
        csvFormat = csvFormat.withCommentMarker(null);

        // When
        boolean result = csvFormat.isCommentMarkerSet();

        // Then
        assertFalse(result);
    }

}