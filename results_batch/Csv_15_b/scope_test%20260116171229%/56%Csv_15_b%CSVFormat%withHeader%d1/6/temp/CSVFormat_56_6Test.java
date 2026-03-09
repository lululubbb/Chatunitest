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

public class CSVFormat_56_6Test {

    @Test
    @Timeout(8000)
    void testWithHeaderNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader((String[]) null);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderEmpty() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader(new String[0]);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNotNull(newFormat.getHeader());
        assertEquals(0, newFormat.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderNonEmpty() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] headers = new String[] {"col1", "col2", "col3"};
        CSVFormat newFormat = format.withHeader(headers);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertArrayEquals(headers, newFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderDoesNotModifyOriginal() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] headers = new String[] {"a", "b"};
        CSVFormat newFormat = format.withHeader(headers);
        assertNull(format.getHeader());
        assertArrayEquals(headers, newFormat.getHeader());
    }
}