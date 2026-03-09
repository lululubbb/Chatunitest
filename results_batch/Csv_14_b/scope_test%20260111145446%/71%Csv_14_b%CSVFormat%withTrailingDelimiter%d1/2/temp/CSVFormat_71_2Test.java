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
import static org.mockito.Mockito.mock;
import java.lang.reflect.Field;

public class CSVFormat_71_2Test {

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withTrailingDelimiter(true);

        // Then
        assertEquals(true, updatedFormat.getTrailingDelimiter());
    }

    // Helper method to set private fields using reflection
    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiterReflection() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        setPrivateField(csvFormat, "trailingDelimiter", false);
        CSVFormat updatedFormat = csvFormat.withTrailingDelimiter(true);

        // Then
        assertEquals(true, updatedFormat.getTrailingDelimiter());
    }
}