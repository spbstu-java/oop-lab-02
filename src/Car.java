public class Car
{

    @Repeat(5)
    public void showModel(String model)
    {
        System.out.println("Модель автомобиля: " + model);
    }

    public void showYear(int year)
    {
        System.out.println("Год выпуска: " + year);
    }

    @Repeat(2)
    protected void showMaxSpeed(int speed)
    {
        System.out.println("Максимальная скорость автомобиля " + speed + " км/ч");
    }

    @Repeat(3)
    protected void brake(boolean smoothly)
    {
        System.out.println("Плавное торможение: " + smoothly);
    }

    @Repeat(2)
    private void refuel(String fuel, double liters)
    {
        System.out.println("Заправка: " + fuel + ", объём: " + liters + " л");
    }

    @Repeat(1)
    private void changeWheels(Wheel wheel)
    {
        System.out.println("Установка колёс: " + wheel);
    }

    public static class Wheel
    {
        public Wheel() {}
        @Override
        public String toString()
        {
            return "Зимнее колесо";
        }
    }
}