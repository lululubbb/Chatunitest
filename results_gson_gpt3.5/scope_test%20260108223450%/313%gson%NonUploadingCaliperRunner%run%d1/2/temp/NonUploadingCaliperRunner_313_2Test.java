package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class NonUploadingCaliperRunner_313_2Test {

  @Test
    @Timeout(8000)
  void run_invokesCaliperMainWithModifiedArgs() {
    Class<?> testClass = String.class;
    String[] originalArgs = new String[] {"arg1", "arg2"};

    try (MockedStatic<CaliperMain> caliperMainMock = Mockito.mockStatic(CaliperMain.class)) {
      NonUploadingCaliperRunner.run(testClass, originalArgs);

      caliperMainMock.verify(() -> CaliperMain.main(eq(testClass), argThat(args -> {
        if (args.length != originalArgs.length + 1) return false;
        if (!args[0].startsWith("-Cresults.upload.options.url=")) return false;
        for (int i = 1; i < args.length; i++) {
          if (!args[i].equals(originalArgs[i - 1])) return false;
        }
        return true;
      })));
    }
  }

  @Test
    @Timeout(8000)
  void concat_withOneStringAndMultipleOthers_returnsConcatenatedArray() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "-Cresults.upload.options.url=";
    String[] others = new String[] {"arg1", "arg2"};

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals(first, result[0]);
    assertEquals("arg1", result[1]);
    assertEquals("arg2", result[2]);
  }

  @Test
    @Timeout(8000)
  void concat_withOneStringAndNoOthers_returnsArrayWithOneElement() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "-Cresults.upload.options.url=";
    String[] others = new String[0];

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals(first, result[0]);
  }
}