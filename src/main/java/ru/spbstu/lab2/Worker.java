package main.java.ru.spbstu.lab2;

/** Класс с методами разных уровней доступа */
public class Worker {
    public void showMessage(String message) {
        System.out.println("Публичный showMessage: " + message);
    }

    public int calculateSum(int first, int second) {
        int result = first + second;
        System.out.println("Публичный calculateSum: " + result);
        return result;
    }

    @Repeat(2)
    protected void printNumber(int number) {
        System.out.println("Защищённый printNumber: " + number);
    }

    @Repeat(3)
    protected void printText(String text, int count) {
        System.out.println("Защищённый printText: " + text + ", количество = " + count);
    }

    @Repeat(2)
    private void showCoordinates(int x, int y) {
        System.out.println("Приватный showCoordinates: x = " + x + ", y = " + y);
    }

    @Repeat(1)
    private void showInformation(String name, boolean active, double rating) {
        System.out.println("Приватный showInformation: " + name
                + ", активен = " + active + ", рейтинг = " + rating);
    }
}
