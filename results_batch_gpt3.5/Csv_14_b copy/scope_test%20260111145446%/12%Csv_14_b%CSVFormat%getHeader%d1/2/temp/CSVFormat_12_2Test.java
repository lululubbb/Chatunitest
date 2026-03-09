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
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_12_2Test {

    @Test
    @Timeout(8000)
    public void testGetHeader() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = createCSVFormat();
        String[] expectedHeader = {"Column1", "Column2", "Column3"};
        setPrivateField(csvFormat, "header", expectedHeader);

        // When
        Method getHeaderMethod = CSVFormat.class.getDeclaredMethod("getHeader");
        getHeaderMethod.setAccessible(true);
        String[] actualHeader = (String[]) getHeaderMethod.invoke(csvFormat);

        // Then
        assertArrayEquals(expectedHeader, actualHeader);
    }

    private CSVFormat createCSVFormat() {
        return CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreEmptyLines(false)
                .withIgnoreSurroundingSpaces(true)
                .withRecordSeparator("\r\n")
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(false)
                .withIgnoreHeaderCase(true)
                .withTrim(false)
                .withTrailingDelimiter(false);
    }

    private void setPrivateField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        try {
            java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}