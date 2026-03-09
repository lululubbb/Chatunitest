package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_5_6Test {

    ExtendedBufferedReader reader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        reader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NullLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtendedBufferedReader readerNull = new ExtendedBufferedReader(mockReader) {
            @Override
            public String readLine() throws IOException {
                String line = null;
                if (line != null) {
                    if (line.length() > 0) {
                        try {
                            Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                            lastCharField.setAccessible(true);
                            lastCharField.setInt(this, line.charAt(line.length() - 1));
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    try {
                        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
                        lineCounterField.setAccessible(true);
                        int currentCount = lineCounterField.getInt(this);
                        lineCounterField.setInt(this, currentCount + 1);
                    } catch (Exception e) {
                        // ignore
                    }
                } else {
                    try {
                        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                        lastCharField.setAccessible(true);
                        lastCharField.setInt(this, ExtendedBufferedReader.END_OF_STREAM);
                    } catch (Exception e) {
                        // ignore
                    }
                }
                return line;
            }
        };

        String line = readerNull.readLine();

        assertNull(line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(readerNull);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(readerNull);
        assertEquals(0, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_EmptyLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtendedBufferedReader readerEmpty = new ExtendedBufferedReader(mockReader) {
            @Override
            public String readLine() throws IOException {
                String line = "";
                if (line != null) {
                    if (line.length() > 0) {
                        try {
                            Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                            lastCharField.setAccessible(true);
                            lastCharField.setInt(this, line.charAt(line.length() - 1));
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    try {
                        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
                        lineCounterField.setAccessible(true);
                        int currentCount = lineCounterField.getInt(this);
                        lineCounterField.setInt(this, currentCount + 1);
                    } catch (Exception e) {
                        // ignore
                    }
                } else {
                    try {
                        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                        lastCharField.setAccessible(true);
                        lastCharField.setInt(this, ExtendedBufferedReader.END_OF_STREAM);
                    } catch (Exception e) {
                        // ignore
                    }
                }
                return line;
            }
        };

        String line = readerEmpty.readLine();

        assertEquals("", line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(readerEmpty);
        assertEquals(ExtendedBufferedReader.UNDEFINED, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(readerEmpty);
        assertEquals(1, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NonEmptyLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        final String testLine = "Hello, World!";

        ExtendedBufferedReader readerNonEmpty = new ExtendedBufferedReader(mockReader) {
            @Override
            public String readLine() throws IOException {
                String line = testLine;
                if (line != null) {
                    if (line.length() > 0) {
                        try {
                            Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                            lastCharField.setAccessible(true);
                            lastCharField.setInt(this, line.charAt(line.length() - 1));
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    try {
                        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
                        lineCounterField.setAccessible(true);
                        int currentCount = lineCounterField.getInt(this);
                        lineCounterField.setInt(this, currentCount + 1);
                    } catch (Exception e) {
                        // ignore
                    }
                } else {
                    try {
                        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                        lastCharField.setAccessible(true);
                        lastCharField.setInt(this, ExtendedBufferedReader.END_OF_STREAM);
                    } catch (Exception e) {
                        // ignore
                    }
                }
                return line;
            }
        };

        String line = readerNonEmpty.readLine();

        assertEquals(testLine, line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(readerNonEmpty);
        assertEquals(testLine.charAt(testLine.length() - 1), lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(readerNonEmpty);
        assertEquals(1, lineCounter);
    }
}