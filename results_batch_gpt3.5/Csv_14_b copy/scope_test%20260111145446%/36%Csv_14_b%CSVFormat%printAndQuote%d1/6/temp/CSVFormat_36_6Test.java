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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_36_6Test {

    @Test
    @Timeout(8000)
    public void testPrintAndQuote() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyFormat = spy(csvFormat);
        Object object = "test";
        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        try {
            Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
            method.setAccessible(true);
            method.invoke(spyFormat, object, value, offset, len, out, newRecord);
        } catch (Exception e) {
            throw new IOException("Error invoking private method", e);
        }

        // Then
        assertEquals("\"value\"", out.toString());
    }
}