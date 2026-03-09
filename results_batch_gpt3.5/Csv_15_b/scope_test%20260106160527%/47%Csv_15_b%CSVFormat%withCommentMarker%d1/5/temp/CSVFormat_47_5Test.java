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

class CSVFormat_47_5Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a normal comment marker
        char commentChar = '#';
        CSVFormat newFormat = baseFormat.withCommentMarker(commentChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());

        // Test with comment marker as comma (which is default delimiter)
        commentChar = ',';
        newFormat = baseFormat.withCommentMarker(commentChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());

        // Test with comment marker as null char (edge case, but char can't be null, so use '\0')
        commentChar = '\0';
        newFormat = baseFormat.withCommentMarker(commentChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());

        // Test that original format is not modified (immutability)
        assertNull(baseFormat.getCommentMarker());

        // Test chaining withCommentMarker calls
        CSVFormat chainedFormat = baseFormat.withCommentMarker('!').withCommentMarker('?');
        assertEquals(Character.valueOf('?'), chainedFormat.getCommentMarker());
    }
}