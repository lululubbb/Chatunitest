package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private File mockFile;
    private Charset charset;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFile = mock(File.class);
        charset = Charset.forName("UTF-8");
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_ShouldThrowNullPointerException_WhenFileIsNull() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            // Use explicit cast to File to disambiguate method call
            CSVParser.parse((File) null, charset, mockFormat);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ShouldThrowNullPointerException_WhenFormatIsNull() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockFile, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ShouldReturnCSVParserInstance_WhenValidArguments() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Instead of mocking File, create a real temporary file
        java.io.File tempFile = java.io.File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        CSVParser parser = CSVParser.parse(tempFile, charset, mockFormat);
        assertNotNull(parser);

        // Use reflection to access private field 'format'
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertEquals(mockFormat, actualFormat);
    }
}