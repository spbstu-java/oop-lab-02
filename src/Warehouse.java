import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public final class Warehouse {
    private final Map<Product, Integer> stock = new HashMap<>();

    public record Product(String name, double weightPerUnitKg) {
        public Product {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Укажите название товара");
            }
            if (!Double.isFinite(weightPerUnitKg) || weightPerUnitKg <= 0) {
                throw new IllegalArgumentException("Масса единицы товара должна быть положительным конечным числом");
            }
            name = name.strip();
        }
    }

    public record Shipment(Map<Product, Integer> quantities) {
        public Shipment {
            quantities = Map.copyOf(quantities);
            if (quantities.isEmpty()) {
                throw new IllegalArgumentException("Укажите хотя бы одну позицию для отгрузки");
            }
            quantities.values().forEach(Warehouse::requirePositiveQuantity);
        }
    }

    public record Packaging(int weightGrams) {}

    public void addStock(Product product, int quantity) {
        Objects.requireNonNull(product, "Укажите товар");
        requirePositiveQuantity(quantity);
        stock.merge(product, quantity, Math::addExact);
    }

    public void removeStock(Product product, int quantity) {
        requirePositiveQuantity(quantity);
        int available = stockOf(product);
        if (quantity > available) {
            throw new IllegalStateException(
                    "Недостаточно товара «%s» для списания: запрошено %d шт., есть %d шт."
                            .formatted(product.name(), quantity, available));
        }
        stock.put(product, available - quantity);
    }

    public int stockOf(Product product) {
        Objects.requireNonNull(product, "Укажите товар");
        return stock.getOrDefault(product, 0);
    }

    @Repeat(1)
    protected int countCompleteShipments(Shipment shipment) {
        return shipment.quantities().entrySet().stream()
                .mapToInt(line -> stockOf(line.getKey()) / line.getValue())
                .min()
                .orElseThrow();
    }

    protected String findShortages(Shipment shipment) {
        StringJoiner shortages = new StringJoiner(", ");
        shipment.quantities().forEach((product, quantity) -> {
            int available = stockOf(product);
            if (available < quantity) {
                shortages.add("%s (нужно %d шт., есть %d шт.)".formatted(product.name(), quantity, available));
            }
        });
        return shortages.toString();
    }

    @Repeat(1)
    private String calculateShipmentWeight(Shipment shipment, Packaging packaging) {
        int packagingWeightGrams = packaging.weightGrams();
        BigDecimal kilograms = BigDecimal.valueOf(packagingWeightGrams, 3);
        for (var line : shipment.quantities().entrySet()) {
            BigDecimal weightPerUnit = BigDecimal.valueOf(line.getKey().weightPerUnitKg());
            kilograms = kilograms.add(weightPerUnit.multiply(BigDecimal.valueOf(line.getValue())));
        }
        return "%s кг (упаковка — %d г)"
                .formatted(kilograms.stripTrailingZeros().toPlainString(), packagingWeightGrams);
    }

    @Repeat(3)
    private String shipBatch(Shipment shipment, String destination) {
        String shortages = findShortages(shipment);
        if (!shortages.isEmpty()) {
            return "Отгрузка отклонена. Недостаточно товара: " + shortages;
        }

        StringJoiner shipped = new StringJoiner(", ");
        shipment.quantities().forEach((product, quantity) -> {
            removeStock(product, quantity);
            shipped.add("%s — %d шт.".formatted(product.name(), quantity));
        });
        return "Отгружено в «%s»: %s".formatted(destination, shipped);
    }

    private static void requirePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество товара должно быть положительным");
        }
    }
}
