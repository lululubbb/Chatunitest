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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_12_1Test {

    @Test
    @Timeout(8000)
    public void testGetHeader() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        // Given
        CSVFormat csvFormat = createCSVFormatInstance();
        String[] expectedHeader = { "Header1", "Header2", "Header3" };
        csvFormat.withHeader(expectedHeader);

        // When
        Method method = CSVFormat.class.getDeclaredMethod("getHeader");
        method.setAccessible(true);
        String[] actualHeader = (String[]) method.invoke(csvFormat);

        // Then
        assertArrayEquals(expectedHeader, actualHeader);
    }

    private CSVFormat createCSVFormatInstance() {
        return CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreEmptyLines(false)
                .withAllowMissingColumnNames()
                .withRecordSeparator("\r\n");
    }
}