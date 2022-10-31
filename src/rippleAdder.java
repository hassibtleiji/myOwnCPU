// rippleAdder class
// Add and Subtract for Longword

public class rippleAdder{
    /**
     * Add function for Longword
     * @param a:input value
     * @param b:input value
     * @return a+b value
     */
    public static Longword add(Longword a, Longword b){

        Longword sum = new Longword();

        int overflow = 0;
        // Setting bit value with true or false
        bit one = new bit();
        bit zero = new bit();
        one.set(true);
        zero.set(false);


        for (int i = sum.LONGWORD - 1; i >= 0; i--){

            int aa = toInt(a.getBit(i).getValue());
            int bb = toInt(b.getBit(i).getValue());
            int s = aa + bb;

            if(overflow == 0){ // in case overflow == 0
                if(s == 0 ) { // 0+0
                    sum.setBit(i, zero);
                }else if (s == 1 ){ //1+0 or 0+1
                    sum.setBit(i, one);
                }else if(s == 2){ // 1+1
                    sum.setBit(i, zero);
                    overflow++;
                }
            }else{ // overflow > 0
                if(s == 0){
                    sum.setBit(i,one);
                }else if(s == 1){
                    sum.setBit(i,zero);
                    overflow++;
                }else{
                    sum.setBit(i,one);
                    overflow++;
                }
                if(overflow > 0) overflow--;
            }
        }
        // return value
        return sum;
    }

    /**
     * Subtract function for Longword
     * @param a: input value
     * @param b: input value
     * @return a-b value
     */
    public static Longword subtract(Longword a, Longword b){

        Longword sub = new Longword();
        Longword tmp = new Longword();

        int aVal = a.getSigned();
        int bVal = b.getSigned();

        if(aVal > 0 && bVal < 0){ // ex: 5 - -3 = 5
            return add(a, twoComplement(b));
        }else if(aVal < 0 && bVal > 0){ // ex: -3 - 2 = -5
            return twoComplement(add(twoComplement(a), b));
        }

        // Setting bit value with true or false
        bit one = new bit();
        bit zero = new bit();
        one.set(true);
        zero.set(false);

        // binary subtract rule
        // 1010
        // 0011
        // 0101

        for(int i = sub.LONGWORD - 1; i >= 0; i--){
            int s = toInt(a.getBit(i).getValue()) - toInt(b.getBit(i).getValue());

            if(s < 0){ // 0-1
                // borrow a 1 from Previous value
                int flag = toInt(bring(i, a).getValue());

                // re-calculating sub with the changed Longword a
                s = toInt(a.getBit(i).getValue()) - toInt(b.getBit(i).getValue()) + flag;
            }

            // In case s >= 0
            if(s == 0) sub.setBit(i,zero);
            else sub.setBit(i,one);
        }

        //return value
        return  sub;
    }

    /**
     * Get 1 or 0 from true or false: helper function
     */
    public static int toInt(boolean value){
        if (value) return 1;
        else return 0;
    }

    /**
     * two's complement : helper function
     */
    public static Longword twoComplement(Longword a){
        return new Longword(a.not().getSigned() + 1);
    }

    /**
     * help function for bring 1 from previous
     */
    private static bit bring(int i, Longword a){

        // Setting bit value with true or false
        bit one = new bit();
        bit zero = new bit();
        one.set(true);
        zero.set(false);

        if(i == 0){ // 0 th
            a.setBit(i, one);
            return one;
        }else if (a.getBit(i - 1).getValue()){ // value 1 of i-1th
            a.setBit(i - 1, zero);
            a.setBit(i, one);
            return one;
        }else{ // value 0 of i-1
            a.setBit(i, bring(i - 1, a));
            return one;
        }
    }
}
