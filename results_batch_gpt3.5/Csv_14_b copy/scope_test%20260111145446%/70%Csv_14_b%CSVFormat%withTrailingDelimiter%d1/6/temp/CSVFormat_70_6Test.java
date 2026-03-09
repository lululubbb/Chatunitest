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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_70_6Test {

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withTrailingDelimiter();

        // Then
        assertTrue(updatedFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiterFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false);

        // When
        CSVFormat updatedFormat = csvFormat.withTrailingDelimiter();

        // Then
        assertFalse(updatedFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiterReflection() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat updatedFormat;

        // When
        updatedFormat = invokeWithTrailingDelimiter(csvFormat);

        // Then
        assertTrue(updatedFormat.getTrailingDelimiter());
    }

    private CSVFormat invokeWithTrailingDelimiter(CSVFormat csvFormat) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("withTrailingDelimiter", boolean.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(csvFormat, true);
    }
}