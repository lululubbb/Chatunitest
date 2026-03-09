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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_12_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT.withHeader((String[]) null);
    }

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNull() throws Exception {
        setFinalField(csvFormat, "header", null);

        String[] header = csvFormat.getHeader();
        assertNull(header);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNonNull() throws Exception {
        String[] originalHeader = new String[] { "col1", "col2", "col3" };

        setFinalField(csvFormat, "header", originalHeader);

        String[] returnedHeader = csvFormat.getHeader();

        assertNotNull(returnedHeader);
        assertArrayEquals(originalHeader, returnedHeader);

        // Verify defensive copy: modifying returned array should not affect original
        returnedHeader[0] = "changed";
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] headerAfterChange = (String[]) headerField.get(csvFormat);
        assertEquals("col1", headerAfterChange[0]);
    }
}