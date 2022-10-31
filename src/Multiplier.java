/**
 * Multiplier class for Longword type values
 */

public class Multiplier {

    /**
     * Multiply function for Longword
     * @param a: input value
     * @param b: input value
     * @return a * b
     */
    public static Longword multiply(Longword a, Longword b){

        // return Longword value
        Longword multiply_value = new Longword();

        for (int i = b.LONGWORD - 1; i >= 0; i--){
            Longword addTmp = new Longword();
            for (int j = a.LONGWORD - 1; j >= 0; j--){
                int aBit = toInt(a.getBit(j).getValue());
                int bBit = toInt(b.getBit(i).getValue());
                // ex: 1011*0010: int(0) * int(1) = int(0), int(1) * int(1) = int(1)
                addTmp.setBit(j, toBit(aBit * bBit));
            }

            // left shift(length - i)
            int length = a.LONGWORD - 1;
            multiply_value = rippleAdder.add(multiply_value, addTmp.leftShift(length - i));
        }
        return multiply_value;
    }

    /**
     * Get 1 or 0 from true or false: helper function
     */
    private static int toInt(boolean value){
        if (value) return 1;
        else return 0;
    }

    /**
     * convert Int type to Bit type
     * return bit value from 1 or 0: helper function
     */
    private static bit toBit(int value){
        bit conv = new bit();
        conv.set(value == 1);
        return conv;
    }
}
