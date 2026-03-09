package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_30_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_returnsCSVParser() throws IOException, ReflectiveOperationException {
        Reader mockReader = mock(Reader.class);
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get private fields from CSVParser class
        Reader parserReader = getPrivateField(parser, "reader", Reader.class);
        CSVFormat parserFormat = getPrivateField(parser, "format", CSVFormat.class);

        assertEquals(mockReader, parserReader);
        assertEquals(csvFormat, parserFormat);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    @Test
    @Timeout(8000)
    void testParse_calledMultipleTimes_returnsDistinctParsers() throws IOException, ReflectiveOperationException {
        Reader mockReader1 = mock(Reader.class);
        Reader mockReader2 = mock(Reader.class);
        CSVParser parser1 = csvFormat.parse(mockReader1);
        CSVParser parser2 = csvFormat.parse(mockReader2);
        assertNotSame(parser1, parser2);

        Reader parser1Reader = getPrivateField(parser1, "reader", Reader.class);
        Reader parser2Reader = getPrivateField(parser2, "reader", Reader.class);

        assertEquals(mockReader1, parser1Reader);
        assertEquals(mockReader2, parser2Reader);
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object target, String fieldName, Class<T> fieldType) throws ReflectiveOperationException {
        Field field = null;
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy of " + target.getClass());
        }
        field.setAccessible(true);
        return fieldType.cast(field.get(target));
    }
}