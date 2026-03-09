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

class CSVFormat_40_1Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullAndEmpty() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat resultNull = format.withHeaderComments((Object[]) null);
        assertNotNull(resultNull);
        assertNull(resultNull.getHeaderComments());

        CSVFormat resultEmpty = format.withHeaderComments(new Object[0]);
        assertNotNull(resultEmpty);
        assertNotNull(resultEmpty.getHeaderComments());
        assertEquals(0, resultEmpty.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_WithSingleAndMultipleComments() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat singleCommentFormat = format.withHeaderComments(new Object[] { "comment1" });
        assertNotNull(singleCommentFormat);
        assertArrayEquals(new String[]{"comment1"}, singleCommentFormat.getHeaderComments());

        CSVFormat multipleCommentsFormat = format.withHeaderComments("comment1", "comment2", "3");
        assertNotNull(multipleCommentsFormat);
        assertArrayEquals(new String[]{"comment1", "comment2", "3"}, multipleCommentsFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_OriginalImmutable() {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("original");
        CSVFormat newFormat = format.withHeaderComments("newComment");
        assertNotSame(format, newFormat);
        assertArrayEquals(new String[]{"original"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"newComment"}, newFormat.getHeaderComments());
    }
}