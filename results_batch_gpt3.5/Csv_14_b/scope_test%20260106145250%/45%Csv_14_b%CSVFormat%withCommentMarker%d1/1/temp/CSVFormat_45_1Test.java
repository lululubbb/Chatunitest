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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_45_1Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Use a normal comment marker character
        char commentChar = '#';
        CSVFormat result = baseFormat.withCommentMarker(commentChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());

        // Use a line break character as comment marker (allowed, no validation in given code)
        char lineBreakChar = '\n';
        CSVFormat result2 = baseFormat.withCommentMarker(lineBreakChar);
        assertNotNull(result2);
        assertEquals(Character.valueOf(lineBreakChar), result2.getCommentMarker());

        // Use a non-printable ASCII character
        char nonPrintable = 0x01;
        CSVFormat result3 = baseFormat.withCommentMarker(nonPrintable);
        assertNotNull(result3);
        assertEquals(Character.valueOf(nonPrintable), result3.getCommentMarker());

        // Use the null character (char value 0)
        char nullChar = 0;
        CSVFormat result4 = baseFormat.withCommentMarker(nullChar);
        assertNotNull(result4);
        assertEquals(Character.valueOf(nullChar), result4.getCommentMarker());

        // Verify that calling on a format that already has a comment marker returns a new instance
        CSVFormat withComment = CSVFormat.DEFAULT.withCommentMarker('!');
        CSVFormat newWithComment = withComment.withCommentMarker('$');
        assertNotSame(withComment, newWithComment);
        assertEquals(Character.valueOf('$'), newWithComment.getCommentMarker());
    }
}