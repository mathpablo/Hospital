package com.example.Hospital.Enum;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum Specialty {
    CARDIOLOGIA("CAR"),
    NEUROLOGIA("NEU"),
    ORTOPEDIA("ORT"),
    PEDIATRIA("PED");

    private String code;

    Specialty(String code){
        this.code = code;

    }

    @JsonCreator
    public String getCode(){
        return code;
    }

    public static Specialty fromString(String str){
        if(str == null){
            throw new IllegalStateException("Especialidade não pode ser nula");
        }
        String normalized = str.trim().toUpperCase();

        for (Specialty s: Specialty.values()){
            if(s.name().equals(normalized) || s.code.equalsIgnoreCase(str.trim())){
                return s ;
            }
        }
        throw new IllegalStateException("Especialidae inválida" + str);
    }
}
