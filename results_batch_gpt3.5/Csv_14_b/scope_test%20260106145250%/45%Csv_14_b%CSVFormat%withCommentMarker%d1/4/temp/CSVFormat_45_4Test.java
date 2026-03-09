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

public class CSVFormat_45_4Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a normal comment marker character
        char commentChar = '#';
        CSVFormat newFormat = baseFormat.withCommentMarker(Character.valueOf(commentChar));
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());
        // Original instance remains unchanged (immutability)
        assertNull(baseFormat.getCommentMarker());

        // Test with a comment marker character that is same as delimiter (should still work)
        char delimiterChar = baseFormat.getDelimiter();
        CSVFormat formatWithDelimiterAsComment = baseFormat.withCommentMarker(Character.valueOf(delimiterChar));
        assertNotNull(formatWithDelimiterAsComment);
        assertEquals(Character.valueOf(delimiterChar), formatWithDelimiterAsComment.getCommentMarker());

        // Test with a control character as comment marker
        char controlChar = '\n';
        CSVFormat formatWithControlChar = baseFormat.withCommentMarker(Character.valueOf(controlChar));
        assertNotNull(formatWithControlChar);
        assertEquals(Character.valueOf(controlChar), formatWithControlChar.getCommentMarker());

        // Test with comment marker as space character
        char spaceChar = ' ';
        CSVFormat formatWithSpace = baseFormat.withCommentMarker(Character.valueOf(spaceChar));
        assertNotNull(formatWithSpace);
        assertEquals(Character.valueOf(spaceChar), formatWithSpace.getCommentMarker());
    }
}