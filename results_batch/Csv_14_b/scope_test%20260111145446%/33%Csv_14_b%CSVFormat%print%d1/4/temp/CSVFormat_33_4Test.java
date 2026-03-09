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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

public class CSVFormat_33_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrint() throws IOException {
        // Given
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        StringWriter out = new StringWriter();
        Object value = "testValue";
        boolean newRecord = true;

        // When
        doCallRealMethod().when(spyFormat).print(any(), any(), any());
        spyFormat.print(value, out, newRecord);

        // Then
        assertEquals("\"testValue\"\r\n", out.toString());
    }
}