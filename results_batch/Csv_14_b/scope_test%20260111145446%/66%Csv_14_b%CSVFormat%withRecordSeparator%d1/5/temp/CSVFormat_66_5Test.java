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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_66_5Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator() {
        // Given
        char recordSeparator = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withRecordSeparator(recordSeparator);

        // Then
        assertEquals(String.valueOf(recordSeparator), result.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_usingReflection() throws Exception {
        // Given
        char recordSeparator = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = invokeWithRecordSeparator(csvFormat, recordSeparator);

        // Then
        assertEquals(String.valueOf(recordSeparator), result.getRecordSeparator());
    }

    // Helper method to invoke private method using reflection
    private CSVFormat invokeWithRecordSeparator(CSVFormat csvFormat, char recordSeparator) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("withRecordSeparator", char.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(csvFormat, recordSeparator);
    }
}