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

public class CSVFormat_12_4Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header set to null using withHeader method
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] header = format.getHeader();
        assertNull(header, "Header should be null when internal header field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneNotSameInstance() throws Exception {
        String[] originalHeader = new String[] {"col1", "col2"};
        // Create a new CSVFormat instance with header set to originalHeader using withHeader method
        CSVFormat format = CSVFormat.DEFAULT.withHeader(originalHeader);

        String[] returnedHeader = format.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(originalHeader, returnedHeader, "Returned header should have the same content");
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same instance");
    }
}