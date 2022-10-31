public class Longword {

    public final byte LONGWORD = 32;		//32 bit size
    private bit[] bit;

  
    //All bits set to 0
    public Longword() { 
    	bit = new bit[LONGWORD];
        for (int i = 0; i < LONGWORD; i++) {
        	bit[i] = new bit();
        }
    }

    //All bits copied from other longword to this one
    public Longword(Longword o) { 
    	bit = new bit[LONGWORD];
        copy(o);
    }

    //Constructor, with argument as signed integer
    public Longword(int val) { 
        this();
        set(val);
    }

    //Get bit i
    public bit getBit(int i) { 
        return bit[i];
    }

    //set bit i's value
    public void setBit(int i, bit value) { 
    	bit[i].set(value.getValue());
    }


    //AND two longwords, returning a third
    public Longword and(Longword o) { 
        Longword a = new Longword();
        for (int i = 0; i < LONGWORD; i++) {
            a.setBit(i, bit[i].and(o.getBit(i)));
        }
        return a;
    }
    
    //OR two longwords, returning a third
    public Longword or(Longword o) { 
        Longword a = new Longword();
        for (int i = 0; i < LONGWORD; i++) {
            a.setBit(i, bit[i].or(o.getBit(i)));
        }
        return a;
    }

    //XOR two longwords, returning a third
    public Longword xor(Longword o) { 
        Longword a = new Longword();
        for (int i = 0; i < LONGWORD; i++) {
            a.setBit(i, bit[i].xor(o.getBit(i)));
        }
        return a;
    }

    //Negate longword, create another one
    public Longword not() { 
        Longword a = new Longword();
        for (int i = 0; i < LONGWORD; i++) {
            a.setBit(i, bit[i].not());
        }
        return a;
    }

    //Rightshift longword by amount of bits, create and return a new longword
    Longword rightShift(int amount_bits) { 
        Longword a = new Longword();
        for (int i = amount_bits, j = 0; i < LONGWORD; i++, j++) {
            a.setBit(i, bit[j]);
        }
        return a;
    }

    //Leftshift  longword by amount of bits, create and return a new longword
    Longword leftShift(int amount_bits) { 
        Longword a = new Longword();
        for (int i = 0, j = amount_bits; j < LONGWORD; i++, j++) {
            a.setBit(i, bit[j]);
        }
        return a;
    }

    //Returns the longword as a string
    @Override
    public String toString() { 
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < bit.length; i++) {
            str.append(bit[i].getValue()?"1":"0");

            if (i != 0 && (i + 1) % 4 == 0)
                str.append(" ");
        }
        return str.toString();
    }

    //Returns the value of the longword as a 'long'
    public long getUnsigned() {

        long num = 0;
        long power = 1;
        for (int i = LONGWORD - 1; i >= 0; i--) {
            bit b = bit[i];
            num = num + (Long.parseLong(b + "") * power);
            power = power * 2;
        }

        return num;
    }

 //Returns value of this Longword as an integer.
    public int getSigned() { 
        int num = 0;
        int power = 1;

        if (bit[0].getValue()) {

            for (int i = LONGWORD - 1; i >= 1; i--) {
                int b = bit[i].getValue()?0:1;
                num = num + (Integer.parseInt(b + "") * power);
                power = power * 2;
            }
            num += 1;
            num *= -1;
        } else {
            for (int i = LONGWORD - 1; i >= 0; i--) {
                bit b = bit[i];
                num = num + (Integer.parseInt(b + "") * power);
                power = power * 2;
            }
        }
        return num;
    }
   
    //Copies the values of bits from one Longword to the other
    public void copy(Longword o) { 
        for (int i = 0; i < LONGWORD; i++) {
        	bit[i].set(o.bit[i].getValue());
        }
    }

  //Set the value of the bits of this long word
	void set(int value) {
        String str_bits = Long.toBinaryString(Integer.toUnsignedLong(value) | 0x100000000L).substring(1);
        for (int i = 0; i < LONGWORD; i++) {
            bit[i].set(str_bits.charAt(i) - 49 == 0); // char code 49 = 1, char code 48 = 0
        }
//		int k = bit.length - 1;
//		do {
//			if(value % 2 == 0)
//				bit[k].set(false);
//			else
//				bit[k].set(true);
//			k--;
//			value = value / 2;
//		} while(value != 0);
	}

    // extends sign from bit[index] to first bit in longword
    public Longword signExtended(int index){
        Longword extended = this;
        if(index > extended.LONGWORD)
            index = extended.LONGWORD;
        for (int i = 0; i < index; i++) {
            extended.setBit(i, bit[index]);
        }
        return extended;
    }
}