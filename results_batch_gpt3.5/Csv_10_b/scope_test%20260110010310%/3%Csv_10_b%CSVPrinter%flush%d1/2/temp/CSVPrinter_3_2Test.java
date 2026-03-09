package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterFlushTest {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_flushCalled() throws IOException {
        Flushable flushableOut = mock(Flushable.class);

        Object proxy = Proxy.newProxyInstance(
                Appendable.class.getClassLoader(),
                new Class<?>[]{Appendable.class, Flushable.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxyObj, Method method, Object[] args) throws Throwable {
                        if ("flush".equals(method.getName()) && method.getParameterCount() == 0) {
                            flushableOut.flush();
                            return null;
                        }
                        if ("append".equals(method.getName())) {
                            // Return the proxy itself to satisfy Appendable contract
                            return proxyObj;
                        }
                        if ("toString".equals(method.getName()) && method.getParameterCount() == 0) {
                            return "ProxyAppendableFlushable";
                        }
                        if ("hashCode".equals(method.getName()) && method.getParameterCount() == 0) {
                            return System.identityHashCode(proxyObj);
                        }
                        if ("equals".equals(method.getName()) && method.getParameterCount() == 1) {
                            return proxyObj == args[0];
                        }
                        return null;
                    }
                });

        CSVPrinter printer = new CSVPrinter((Appendable) proxy, format);

        printer.flush();

        verify(flushableOut, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noException() throws IOException {
        Appendable nonFlushableOut = mock(Appendable.class);
        CSVPrinter printer = new CSVPrinter(nonFlushableOut, format);

        assertDoesNotThrow(printer::flush);
    }
}