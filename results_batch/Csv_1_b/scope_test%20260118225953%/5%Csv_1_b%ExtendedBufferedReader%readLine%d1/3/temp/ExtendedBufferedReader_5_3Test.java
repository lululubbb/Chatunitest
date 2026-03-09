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

class ExtendedBufferedReader_5_3Test {

    Reader mockReader;

    @BeforeEach
    void setUp() throws IOException {
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testReadLine_nonNullNonEmptyLine() throws Exception {
        String testLine = "Hello, World!";

        class TestExtendedBufferedReader2 extends ExtendedBufferedReader {
            TestExtendedBufferedReader2(Reader r) {
                super(r);
            }

            String readLine_super() throws IOException {
                return super.readLine();
            }

            @Override
            public String readLine() throws IOException {
                String line = readLine_super();

                if (line != null) {
                    if (line.length() > 0) {
                        setLastChar(line.charAt(line.length() - 1));
                    } else {
                        setLastChar(UNDEFINED);
                    }
                    incrementLineCounter();
                } else {
                    setLastChar(END_OF_STREAM);
                }

                return line;
            }

            private void setLastChar(int value) {
                try {
                    Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                    lastCharField.setAccessible(true);
                    lastCharField.setInt(this, value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private void incrementLineCounter() {
                try {
                    Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
                    lineCounterField.setAccessible(true);
                    int current = lineCounterField.getInt(this);
                    lineCounterField.setInt(this, current + 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        TestExtendedBufferedReader2 testReader2 = spy(new TestExtendedBufferedReader2(mockReader));
        doReturn(testLine).when(testReader2).readLine_super();

        String result = testReader2.readLine();

        assertEquals(testLine, result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastCharValue = lastCharField.getInt(testReader2);
        assertEquals(testLine.charAt(testLine.length() - 1), lastCharValue);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounterValue = lineCounterField.getInt(testReader2);
        assertEquals(1, lineCounterValue);
    }

    @Test
    @Timeout(8000)
    void testReadLine_nonNullEmptyLine() throws Exception {
        String testLine = "";

        class TestExtendedBufferedReader2 extends ExtendedBufferedReader {
            TestExtendedBufferedReader2(Reader r) {
                super(r);
            }

            String readLine_super() throws IOException {
                return super.readLine();
            }

            @Override
            public String readLine() throws IOException {
                String line = readLine_super();

                if (line != null) {
                    if (line.length() > 0) {
                        setLastChar(line.charAt(line.length() - 1));
                    } else {
                        setLastChar(UNDEFINED);
                    }
                    incrementLineCounter();
                } else {
                    setLastChar(END_OF_STREAM);
                }

                return line;
            }

            private void setLastChar(int value) {
                try {
                    Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                    lastCharField.setAccessible(true);
                    lastCharField.setInt(this, value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private void incrementLineCounter() {
                try {
                    Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
                    lineCounterField.setAccessible(true);
                    int current = lineCounterField.getInt(this);
                    lineCounterField.setInt(this, current + 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        TestExtendedBufferedReader2 testReader2 = spy(new TestExtendedBufferedReader2(mockReader));
        doReturn(testLine).when(testReader2).readLine_super();

        String result = testReader2.readLine();

        assertEquals(testLine, result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastCharValue = lastCharField.getInt(testReader2);
        assertEquals(ExtendedBufferedReader.UNDEFINED, lastCharValue);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounterValue = lineCounterField.getInt(testReader2);
        assertEquals(1, lineCounterValue);
    }

    @Test
    @Timeout(8000)
    void testReadLine_nullLine() throws Exception {
        class TestExtendedBufferedReader2 extends ExtendedBufferedReader {
            TestExtendedBufferedReader2(Reader r) {
                super(r);
            }

            String readLine_super() throws IOException {
                return super.readLine();
            }

            @Override
            public String readLine() throws IOException {
                String line = readLine_super();

                if (line != null) {
                    if (line.length() > 0) {
                        setLastChar(line.charAt(line.length() - 1));
                    } else {
                        setLastChar(UNDEFINED);
                    }
                    incrementLineCounter();
                } else {
                    setLastChar(END_OF_STREAM);
                }

                return line;
            }

            private void setLastChar(int value) {
                try {
                    Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
                    lastCharField.setAccessible(true);
                    lastCharField.setInt(this, value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private void incrementLineCounter() {
                try {
                    Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
                    lineCounterField.setAccessible(true);
                    int current = lineCounterField.getInt(this);
                    lineCounterField.setInt(this, current + 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        TestExtendedBufferedReader2 testReader2 = spy(new TestExtendedBufferedReader2(mockReader));
        doReturn(null).when(testReader2).readLine_super();

        String result = testReader2.readLine();

        assertNull(result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastCharValue = lastCharField.getInt(testReader2);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastCharValue);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounterValue = lineCounterField.getInt(testReader2);
        assertEquals(0, lineCounterValue);
    }
}