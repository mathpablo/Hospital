package com.example.Hospital.Enum;

import lombok.Data;

public enum Specialty {
    CARDIOLOGIA("CAR"),
    NEUROLOGIA("NEU"),
    ORTOPEDIA("ORT"),
    PEDIATRIA("PED");

    private String code;

    Specialty(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
