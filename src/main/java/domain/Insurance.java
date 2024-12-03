package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Insurance {

    @Id
    private Long id;  
    private String type; 
    private Double basePrice; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
}