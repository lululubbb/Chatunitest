package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_24_4Test {

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_whenCommentMarkerIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a new instance with commentMarker = null using withCommentMarker method
        CSVFormat modifiedFormat = format.withCommentMarker((Character) null);

        assertFalse(modifiedFormat.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_whenCommentMarkerIsNonNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a new instance with commentMarker = '#'
        CSVFormat modifiedFormat = format.withCommentMarker('#');

        assertTrue(modifiedFormat.isCommentMarkerSet());
    }
}