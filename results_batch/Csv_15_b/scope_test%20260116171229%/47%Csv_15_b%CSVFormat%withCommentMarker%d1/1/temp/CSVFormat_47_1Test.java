package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_47_1Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a normal comment marker character
        char commentChar = '#';
        CSVFormat resultFormat = baseFormat.withCommentMarker(commentChar);
        assertNotNull(resultFormat);
        assertEquals(Character.valueOf(commentChar), resultFormat.getCommentMarker());

        // Test that original format is not modified (immutability)
        assertFalse(baseFormat.isCommentMarkerSet());
        assertNull(baseFormat.getCommentMarker());

        // Test with a control character as comment marker
        char controlChar = '\u0001';
        CSVFormat formatWithControlChar = baseFormat.withCommentMarker(controlChar);
        assertEquals(Character.valueOf(controlChar), formatWithControlChar.getCommentMarker());

        // Test with a whitespace character as comment marker
        char spaceChar = ' ';
        CSVFormat formatWithSpace = baseFormat.withCommentMarker(spaceChar);
        assertEquals(Character.valueOf(spaceChar), formatWithSpace.getCommentMarker());

        // Test with comment marker as comma (delimiter character) - allowed
        char commaChar = ',';
        CSVFormat formatWithComma = baseFormat.withCommentMarker(commaChar);
        assertEquals(Character.valueOf(commaChar), formatWithComma.getCommentMarker());
    }
}