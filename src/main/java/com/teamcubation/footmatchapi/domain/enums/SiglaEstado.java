package com.teamcubation.footmatchapi.domain.enums;

import java.util.Arrays;

public enum SiglaEstado {

    AC, AL, AP, AM, BA, CE, DF, ES, GO,
    MT, MS, MG, PA, PB, PR, PE, PI, RJ,
    RS, RO, RR, SC, SP, SE, TO, MA, RN;

    public static boolean isValido(String siglaEstado) {
        return Arrays.stream(values()).anyMatch(e -> e.name().equalsIgnoreCase(siglaEstado));
    }
}

