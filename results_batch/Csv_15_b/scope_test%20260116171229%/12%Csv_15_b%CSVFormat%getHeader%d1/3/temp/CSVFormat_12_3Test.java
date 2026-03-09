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

public class CSVFormat_12_3Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header=null using withHeader(null)
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] header = csvFormat.getHeader();
        assertNull(header, "Expected header to be null when internal header field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneNotSameInstance() throws Exception {
        // Prepare header array
        String[] internalHeader = new String[]{"col1", "col2", "col3"};
        // Create a new CSVFormat instance with the header set
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(internalHeader);

        // Use reflection to get the private 'header' field inside csvFormat
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] actualInternalHeader = (String[]) headerField.get(csvFormat);

        String[] returnedHeader = csvFormat.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(actualInternalHeader, returnedHeader, "Returned header should have same contents as internal header");
        assertNotSame(actualInternalHeader, returnedHeader, "Returned header should be a clone, not the same instance");
    }
}