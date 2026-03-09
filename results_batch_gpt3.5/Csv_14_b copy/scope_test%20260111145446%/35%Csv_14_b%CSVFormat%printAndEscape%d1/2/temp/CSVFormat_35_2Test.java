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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_35_2Test {

    @Test
    @Timeout(8000)
    public void testPrintAndEscape() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withRecordSeparator("\r\n");
        CSVFormat spyCsvFormat = spy(csvFormat);
        CharSequence value = "abc,def";
        int offset = 0;
        int len = value.length();
        StringWriter out = new StringWriter();

        // When
        try {
            Method method = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
            method.setAccessible(true);
            method.invoke(spyCsvFormat, value, offset, len, out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals("abc,def", out.toString());
    }
}