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

class CSVFormatWithHeaderCommentsTest {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullInput() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNull(result.getHeaderComments());
        // Original format remains unchanged
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_EmptyInput() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments();
        assertNotNull(result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
        // Original format remains unchanged
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleString() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments("comment1");
        assertNotNull(result);
        assertArrayEquals(new String[]{"comment1"}, result.getHeaderComments());
        // Original format remains unchanged
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleStrings() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments("comment1", "comment2", "comment3");
        assertNotNull(result);
        assertArrayEquals(new String[]{"comment1", "comment2", "comment3"}, result.getHeaderComments());
        // Original format remains unchanged
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MixedObjects() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments("comment1", 123, true, null);
        assertNotNull(result);
        assertArrayEquals(new String[]{"comment1", "123", "true", "null"}, result.getHeaderComments());
        // Original format remains unchanged
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("initial");
        CSVFormat result = format.withHeaderComments("newComment");
        assertNotSame(format, result);
        assertArrayEquals(new String[]{"initial"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"newComment"}, result.getHeaderComments());
    }
}