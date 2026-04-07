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

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; }
    public String getItemType() { return itemType; }
    public List<String> getOrderHistory() { return orderHistory; }

    public void setName(String name) { this.name = name; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public void addOrderHistory(String historyEntry) {
        orderHistory.add(historyEntry);
    }
}
