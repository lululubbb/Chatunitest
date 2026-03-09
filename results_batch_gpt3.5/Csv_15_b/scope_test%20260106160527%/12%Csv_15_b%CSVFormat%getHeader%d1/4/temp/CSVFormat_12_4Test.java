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

class CSVFormat_12_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withHeader((String[]) null);
    }

    @Test
    @Timeout(8000)
    void testGetHeader_WhenHeaderIsNull() {
        CSVFormat newFormat = csvFormat.withHeader((String[]) null);
        csvFormat = newFormat;

        String[] header = csvFormat.getHeader();
        assertNull(header, "Expected getHeader() to return null when header is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeader_WhenHeaderIsNotNull() {
        String[] originalHeader = new String[] {"col1", "col2", "col3"};

        CSVFormat newFormat = csvFormat.withHeader(originalHeader);
        csvFormat = newFormat;

        String[] returnedHeader = csvFormat.getHeader();

        assertNotNull(returnedHeader, "Expected getHeader() to return a non-null array when header is set");
        assertArrayEquals(originalHeader, returnedHeader, "Expected returned header to be equal to original header");
        assertNotSame(originalHeader, returnedHeader, "Expected returned header to be a clone, not the original array");

        returnedHeader[0] = "modified";
        assertNotEquals(originalHeader[0], returnedHeader[0], "Modifying returned header should not affect original header");
    }
}