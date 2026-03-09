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
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class CSVFormat_62_3Test {

    @Test
    @Timeout(8000)
    public void testWithNullString() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringDefaultValue() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringExcel() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringInformixUnload() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringInformixUnloadCsv() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringMysql() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringRfc4180() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringTdf() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringCustomFormat() throws Exception {
        // Given
        CSVFormat csvFormat = new CSVFormat('|', '"', null, null, null, false, true, "\n",
                null, null, null, false, false, false, false, false);
        String nullString = "\\N";

        // When
        CSVFormat result = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, result.getNullString());
        assertNotSame(csvFormat, result);
    }
}