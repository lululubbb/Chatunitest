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

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CSVFormat_38_1Test {

    @Test
    @Timeout(8000)
    public void testPrintRecord() throws IOException {
        // Given
        Appendable out = mock(Appendable.class);
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        
        // When
        csvFormat.printRecord(out, "value1", "value2", "value3");
        
        // Then
        verify(out, times(3)).append(any(CharSequence.class));
        verify(out, times(2)).append(",");
        verify(out).append("value1");
        verify(out).append("value2");
        verify(out).append("value3");
        verify(out).append(System.lineSeparator());
    }
}