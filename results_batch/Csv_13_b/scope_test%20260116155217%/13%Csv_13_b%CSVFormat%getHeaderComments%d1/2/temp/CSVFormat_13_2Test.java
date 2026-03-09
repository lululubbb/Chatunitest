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

public class CSVFormat_13_2Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) null);
        String[] result = format.getHeaderComments();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        String[] originalComments = new String[] { "comment1", "comment2" };
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) originalComments);

        String[] returnedComments = format.getHeaderComments();

        assertNotNull(returnedComments);
        assertArrayEquals(originalComments, returnedComments);
        assertNotSame(originalComments, returnedComments);

        // Modifying returned array should not affect original
        returnedComments[0] = "modified";

        String[] afterModification = format.getHeaderComments();
        assertEquals("comment1", afterModification[0]);
    }
}