package common;

public class ServicePrice {
    private String serviceType;
    private double price;

    public ServicePrice(String serviceType, double price) {
        if (serviceType == null || serviceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Service type is required.");
        }
        if (serviceType.contains("|")) {
            throw new IllegalArgumentException("Service type cannot contain '|'.");
        }
        if (Double.isNaN(price) || Double.isInfinite(price) || price < 0) {
            throw new IllegalArgumentException("Price must be a valid non-negative number.");
        }
        this.serviceType = serviceType.trim();
        this.price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (Double.isNaN(price) || Double.isInfinite(price) || price < 0) {
            throw new IllegalArgumentException("Price must be a valid non-negative number.");
        }
        this.price = price;
    }
}
