package com.project.InsureCompare.application.mappers;

import org.springframework.stereotype.Component;

import com.project.InsureCompare.application.dto.InsuranceDTO;
import com.project.InsureCompare.domain.entity.Insurance;

@Component
public class InsuranceMapper {

    public Insurance toEntity(InsuranceDTO dto) {
        Insurance insurance = new Insurance();
        insurance.setType(dto.type());
        insurance.setBasePrice(dto.basePrice());
        return insurance;
    }

    public InsuranceDTO toDTO(Insurance insurance) {
        return new InsuranceDTO(
            insurance.getType(),
            insurance.getBasePrice()
        );
    }
}
