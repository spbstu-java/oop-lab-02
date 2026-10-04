package invoker;

import annotation.Repeat;
import domain.Person;
import domain.SampleService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MethodInvokerTest {

    private final MethodInvoker invoker = new MethodInvoker();

    @Test
    void callsAnnotatedProtectedAndPrivateMethodsRepeatTimes() {
        SampleService service = new SampleService();

        List<InvocationResult> results = invoker.invokeAnnotated(service);

        Map<String, Long> calls = results.stream()
                .collect(Collectors.groupingBy(r -> r.method().getName(), Collectors.counting()));
        assertEquals(Map.of(
                "greet", 2L,
                "multiply", 3L,
                "describePerson", 1L,
                "average", 4L,
                "writeToJournal", 2L), calls);
        assertEquals(2, service.getJournal().size());
    }

    @Test
    void neverPassesNullArguments() {
        invoker.invokeAnnotated(new SampleService()).forEach(result ->
                assertTrue(Arrays.stream(result.arguments()).allMatch(Objects::nonNull)));
    }

    @Test
    void doesNotDependOnParametersAndSkipsPublicMethods() {
        ParametersProbe probe = new ParametersProbe();

        invoker.invokeAnnotated(probe);

        assertEquals(2 + 3 + 1, probe.calls);
    }

    @Test
    void rejectsNullTarget() {
        assertThrows(NullPointerException.class, () -> invoker.invokeAnnotated(null));
    }

    @Test
    void rejectsNegativeRepeatCount() {
        assertThrows(MethodInvocationException.class, () -> invoker.invokeAnnotated(new NegativeProbe()));
    }

    @Test
    void wrapsExceptionsThrownByInvokedMethods() {
        MethodInvocationException exception = assertThrows(MethodInvocationException.class,
                () -> invoker.invokeAnnotated(new ThrowingProbe()));
        assertInstanceOf(IllegalStateException.class, exception.getCause());
    }

    @Test
    void failsInsteadOfPassingNullForUnsupportedTypes() {
        assertThrows(MethodInvocationException.class, () -> invoker.invokeAnnotated(new UnsupportedProbe()));
    }

    @SuppressWarnings("unused")
    static class ParametersProbe {
        int calls;

        @Repeat(2)
        private void noParameters() {
            calls++;
        }

        @Repeat(3)
        protected void manyParameters(int a, String b, int[] c, double d) {
            calls++;
        }

        @Repeat(1)
        private void objectParameters(Person person, List<String> list, Map<String, Integer> map) {
            calls++;
        }

        @Repeat(5)
        public void publicIsIgnored() {
            calls += 100;
        }
    }

    @SuppressWarnings("unused")
    static class NegativeProbe {
        @Repeat(-1)
        private void method() {
        }
    }

    @SuppressWarnings("unused")
    static class ThrowingProbe {
        @Repeat(1)
        protected void fail() {
            throw new IllegalStateException("boom");
        }
    }

    @SuppressWarnings("unused")
    static class UnsupportedProbe {
        @Repeat(1)
        private void method(Runnable task) {
        }
    }
}