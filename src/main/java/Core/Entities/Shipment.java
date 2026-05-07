package Core.Entities;

import java.time.LocalDateTime;

public class Shipment {
    private Integer id;
    private Integer orderId;
    private LocalDateTime date;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public Shipment() {
    }

    public Shipment(Integer id, Integer orderId, LocalDateTime date, String address, String city, String state,
                    String country, String zipCode) {
        this.id = id;
        this.orderId = orderId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
