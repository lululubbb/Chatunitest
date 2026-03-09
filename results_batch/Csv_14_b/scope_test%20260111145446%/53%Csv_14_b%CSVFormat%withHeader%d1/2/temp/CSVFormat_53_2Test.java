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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class CSVFormat_53_2Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() throws SQLException {
        // Given
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnLabel(1)).thenReturn("Name");
        when(metaData.getColumnLabel(2)).thenReturn("Age");

        CSVFormat csvFormat = createCSVFormat();

        // When
        CSVFormat result = csvFormat.withHeader(metaData);

        // Then
        assertNotNull(result);
        String[] expectedHeader = new String[]{"Name", "Age"};
        assertArrayEquals(expectedHeader, result.getHeader());
    }

    private CSVFormat createCSVFormat() {
        try {
            return (CSVFormat) CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                    Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                    String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class)
                    .newInstance(',', '"', null, null, null, false, true, "\r\n", null, null, null, false,
                            false, false, false, false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create CSVFormat instance", e);
        }
    }

    // Add more test cases to achieve full branch coverage

}