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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_59_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withIgnoreHeaderCase(true);

        // Then
        assertEquals(true, result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withIgnoreHeaderCase(false);

        // Then
        assertEquals(false, result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseTwice() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result1 = csvFormat.withIgnoreHeaderCase(true);
        CSVFormat result2 = result1.withIgnoreHeaderCase(false);

        // Then
        assertEquals(false, result2.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCaseReflectively() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat result = null;

        // When
        try {
            result = (CSVFormat) csvFormat.getClass().getDeclaredMethod("withIgnoreHeaderCase", boolean.class)
                    .invoke(csvFormat, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals(true, result.getIgnoreHeaderCase());
    }
}