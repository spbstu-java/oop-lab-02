import java.util.Arrays;

public class Workshop {
    @Repeat(4)
    public void open(String name) {
        System.out.println("Открытие мастерской: " + name);
    }

    public void countTools(int count) {
        System.out.println("Инструментов: " + count);
    }

    @Repeat(2)
    protected void checkTemperature(double temperature, boolean calibrated) {
        System.out.printf("Проверка температуры: %s, калибровка: %s%n", temperature, calibrated);
    }

    protected void clean(String room) {
        System.out.println("Уборка: " + room);
    }

    @Repeat(3)
    private void inspectOrder(Order order, int[] measurements) {
        System.out.printf("Проверка заказа: %s, замеры: %s%n", order, Arrays.toString(measurements));
    }

    @Repeat(1)
    private void mark(char mark) {
        System.out.println("Метка: " + (int) mark + " (код символа)");
    }

    record Order(Tool tool, int quantity) {
    }

    record Tool(String name) {
    }
}
