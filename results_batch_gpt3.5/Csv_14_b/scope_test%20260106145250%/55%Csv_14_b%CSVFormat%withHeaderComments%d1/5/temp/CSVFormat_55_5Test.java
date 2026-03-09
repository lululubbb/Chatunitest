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

class CSVFormat_55_5Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullAndEmpty() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Passing null
        CSVFormat resultNull = format.withHeaderComments((Object[]) null);
        assertNotNull(resultNull);
        assertNull(resultNull.getHeaderComments());

        // Passing empty array
        CSVFormat resultEmpty = format.withHeaderComments(new Object[0]);
        assertNotNull(resultEmpty);
        assertNotNull(resultEmpty.getHeaderComments());
        assertEquals(0, resultEmpty.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleAndMultiple() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Single header comment
        CSVFormat resultSingle = format.withHeaderComments((Object) "Comment1");
        assertNotNull(resultSingle);
        assertArrayEquals(new String[]{"Comment1"}, resultSingle.getHeaderComments());

        // Multiple header comments
        CSVFormat resultMultiple = format.withHeaderComments("Comment1", "Comment2", 123);
        assertNotNull(resultMultiple);
        assertArrayEquals(new String[]{"Comment1", "Comment2", "123"}, resultMultiple.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments("A", "B");
        // Original format should not have header comments set
        assertNull(format.getHeaderComments());
        // New format should have header comments set
        assertArrayEquals(new String[]{"A", "B"}, newFormat.getHeaderComments());
    }
}