package main;

import java.sql.Date;

public class Customer {
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private Date customerCreatedDate;
    private String customerCreatedBy;
    private Date customerLastUpdatedDate;
    private String customerLastUpdatedBy;
    private int customerDivisionId;

    /**
     * The constructor for the Customer class.
     */
    public Customer(int id, String name, String address, String postalCode, String phone, Date createdDate, String createdBy, Date lastUpdatedDate, String lastUpdatedBy, int divisionId) {
        this.customerId = id;
        this.customerName = name;
        this.customerAddress = address;
        this.customerPostalCode = postalCode;
        this.customerPhone = phone;
        this.customerCreatedDate = createdDate;
        this.customerCreatedBy = createdBy;
        this.customerLastUpdatedDate = lastUpdatedDate;
        this.customerLastUpdatedBy = lastUpdatedBy;
        this.customerDivisionId = divisionId;
    }

    /**
     * @param id the customers id
     */
    public void setCustomerId(int id) {
        this.customerId = id;
    }

    /**
     * @return the customers id
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param name the customers full name
     */
    public void setCustomerName(String name) {
        this.customerName = name;
    }

    /**
     * @return the customers full name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param address the customers address
     */
    public void setCustomerAddress(String address) {
        this.customerAddress = address;
    }

    /**
     * @return the customers address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param postalCode the customers postal code
     */
    public void setCustomerPostalCode(String postalCode) {
        this.customerPostalCode = postalCode;
    }

    /**
     * @return the customers postal code
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * @param phone the customers phone number
     */
    public void setCustomerPhone(String phone) {
        this.customerPhone = phone;
    }

    /**
     * @return the customers phone number
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param createdDate the date the customer was created
     */
    public void setCustomerCreatedDate(Date createdDate) {
        this.customerCreatedDate = createdDate;
    }

    /**
     * @return the date the customer was created
     */
    public Date getCustomerCreatedDate() {
        return customerCreatedDate;
    }

    /**
     * @param createdBy who the customer was created by
     */
    public void setCustomerCreatedBy(String createdBy) {
        this.customerCreatedBy = createdBy;
    }

    /**
     * @return who the customer was created by
     */
    public String getCustomerCreatedBy() {
        return customerCreatedBy;
    }

    /**
     * @param lastUpdatedDate the date the customers information was last updated
     */
    public void setCustomerLastUpdatedDate(Date lastUpdatedDate) {
        this.customerLastUpdatedDate = lastUpdatedDate;
    }

    /**
     * @return the date the customers information was last updated
     */
    public Date getCustomerLastUpdatedDate() {
        return customerLastUpdatedDate;
    }

    /**
     * @param lastUpdatedBy who the customer was last updated by
     */
    public void setCustomerLastUpdatedBy(String lastUpdatedBy) {
        this.customerLastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return who the customer was last updated by
     */
    public String getCustomerLastUpdatedBy() {
        return customerLastUpdatedBy;
    }

    /**
     * @param divisionId the customers division id
     */
    public void setCustomerDivisionId(int divisionId) {
        this.customerDivisionId = divisionId;
    }

    /**
     * @return the customers division id
     */
    public int getCustomerDivisionId() {
        return customerDivisionId;
    }

}
