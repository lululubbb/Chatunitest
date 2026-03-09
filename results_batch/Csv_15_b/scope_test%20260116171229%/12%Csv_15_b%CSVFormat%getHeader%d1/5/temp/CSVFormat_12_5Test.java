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
import org.junit.jupiter.api.BeforeEach;

public class CSVFormat_12_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneOfHeader() throws Exception {
        // Create a new CSVFormat instance with header set, instead of modifying DEFAULT directly
        String[] header = new String[] { "col1", "col2", "col3" };
        CSVFormat formatWithHeader = csvFormat.withHeader(header);

        String[] returnedHeader = formatWithHeader.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(header, returnedHeader, "Returned header should equal the original header");
        assertNotSame(header, returnedHeader, "Returned header should be a clone, not the same instance");

        // Modify returnedHeader and verify original header not affected
        returnedHeader[0] = "modified";
        String[] originalHeaderAfterModification = formatWithHeader.getHeader();
        assertEquals("col1", originalHeaderAfterModification[0], "Original header should not be affected by changes to returned array");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header set to null
        CSVFormat formatWithNullHeader = csvFormat.withHeader((String[]) null);

        String[] returnedHeader = formatWithNullHeader.getHeader();

        assertNull(returnedHeader, "Returned header should be null when header field is null");
    }
}