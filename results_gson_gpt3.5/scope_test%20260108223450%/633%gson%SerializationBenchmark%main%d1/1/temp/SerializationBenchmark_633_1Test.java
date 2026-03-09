package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class SerializationBenchmark_633_1Test {

  @Test
    @Timeout(8000)
  public void testMain_invokesNonUploadingCaliperRunnerRun() {
    try (MockedStatic<NonUploadingCaliperRunner> mockedRunner = Mockito.mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = new String[] {"arg1", "arg2"};

      SerializationBenchmark.main(args);

      mockedRunner.verify(() -> NonUploadingCaliperRunner.run(SerializationBenchmark.class, args));
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateSetUpMethod_invocation() throws Exception {
    SerializationBenchmark instance = new SerializationBenchmark();

    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(instance);
  }
}