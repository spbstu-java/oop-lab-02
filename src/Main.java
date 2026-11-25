import java.util.Map;

public final class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        Warehouse.Product mugs = new Warehouse.Product("Кружка", 0.25);
        Warehouse.Product plates = new Warehouse.Product("Тарелка", 0.4);

        warehouse.addStock(mugs, 24);
        warehouse.addStock(plates, 10);
        warehouse.removeStock(mugs, 1);

        Warehouse.Shipment shipment = new Warehouse.Shipment(Map.of(mugs, 6, plates, 4));
        try {
            System.out.println(WarehouseDemo.run(warehouse, shipment, "Магазин"));
        } catch (ReflectiveOperationException exception) {
            System.err.println("Ошибка выполнения — " + exception.getCause());
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }
}
