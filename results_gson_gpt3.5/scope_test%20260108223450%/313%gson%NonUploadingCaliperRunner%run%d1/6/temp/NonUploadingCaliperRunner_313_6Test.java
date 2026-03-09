package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class NonUploadingCaliperRunner_313_6Test {

  private static MockedStatic<CaliperMain> caliperMainMock;

  @BeforeAll
  static void setUp() {
    caliperMainMock = mockStatic(CaliperMain.class);
  }

  @AfterAll
  static void tearDown() {
    caliperMainMock.close();
  }

  @Test
    @Timeout(8000)
  void run_callsCaliperMainWithConcatenatedArgs() {
    Class<?> testClass = String.class;
    String[] inputArgs = new String[] {"arg1", "arg2"};
    NonUploadingCaliperRunner.run(testClass, inputArgs);

    caliperMainMock.verify(() -> CaliperMain.main(eq(testClass), eq(new String[] {
        "-Cresults.upload.options.url=", "arg1", "arg2"
    })));
  }

  @Test
    @Timeout(8000)
  void concat_singleFirstOnly_returnsArrayWithFirst() throws Exception {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "prefix";
    String[] others = new String[0];
    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assert result.length == 1;
    assert result[0].equals("prefix");
  }

  @Test
    @Timeout(8000)
  void concat_firstWithOthers_returnsArrayWithAll() throws Exception {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "prefix";
    String[] others = new String[] {"one", "two"};
    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assert result.length == 3;
    assert result[0].equals("prefix");
    assert result[1].equals("one");
    assert result[2].equals("two");
  }
}