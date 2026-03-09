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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVFormat_31_6Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws IOException {
        // Given
        File file = new File("test.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat csvFormat = Mockito.spy(new CSVFormat(',', '"', null, null, null, false, true, "\r\n", null, null, null,
                false, false, false, false, false));
        CSVPrinter csvPrinterMock = mock(CSVPrinter.class, withSettings().extraInterfaces(Appendable.class));
        OutputStreamWriter outputStreamWriterMock = mock(OutputStreamWriter.class);

        when(csvPrinterMock.print((Appendable) Mockito.any())).thenReturn(csvPrinterMock);
        when(outputStreamWriterMock.close()).thenReturn(null);
        when(csvFormat.print(Mockito.eq(file), Mockito.eq(charset))).thenReturn(csvPrinterMock);
        whenNew(CSVPrinter.class).thenReturn(csvPrinterMock);
        whenNew(OutputStreamWriter.class).thenReturn(outputStreamWriterMock);

        // When
        CSVPrinter result = csvFormat.print(file, charset);

        // Then
        assertNotNull(result);
        verify(csvFormat).print(file, charset);
        verify(outputStreamWriterMock).close();
    }

    private <T> T whenNew(Class<T> clazz) throws Exception {
        return Mockito.mock(clazz);
    }

    private <T> T whenNew(Class<T> clazz, Object... arguments) throws Exception {
        return Mockito.mock(clazz);
    }
}