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

public class SerializationBenchmark_633_4Test {

  @Test
    @Timeout(8000)
  void testMain_invokesNonUploadingCaliperRunnerRun() {
    try (MockedStatic<NonUploadingCaliperRunner> mockedRunner = Mockito.mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = new String[] {"arg1", "arg2"};

      SerializationBenchmark.main(args);

      mockedRunner.verify(() -> NonUploadingCaliperRunner.run(SerializationBenchmark.class, args));
    }
  }

  @Test
    @Timeout(8000)
  void testSetUp_andTimeObjectSerialization_reflection() throws Exception {
    SerializationBenchmark benchmark = new SerializationBenchmark();

    // Invoke private setUp method via reflection
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Prepare to invoke timeObjectSerialization
    Method timeMethod = SerializationBenchmark.class.getDeclaredMethod("timeObjectSerialization", int.class);
    timeMethod.setAccessible(true);

    // Call with reps = 1
    timeMethod.invoke(benchmark, 1);

    // Call with reps = 0 to cover branch if any
    timeMethod.invoke(benchmark, 0);
  }
}