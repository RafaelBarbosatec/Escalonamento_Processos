package rafael.barbosa.escalonamento.util;

/**
 * Created by rafael on 15/08/17.
 */

public class Funcoes {

    public static String generateNome(int position) {

        switch (position){
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
            case 8: return "I";
            case 9: return "J";
            default: return "L";
        }

    }
}
