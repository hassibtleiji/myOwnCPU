import java.util.Arrays;

/**
 * ALU: 2 Longwords and 4 bits
 * 1000 – and
 * 1001 – or
 * 1010 – xor
 * 1011 – not (not “a”; ignore b)
 * 1100 – left shift ( “a” is the value to shift, “b” is the amount to shift it; ignore all but the lowest 5 bits)
 * 1101 – right shift ( “a” is the value to shift, “b” is the amount to shift it; ignore all but the lowest 5 bits)
 * 1110 – add
 * 1111 – subtract
 * 0111 - multiply
 */
public class ALU {

    public static bit[] and       = new bit[]{toBit(1),toBit(0),toBit(0),toBit(0)};
    public static bit[] or        = new bit[]{toBit(1),toBit(0),toBit(0),toBit(1)};
    public static bit[] xor       = new bit[]{toBit(1),toBit(0),toBit(1),toBit(0)};
    public static bit[] not       = new bit[]{toBit(1),toBit(0),toBit(1),toBit(1)};
    public static bit[] lShift    = new bit[]{toBit(1),toBit(1),toBit(0),toBit(0)};
    public static bit[] rShift    = new bit[]{toBit(1),toBit(1),toBit(0),toBit(1)};
    public static bit[] add       = new bit[]{toBit(1),toBit(1),toBit(1),toBit(0)};
    public static bit[] subtract  = new bit[]{toBit(1),toBit(1),toBit(1),toBit(1)};
    public static bit[] multiply  = new bit[]{toBit(0),toBit(1),toBit(1),toBit(1)};

    /**
     * 9 ALU passing:
     * operation: bit array(4)
     * a: first input
     * b: second input
     */
    public static Longword opALU(bit[] op, Longword a, Longword b){

        if (compareBit(op, and)) return a.and(b);
        else if (compareBit(op, or)) return a.or(b);
        else if (compareBit(op, xor)) return a.xor(b);
        else if (compareBit(op, not)) return a.not();
        else if (compareBit(op, lShift)) return a.leftShift(lowestFiveBit(b).getSigned());
        else if (compareBit(op, rShift)) {
            Longword tmp = a.rightShift(lowestFiveBit(b).getSigned());
            return tmp;
        }
        else if (compareBit(op, add)) return rippleAdder.add(a,b);
        else if (compareBit(op, subtract)) return rippleAdder.subtract(a,b);
        else if (compareBit(op, multiply)) return Multiplier.multiply(a,b);

        return new Longword();
    }

    /**
     * Compare two bit[], equal:true, else:false
     */
    public static boolean compareBit(bit[] a, bit[] b){
        if(Arrays.deepEquals(a,b)) return true;
        if (a == null || b == null || b.length != a.length ) return false;
        for (int i = 0; i < b.length; i++)
            if(a[i] == null || b[i] == null || a[i].getValue() != b[i].getValue()) return false;
        return true;
    }

    /**
     * origin value: Longword a (32bit)
     * left Shift(27): xxxxx000000000000000000000000000
     * right Shift(27): 000000000000000000000000000xxxxx
     */
    private static Longword lowestFiveBit(Longword a){
        Longword tmp = a.leftShift(27).rightShift(27);
        return tmp;
    }

    /**
     * return bit value from 1 or 0: helper function
     */
    private static bit toBit(int value){
        bit conv = new bit();
        conv.set(value == 1);
        return conv;
    }

}
