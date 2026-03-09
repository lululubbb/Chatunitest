package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_68_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNonNullValues() throws Exception {
        StringBuilder appendable = new StringBuilder();

        class RawAppendable implements Appendable {
            private final StringBuilder sb;

            RawAppendable(StringBuilder sb) {
                this.sb = sb;
            }

            @Override
            public Appendable append(CharSequence csq) throws IOException {
                if (csq == null) {
                    sb.append("null");
                } else {
                    sb.append(csq);
                }
                return this;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                if (csq == null) {
                    sb.append("null");
                } else {
                    sb.append(csq, start, end);
                }
                return this;
            }

            @Override
            public Appendable append(char c) throws IOException {
                sb.append(c);
                return this;
            }
        }

        RawAppendable rawAppendable = new RawAppendable(appendable);
        CSVPrinter csvPrinter = new CSVPrinter(rawAppendable, format);

        csvPrinter.printRecord("a", 123, true);

        assertEquals("a123true\n", appendable.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValues() throws Exception {
        StringBuilder appendable = new StringBuilder();

        class RawAppendable implements Appendable {
            private final StringBuilder sb;

            RawAppendable(StringBuilder sb) {
                this.sb = sb;
            }

            @Override
            public Appendable append(CharSequence csq) throws IOException {
                if (csq == null) {
                    sb.append("null");
                } else {
                    sb.append(csq);
                }
                return this;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                if (csq == null) {
                    sb.append("null");
                } else {
                    sb.append(csq, start, end);
                }
                return this;
            }

            @Override
            public Appendable append(char c) throws IOException {
                sb.append(c);
                return this;
            }
        }

        RawAppendable rawAppendable = new RawAppendable(appendable);
        CSVPrinter csvPrinter = new CSVPrinter(rawAppendable, format);

        csvPrinter.printRecord("a", null, "c");

        assertEquals("anullc\n", appendable.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws Exception {
        StringBuilder appendable = new StringBuilder();

        class RawAppendable implements Appendable {
            private final StringBuilder sb;

            RawAppendable(StringBuilder sb) {
                this.sb = sb;
            }

            @Override
            public Appendable append(CharSequence csq) throws IOException {
                if (csq == null) {
                    sb.append("null");
                } else {
                    sb.append(csq);
                }
                return this;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                if (csq == null) {
                    sb.append("null");
                } else {
                    sb.append(csq, start, end);
                }
                return this;
            }

            @Override
            public Appendable append(char c) throws IOException {
                sb.append(c);
                return this;
            }
        }

        RawAppendable rawAppendable = new RawAppendable(appendable);
        CSVPrinter csvPrinter = new CSVPrinter(rawAppendable, format);

        csvPrinter.printRecord();

        assertEquals("\n", appendable.toString());
    }
}