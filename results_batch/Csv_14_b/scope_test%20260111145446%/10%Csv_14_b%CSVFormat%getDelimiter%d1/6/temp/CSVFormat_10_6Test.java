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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CSVFormat_10_6Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter() {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = new CSVFormat(expectedDelimiter, '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterForExcelFormat() {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterForInformixUnloadFormat() {
        // Given
        char expectedDelimiter = '|';
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterForInformixUnloadCsvFormat() {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterForMySQLFormat() {
        // Given
        char expectedDelimiter = '\t';
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterForRFC4180Format() {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterUsingReflection() throws Exception {
        // Given
        char expectedDelimiter = '\t';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        char actualDelimiter;
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                    boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            csvFormat = constructor.newInstance('\t', '"', null, null, null, false, true, "\r\n",
                    null, null, null, false, false, false, false, false);
            Method method = CSVFormat.class.getDeclaredMethod("getDelimiter");
            method.setAccessible(true);
            actualDelimiter = (char) method.invoke(csvFormat);
        } catch (NoSuchMethodException e) {
            actualDelimiter = '\0'; // Handle exception
        }

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }
}