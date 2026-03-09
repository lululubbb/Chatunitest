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
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(CSVFormat.class)
public class CSVFormat_31_5Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws Exception {
        // Given
        File out = new File("test.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Mock OutputStreamWriter and FileOutputStream
        OutputStreamWriter outputStreamWriter = mock(OutputStreamWriter.class);
        FileOutputStream fileOutputStream = mock(FileOutputStream.class);
        PowerMockito.whenNew(OutputStreamWriter.class).withArguments(fileOutputStream, charset).thenReturn(outputStreamWriter);
        PowerMockito.whenNew(FileOutputStream.class).withArguments(out).thenReturn(fileOutputStream);

        // When
        CSVPrinter csvPrinter = csvFormat.print(out, charset);

        // Then
        assertNotNull(csvPrinter);
        PowerMockito.verifyNew(OutputStreamWriter.class).withArguments(fileOutputStream, charset);
        PowerMockito.verifyNew(FileOutputStream.class).withArguments(out);
    }
}