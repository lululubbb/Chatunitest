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
    public void testWithHeader_NullHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader((String[]) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getHeader());
        // Original format header remains null
        assertNull(format.getHeader());
        // New object created, not same instance
        assertNotSame(format, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader(new String[0]);
        assertNotNull(newFormat);
        assertNotNull(newFormat.getHeader());
        assertEquals(0, newFormat.getHeader().length);
        // Original format header remains null
        assertNull(format.getHeader());
        assertNotSame(format, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NonEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[]{"col1", "col2", "col3"};
        CSVFormat newFormat = format.withHeader(header);
        assertNotNull(newFormat);
        assertArrayEquals(header, newFormat.getHeader());
        // Original format header remains null
        assertNull(format.getHeader());
        assertNotSame(format, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[]{"a", "b"};
        CSVFormat newFormat = format.withHeader(header);
        // Changing original header array after withHeader call should not affect newFormat's header
        header[0] = "changed";
        assertNotEquals(header[0], newFormat.getHeader()[0]);
    }
}