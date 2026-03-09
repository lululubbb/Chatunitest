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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_24_6Test {

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsNull() {
        // Create a new CSVFormat instance with commentMarker null using withCommentMarker((Character) null)
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);

        assertFalse(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsSet() {
        // Create a new CSVFormat instance with commentMarker set to '#'
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');

        assertTrue(format.isCommentMarkerSet());
    }
}