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

import java.lang.reflect.Field;

public class CSVFormat_12_6Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneWhenHeaderNotNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("a", "b", "c");

        // Use reflection to get the private header field
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] originalHeader = (String[]) headerField.get(csvFormat);

        String[] returnedHeader = csvFormat.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(originalHeader, returnedHeader, "Returned header should equal original header");
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same instance");

        // Modify returnedHeader and check originalHeader not affected
        returnedHeader[0] = "modified";
        assertNotEquals(originalHeader[0], returnedHeader[0], "Original header should not be affected by changes to returned array");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        // Create CSVFormat instance with header field set to non-null first
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("a", "b");

        // Use reflection to set header field to null explicitly
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        // Since CSVFormat is immutable, set header to null on the instance via reflection
        headerField.set(csvFormat, null);

        String[] returnedHeader = csvFormat.getHeader();

        assertNull(returnedHeader, "Returned header should be null when header field is null");
    }
}