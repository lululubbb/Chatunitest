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
import org.junit.jupiter.api.BeforeEach;

public class CSVFormat_26_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        // Start with a default CSVFormat instance
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WhenCommentMarkerIsNull() {
        // Create a new CSVFormat instance with commentMarker null by using withCommentMarker((Character) null)
        CSVFormat formatWithNullComment = csvFormat.withCommentMarker((Character) null);

        assertFalse(formatWithNullComment.isCommentMarkerSet(), "Expected isCommentMarkerSet to return false when commentMarker is null");
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WhenCommentMarkerIsSet() {
        // Create a new CSVFormat instance with commentMarker set to '#'
        CSVFormat formatWithComment = csvFormat.withCommentMarker('#');

        assertTrue(formatWithComment.isCommentMarkerSet(), "Expected isCommentMarkerSet to return true when commentMarker is set");
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WithWithCommentMarkerMethod() {
        // Use the public withCommentMarker method to create a new instance with comment marker set
        CSVFormat formatWithComment = csvFormat.withCommentMarker('#');
        assertTrue(formatWithComment.isCommentMarkerSet(), "Expected isCommentMarkerSet to return true after withCommentMarker('#')");

        // Also test with null Character explicitly
        CSVFormat formatWithNullComment = csvFormat.withCommentMarker((Character) null);
        assertFalse(formatWithNullComment.isCommentMarkerSet(), "Expected isCommentMarkerSet to return false after withCommentMarker(null)");
    }
}