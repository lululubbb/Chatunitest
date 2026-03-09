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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

public class CSVFormat_60_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() {
        // Given
        CSVFormat csvFormatSpy = spy(csvFormat);

        // When
        CSVFormat result = csvFormatSpy.withIgnoreSurroundingSpaces();

        // Then
        assertTrue(result.getIgnoreSurroundingSpaces());
        try {
            Method withIgnoreSurroundingSpacesMethod = CSVFormat.class.getDeclaredMethod("withIgnoreSurroundingSpaces", boolean.class);
            withIgnoreSurroundingSpacesMethod.setAccessible(true);
            withIgnoreSurroundingSpacesMethod.invoke(csvFormatSpy, true);
            verify(csvFormatSpy, times(1)).withIgnoreSurroundingSpaces(true);
        } catch (Exception e) {
            fail("Exception occurred while verifying method call");
        }
    }
}