package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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
import java.nio.charset.Charset;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseFileTest {

    private File mockFile;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        mockFile = mock(File.class);
        charset = Charset.defaultCharset();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testParseFileSuccess() throws Exception {
        // Create a temporary file to pass to parse method.
        File tempFile = File.createTempFile("csvparser_test", ".csv");
        tempFile.deleteOnExit();

        CSVParser parser = CSVParser.parse(tempFile, charset, format);
        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));
    }

    @Test
    @Timeout(8000)
    void testParseFileNullFile() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((File) null, charset, format));
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseFileNullFormat() throws Exception {
        // Use a real temporary file instead of mockFile to avoid NullPointerException from Assertions.notNull
        File tempFile = File.createTempFile("csvparser_test", ".csv");
        tempFile.deleteOnExit();

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(tempFile, charset, null));
        assertEquals("format", thrown.getMessage());
    }
}