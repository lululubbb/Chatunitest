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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_61_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreSurroundingSpaces = true;

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withIgnoreSurroundingSpaces", boolean.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(csvFormat, ignoreSurroundingSpaces);

        // Then
        assertNotNull(result);
        assertTrue(result.getIgnoreSurroundingSpaces());
    }
}