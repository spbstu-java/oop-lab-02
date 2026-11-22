
package main.java.ru.spbstu.lab2;

public class Main {

    public static void main(String[] args) {

        Worker worker = new Worker();

        System.out.println("Лабораторная работа № 2 - Аннотации");

        System.out.println("\nПубличные методы");

        worker.showMessage("Демонстрация публичного метода");
        worker.calculateSum(5, 7);

        System.out.println("\nАннотированные protected/private методы");

        AnnotationProcessor processor = new AnnotationProcessor();

        // Передаём функцию для получения аргументов
        processor.process(worker, parameter -> {

            Class<?> type = parameter.getType();

            if (type == int.class || type == Integer.class) {
                return 10;
            }

            if (type == double.class || type == Double.class) {
                return 4.5;
            }

            if (type == String.class) {
                return "Тестовая строка";
            }

            if (type == boolean.class || type == Boolean.class) {
                return true;
            }

            if (type == Person.class) {
                return new Person("Петя");
            }

            throw new IllegalArgumentException(
                    "Не настроено создание аргумента: "
                            + type.getName()
            );
        });

        System.out.println("\nПрограмма завершена.");
    }
}
