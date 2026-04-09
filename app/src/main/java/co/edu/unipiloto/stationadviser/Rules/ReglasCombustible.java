package co.edu.unipiloto.stationadviser.Rules;

import java.util.HashMap;
import java.util.Map;

public class ReglasCombustible {

    // Umbrales mínimos por tipo de combustible (litros)
    private static final Map<String, Integer> UMBRALES_MINIMOS = new HashMap<>();

    static {
        UMBRALES_MINIMOS.put("Corriente", 500);   // 500 litros mínimo
        UMBRALES_MINIMOS.put("Extra", 300);       // 300 litros mínimo
        UMBRALES_MINIMOS.put("Diesel", 800);      // 800 litros mínimo
    }

    // Niveles críticos (alerta roja)
    private static final Map<String, Integer> UMBRALES_CRITICOS = new HashMap<>();

    static {
        UMBRALES_CRITICOS.put("Corriente", 200);
        UMBRALES_CRITICOS.put("Extra", 100);
        UMBRALES_CRITICOS.put("Diesel", 300);
    }

    /**
     * Verifica si el nivel está por debajo del mínimo
     */
    public static boolean isNivelBajo(String tipoCombustible, int cantidadActual) {
        Integer umbral = UMBRALES_MINIMOS.get(tipoCombustible);
        if (umbral == null) return false;
        return cantidadActual < umbral;
    }

    /**
     * Verifica si el nivel está en estado crítico
     */
    public static boolean isNivelCritico(String tipoCombustible, int cantidadActual) {
        Integer umbral = UMBRALES_CRITICOS.get(tipoCombustible);
        if (umbral == null) return false;
        return cantidadActual < umbral;
    }

    /**
     * Obtiene el nivel de alerta
     * @return "NORMAL", "BAJO", "CRITICO"
     */
    public static String getNivelAlerta(String tipoCombustible, int cantidadActual) {
        if (isNivelCritico(tipoCombustible, cantidadActual)) {
            return "CRITICO";
        } else if (isNivelBajo(tipoCombustible, cantidadActual)) {
            return "BAJO";
        } else {
            return "NORMAL";
        }
    }

    /**
     * Obtiene el mensaje de alerta correspondiente
     */
    public static String getMensajeAlerta(String tipoCombustible, int cantidadActual) {
        String nivel = getNivelAlerta(tipoCombustible, cantidadActual);
        switch (nivel) {
            case "CRITICO":
                return "⚠️ ALERTA CRÍTICA: " + tipoCombustible + " está en nivel crítico (" + cantidadActual + " litros). ¡Requiere acción inmediata!";
            case "BAJO":
                return "⚠️ ALERTA: " + tipoCombustible + " tiene nivel bajo (" + cantidadActual + " litros). Mínimo recomendado: " + getUmbralMinimo(tipoCombustible) + " litros.";
            default:
                return "✅ " + tipoCombustible + " en nivel normal (" + cantidadActual + " litros)";
        }
    }

    /**
     * Obtiene el umbral mínimo para un tipo de combustible (compatible API 21)
     */
    public static int getUmbralMinimo(String tipoCombustible) {
        Integer valor = UMBRALES_MINIMOS.get(tipoCombustible);
        if (valor != null) {
            return valor;
        }
        return 0;
    }

    /**
     * Obtiene el umbral crítico para un tipo de combustible (compatible API 21)

    public static int getUmbralCritico(String tipoCombustible) {
        Integer valor = UMBRALES_CRITICOS.get(tipoCombustible);
        if (valor != null) {
            return valor;
        }
        return 0;
    }
     */
}