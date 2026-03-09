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
import org.mockito.Mockito;

import java.lang.reflect.Constructor;

public class CSVFormat_59_5Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreHeaderCase = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreHeaderCase(ignoreHeaderCase);

        // Then
        assertNotNull(newCsvFormat);
        assertEquals(ignoreHeaderCase, newCsvFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_false() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreHeaderCase = false;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreHeaderCase(ignoreHeaderCase);

        // Then
        assertNotNull(newCsvFormat);
        assertEquals(ignoreHeaderCase, newCsvFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_privateConstructor() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreHeaderCase = true;

        // When
        CSVFormat newCsvFormat = invokePrivateConstructor(csvFormat, ignoreHeaderCase);

        // Then
        assertNotNull(newCsvFormat);
        assertEquals(ignoreHeaderCase, newCsvFormat.getIgnoreHeaderCase());
    }

    private CSVFormat invokePrivateConstructor(CSVFormat csvFormat, boolean ignoreHeaderCase) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(',', null, null, null, null, false, true, "\r\n",
                null, null, null, false, false, ignoreHeaderCase, false, false);
    }
}