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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNew;
import static org.mockito.Mockito.whenNew;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;

public class CSVFormat_31_3Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws IOException {
        // Given
        File file = new File("test.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVPrinter csvPrinterMock = mock(CSVPrinter.class);
        CSVFormat spyCsvFormat = spy(csvFormat);

        try {
            FieldSetter.setField(spyCsvFormat, spyCsvFormat.getClass().getDeclaredField("delimiter"), ',');
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            Class<?> clazz = null;
            try {
                clazz = Class.forName("org.apache.commons.csv.CSVFormat");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                FieldSetter.setField(spyCsvFormat, clazz.getDeclaredField("delimiter"), ',');
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        whenNew(CSVPrinter.class).withAnyArguments().thenReturn(csvPrinterMock);

        // When
        CSVPrinter result = spyCsvFormat.print(file, charset);

        // Then
        assertNotNull(result);
        verifyNew(CSVPrinter.class).withArguments(any(OutputStreamWriter.class), eq(spyCsvFormat));
    }
}