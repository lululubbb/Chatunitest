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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class CSVFormat_31_2Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws Exception {
        // Given
        File file = new File("test.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVPrinter csvPrinterMock = mock(CSVPrinter.class);
        CSVFormat spyCsvFormat = Mockito.spy(csvFormat);

        OutputStreamWriter outputStreamWriterMock = mock(OutputStreamWriter.class);
        PowerMockito.whenNew(OutputStreamWriter.class).withArguments(any(FileOutputStream.class), eq(charset))
                .thenReturn(outputStreamWriterMock);

        PowerMockito.whenNew(CSVPrinter.class).withArguments(eq(outputStreamWriterMock), eq(spyCsvFormat))
                .thenReturn(csvPrinterMock);

        // When
        CSVPrinter result = spyCsvFormat.print(file, charset);

        // Then
        assertEquals(csvPrinterMock, result);
        PowerMockito.verifyNew(OutputStreamWriter.class).withArguments(any(FileOutputStream.class), eq(charset));
        PowerMockito.verifyNew(CSVPrinter.class).withArguments(eq(outputStreamWriterMock), eq(spyCsvFormat));
    }
}