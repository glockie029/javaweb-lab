package com.example.lab01.repository;

import com.example.lab01.model.OrderRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderRepository {

    private static final List<OrderRecord> ORDERS;

    static {
        List<OrderRecord> orders = new ArrayList<OrderRecord>();
        orders.add(new OrderRecord(1001, "alice", "Java Web Security Book", 1));
        orders.add(new OrderRecord(1002, "alice", "Tomcat Lab Notebook", 2));
        orders.add(new OrderRecord(2001, "bob", "Spring Audit Guide", 1));
        orders.add(new OrderRecord(2002, "bob", "JDBC Practice Notes", 3));
        ORDERS = Collections.unmodifiableList(orders);
    }

    public OrderRecord findById(int id) {
        for (OrderRecord order : ORDERS) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public OrderRecord findByIdAndOwner(int id, String owner) {
        for (OrderRecord order : ORDERS) {
            if (order.getId() == id && order.getOwner().equals(owner)) {
                return order;
            }
        }
        return null;
    }

    public List<OrderRecord> findAllByOwner(String owner) {
        List<OrderRecord> results = new ArrayList<OrderRecord>();
        for (OrderRecord order : ORDERS) {
            if (order.getOwner().equals(owner)) {
                results.add(order);
            }
        }
        return results;
    }
}
