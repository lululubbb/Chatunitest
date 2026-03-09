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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CSVFormat_40_4Test {

    @Test
    @Timeout(8000)
    public void testToStringArray() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withDelimiter(',').withEscape(null)
                .withHeader(null).withHeaderComments(null).withIgnoreSurroundingSpaces(false).withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n").withNullString(null).withSkipHeaderRecord(false).withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(false).withTrim(false).withTrailingDelimiter(false);
        Object[] values = { "value1", null, "value3" };
        String[] expected = { "value1", null, "value3" };

        // When
        String[] result = invokeToStringArray(csvFormat, values);

        // Then
        assertArrayEquals(expected, result);
    }

    private String[] invokeToStringArray(CSVFormat csvFormat, Object[] values) throws Exception {
        // Using reflection to access the private method
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);
        return (String[]) method.invoke(csvFormat, new Object[] { values });
    }
}