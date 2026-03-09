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

class CSVFormat_45_5Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT;
        char marker = '#';

        CSVFormat result = format.withCommentMarker(marker);

        assertNotNull(result);
        assertNotSame(format, result);
        assertEquals(Character.valueOf(marker), result.getCommentMarker());

        // Test with comment marker as a control character
        char controlMarker = '\n';
        CSVFormat resultControl = format.withCommentMarker(controlMarker);
        assertEquals(Character.valueOf(controlMarker), resultControl.getCommentMarker());

        // Test with comment marker as a delimiter character (comma)
        char commaMarker = ',';
        CSVFormat resultComma = format.withCommentMarker(commaMarker);
        assertEquals(Character.valueOf(commaMarker), resultComma.getCommentMarker());

        // Test with comment marker as a quote character
        char quoteMarker = '"';
        CSVFormat resultQuote = format.withCommentMarker(quoteMarker);
        assertEquals(Character.valueOf(quoteMarker), resultQuote.getCommentMarker());

        // Test with comment marker as a backslash
        char backslashMarker = '\\';
        CSVFormat resultBackslash = format.withCommentMarker(backslashMarker);
        assertEquals(Character.valueOf(backslashMarker), resultBackslash.getCommentMarker());
    }
}