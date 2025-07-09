package com.teamcubation.footmatchapi.domain.enums;

import java.util.Arrays;

public enum SiglaEstado {

    AC, AL, AM, AP, BA, CE, DF, ES, GO,
    MA, MG, MS, MT, PA, PB, PE, PI, PR,
    RJ, RN, RO, RR, RS, SC, SE, SP, TO;

    public static boolean isValido(String siglaEstado) {
        return Arrays.stream(values()).anyMatch(e -> e.name().equalsIgnoreCase(siglaEstado));
    }
}

