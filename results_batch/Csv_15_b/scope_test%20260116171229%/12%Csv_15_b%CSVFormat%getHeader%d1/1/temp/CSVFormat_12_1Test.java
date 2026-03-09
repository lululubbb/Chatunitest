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

class CSVFormat_12_1Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a CSVFormat instance with header set to null via reflection
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        headerField.set(csvFormat, null);

        String[] header = csvFormat.getHeader();
        assertNull(header, "Header should be null when header field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsClone() {
        String[] originalHeader = new String[]{"col1", "col2", "col3"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(originalHeader);

        String[] returnedHeader = csvFormat.getHeader();
        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(originalHeader, returnedHeader, "Returned header should match original header");
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same instance");
    }
}