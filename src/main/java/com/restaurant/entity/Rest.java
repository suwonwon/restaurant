package com.restaurant.entity;

import com.restaurant.dto.RestFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="rest")
@Getter
@Setter
@ToString
public class Rest extends BaseEntity{
    @Id
    @Column(name="rest_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String restNm;

    @Column(name = "restPhone", nullable = false)
    private String restPhone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String category;

    @Lob
    @Column(nullable = false)
    private String introduction;

    @Lob
    @Column(nullable = false)
    private String restDetail;

    public void updateRest(RestFormDto restFormDto){
        this.restNm = restFormDto.getRestNm();
        this.restPhone = restFormDto.getRestPhone();
        this.address = restFormDto.getAddress();
        this.category = restFormDto.getCategory();
        this.introduction= restFormDto.getIntroduction();
        this.restDetail= restFormDto.getRestDetail();
    }

    @Column(nullable = true)
    private String region;

    public void setAddress(String address) {
        this.address = address;
        this.region = extractRegionFromAddress(address);
    }

    private String extractRegionFromAddress(String address) {
        if (address != null && !address.trim().isEmpty()) {
            String[] parts = address.split(" ");
            if (parts.length > 1) {
                return parts[1];
            }
        }
        return null;
    }
}
