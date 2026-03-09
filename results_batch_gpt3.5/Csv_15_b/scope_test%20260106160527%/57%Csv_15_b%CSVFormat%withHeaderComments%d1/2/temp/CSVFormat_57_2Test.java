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

public class CSVFormat_57_2Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_nullComments() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_emptyComments() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_singleComment() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeaderComments(new Object[]{"header1"});
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[]{"header1"}, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_multipleComments() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeaderComments(new Object[]{"header1", "header2", "header3"});
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[]{"header1", "header2", "header3"}, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_doesNotAffectOriginal() {
        CSVFormat original = CSVFormat.DEFAULT.withHeaderComments("oldHeader");
        CSVFormat result = original.withHeaderComments("newHeader");
        assertNotSame(original, result);
        assertArrayEquals(new String[]{"oldHeader"}, original.getHeaderComments());
        assertArrayEquals(new String[]{"newHeader"}, result.getHeaderComments());
    }
}