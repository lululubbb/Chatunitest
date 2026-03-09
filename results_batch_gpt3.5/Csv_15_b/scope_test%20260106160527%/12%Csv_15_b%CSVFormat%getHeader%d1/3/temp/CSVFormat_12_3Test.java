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
    void testGetHeaderReturnsCloneWhenHeaderIsSet() throws Exception {
        String[] header = new String[]{"col1", "col2"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(); // create default format without header

        // Use reflection to set the private final header field
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        headerField.set(format, header);

        String[] returnedHeader = format.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(header, returnedHeader, "Returned header should match the set header");
        assertNotSame(header, returnedHeader, "Returned header should be a clone, not the same instance");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsNullWhenHeaderIsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] returnedHeader = format.getHeader();

        assertNull(returnedHeader, "Returned header should be null when header field is null");
    }
}