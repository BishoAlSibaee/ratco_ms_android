package com.syrsoft.ratcoms.SALESActivities;

import java.util.List;

public class TermsAndConditions {

    String DeliveryLocation ;
    String Availability ;
    String Installation ;
    String Warranty ;
    List<PaymentTerms> Payment ;

    public void setDeliveryLocation(String deliveryLocation) {
        DeliveryLocation = deliveryLocation;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public void setInstallation(String installation) {
        Installation = installation;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public void setPayment(List<PaymentTerms> payment) {
        Payment = payment;
    }

    public String getDeliveryLocation() {
        return DeliveryLocation;
    }

    public String getAvailability() {
        return Availability;
    }

    public String getInstallation() {
        return Installation;
    }

    public String getWarranty() {
        return Warranty;
    }

    public List<PaymentTerms> getPayment() {
        return Payment;
    }
}
