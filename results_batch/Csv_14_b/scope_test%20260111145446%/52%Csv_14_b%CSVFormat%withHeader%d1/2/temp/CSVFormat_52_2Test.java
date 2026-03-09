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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class CSVFormat_52_2Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() throws SQLException {
        // Mocking ResultSet and ResultSetMetaData
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        // Mocking the behavior of ResultSet and ResultSetMetaData
        when(resultSet.getMetaData()).thenReturn(metaData);

        // Creating an instance of CSVFormat using reflection
        CSVFormat csvFormat = createCSVFormat();

        // Invoking the withHeader method
        CSVFormat result = csvFormat.withHeader(resultSet);

        // Verifying the behavior
        assertNotNull(result);
    }

    private CSVFormat createCSVFormat() {
        try {
            return (CSVFormat) CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                    String[].class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                    String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class)
                    .newInstance(',', '"', null, null, null, false, true, "\r\n", null, null, null, false, false, false, false,
                            false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}