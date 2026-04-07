package model;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private final int id;
    private String name;
    private String contactInfo;
    private String itemType;
    private final List<String> orderHistory;

    public Supplier(int id, String name, String contactInfo, String itemType) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.itemType = itemType;
        this.orderHistory = new ArrayList<>();
    }
}
