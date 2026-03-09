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
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CSVFormat_62_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithNullString() {
        // Given
        String nullString = "\\N";

        // When
        CSVFormat newFormat = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, newFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithNullStringReflection() throws Exception {
        // Given
        String nullString = "\\N";
        CSVFormat csvFormatSpy = spy(csvFormat);

        // When
        CSVFormat newFormat = (CSVFormat) invokePrivateMethod(csvFormatSpy, "withNullString", String.class, nullString);

        // Then
        assertEquals(nullString, newFormat.getNullString());
    }

    private Object invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return method.invoke(object, argument);
    }
}