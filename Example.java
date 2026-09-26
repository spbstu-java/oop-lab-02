public class Example {

    private void methodTextA(String a) {
        IO.println("Строка A: " + a);
    }

    @MyAnnotation(count = 2)
    private void methodSum(int a, int b) {
        IO.println("Сумма A+B: " + (a + b));
    }

    @MyAnnotation(count = 3)
    private void methodArrayAndObject(int[] numbers, Person person) {
        IO.println("Массив чисел содержит " + numbers.length + " элементов, а переданный перс: " + person);
    }

    @MyAnnotation(count = 4)
    protected void methodBoolean(boolean c) {
        IO.println("Метка: " + c);
    }

    protected void methodMultiply(double a, double b) {
        IO.println("Произведение AxB: " + (a * b));
    }

    protected void methodInfo(String info, int code) {
        IO.println("Инфо: " + info + ", код: " + code);
    }

    @MyAnnotation(count = 6)
    public void methodDivision(int e, int f) {
        IO.println("Деление E/F: " + (e / f));
    }

    public void methodE(int e) {
        IO.println("Число E: " + e);
    }
}