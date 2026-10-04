package demo;

import domain.SampleService;
import invoker.InvocationResult;
import invoker.MethodInvoker;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SampleService service = new SampleService();
        MethodInvoker invoker = new MethodInvoker();

        System.out.println("Вызов аннотированных protected и private методов:");
        List<InvocationResult> results = invoker.invokeAnnotated(service);
        results.forEach(result -> System.out.println("  " + result.describe()));

        System.out.println();
        System.out.println("Всего вызовов: " + results.size());
        System.out.println("Вызовов по методам: " + results.stream()
                .collect(Collectors.groupingBy(r -> r.method().getName(), TreeMap::new, Collectors.counting())));
        System.out.println("Записи журнала: " + service.getJournal());
    }
}