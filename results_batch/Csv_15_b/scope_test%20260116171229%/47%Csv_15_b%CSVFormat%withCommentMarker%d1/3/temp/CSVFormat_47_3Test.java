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

class CSVFormat_47_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with normal comment marker char
        CSVFormat formatWithComment = baseFormat.withCommentMarker(Character.valueOf('#'));
        assertNotNull(formatWithComment);
        assertEquals(Character.valueOf('#'), formatWithComment.getCommentMarker());

        // Test with comment marker as comma (existing delimiter char, but allowed as commentMarker)
        CSVFormat formatWithCommaComment = baseFormat.withCommentMarker(Character.valueOf(','));
        assertNotNull(formatWithCommaComment);
        assertEquals(Character.valueOf(','), formatWithCommaComment.getCommentMarker());

        // Test with comment marker as quote char (should be allowed, no validation here)
        CSVFormat formatWithQuoteComment = baseFormat.withCommentMarker(Character.valueOf('"'));
        assertNotNull(formatWithQuoteComment);
        assertEquals(Character.valueOf('"'), formatWithQuoteComment.getCommentMarker());

        // Test chaining: call withCommentMarker twice, last one wins
        CSVFormat chainedFormat = baseFormat.withCommentMarker(Character.valueOf('!')).withCommentMarker(Character.valueOf('%'));
        assertNotNull(chainedFormat);
        assertEquals(Character.valueOf('%'), chainedFormat.getCommentMarker());

        // Test that original format remains unchanged (immutability)
        assertNull(baseFormat.getCommentMarker());
    }
}