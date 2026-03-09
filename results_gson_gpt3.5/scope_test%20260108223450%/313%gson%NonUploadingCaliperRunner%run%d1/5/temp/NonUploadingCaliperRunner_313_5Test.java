package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class NonUploadingCaliperRunner_313_5Test {

  @Test
    @Timeout(8000)
  void run_invokesCaliperMainWithModifiedArgs() {
    try (MockedStatic<CaliperMain> caliperMainMock = mockStatic(CaliperMain.class)) {
      Class<?> testClass = String.class;
      String[] originalArgs = new String[] {"-Xms512m", "-Xmx1024m"};

      NonUploadingCaliperRunner.run(testClass, originalArgs);

      String[] expectedArgs = new String[originalArgs.length + 1];
      expectedArgs[0] = "-Cresults.upload.options.url=";
      System.arraycopy(originalArgs, 0, expectedArgs, 1, originalArgs.length);

      caliperMainMock.verify(() -> CaliperMain.main(eq(testClass), eq(expectedArgs)));
    }
  }

  @Test
    @Timeout(8000)
  void concat_returnsArrayWithFirstAndOthers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "prefix";
    String[] others = new String[] {"a", "b", "c"};

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    // Expected array: first element is 'prefix', followed by "a","b","c"
    assert result.length == others.length + 1;
    assert result[0].equals(first);
    for (int i = 0; i < others.length; i++) {
      assert result[i + 1].equals(others[i]);
    }
  }

  @Test
    @Timeout(8000)
  void concat_returnsArrayWithOnlyFirstWhenOthersEmpty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "onlyFirst";
    String[] others = new String[0];

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assert result.length == 1;
    assert result[0].equals(first);
  }

}