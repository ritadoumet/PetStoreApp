package com.androidproject.petstoreapp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ServiceType {
    GROOMING(10),
    PET_WALKING(7),
    SURGERY(120),
    PET_TRAINING(50),
    HAIRCUT(20),
    NAIL_TRIMMING(20);

    private final int price;

    ServiceType(int price){
        this.price=price;
    }
    private static Map< ServiceType, Integer> map = new HashMap<ServiceType, Integer>();

    static {
        for (ServiceType serviceTypeEnum: ServiceType.values()){
            map.put(serviceTypeEnum, serviceTypeEnum.price );
        }
    }

    public static int valueOf(ServiceType serviceType){
        return map.get(serviceType);
    }

}
