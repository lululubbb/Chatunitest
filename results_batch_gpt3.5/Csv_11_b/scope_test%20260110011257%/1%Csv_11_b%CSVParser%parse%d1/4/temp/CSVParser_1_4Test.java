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

class CSVParser_1_4Test {

    private Charset charset;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        charset = Charset.defaultCharset();
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_withValidFileCharsetAndFormat_shouldReturnCSVParserInstance() throws Exception {
        // Use a real File instance pointing to an existing file
        File realFile = new File("src/test/resources/dummy.csv");
        assertTrue(realFile.exists(), "Test file must exist at src/test/resources/dummy.csv");

        CSVParser parser = CSVParser.parse(realFile, charset, formatMock);
        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(formatMock, formatField.get(parser));
    }

    @Test
    @Timeout(8000)
    void parse_withNullFile_shouldThrowNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, charset, formatMock);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_shouldThrowNullPointerException() {
        File realFile = new File("src/test/resources/dummy.csv");
        assertTrue(realFile.exists(), "Test file must exist at src/test/resources/dummy.csv");

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(realFile, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }
}