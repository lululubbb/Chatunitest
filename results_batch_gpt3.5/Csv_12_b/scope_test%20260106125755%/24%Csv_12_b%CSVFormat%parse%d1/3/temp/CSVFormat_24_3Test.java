package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

public class CSVFormat_24_3Test {

    @Mock
    private Reader mockReader;

    private CSVFormat csvFormat;

    private MockitoSession mockitoSession;

    @BeforeEach
    public void setUp() {
        mockitoSession = org.mockito.Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.LENIENT)
                .startMocking();
        csvFormat = CSVFormat.DEFAULT;
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (mockitoSession != null) {
            mockitoSession.finishMocking();
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ReturnsCSVParser() throws IOException {
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);
        // Verify that the CSVParser was created with the correct Reader and CSVFormat
        assertEquals(mockReader, getField(parser, "in"));
        assertEquals(csvFormat, getField(parser, "format"));
    }

    @Test
    @Timeout(8000)
    public void testParse_WithNullReader_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    // Helper method to access private fields via reflection
    private Object getField(Object instance, String fieldName) {
        try {
            java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
            return null;
        }
    }
}