import java.util.Arrays;
import java.util.Objects;

/**
 * building a functional CPU for Computer
 */
public class Computer {

    // Add a bit to indicate if the computer is halted or not
    private bit halt;
    // Add a memory member
    private Memory memory;
    // program counter
    private Longword PC;
    // stack pointer
    private Longword SP;
    // store variable of current instruction value
    private Longword currentInstruction;
    // register variable array 16
    private Longword[] register;
    // two variable for copying register data
    private Longword op1,op2;
    // result variable of operation
    private Longword result;

    int capacity;

    bit[] comp_flag;

    boolean jump_status = false;
    boolean branch_status = false;
    boolean compare_status = false;

    public static bit[] haltBit  = new bit[]{toBit(0),toBit(0),toBit(0),toBit(0)};
    public static bit[] moveBit  = new bit[]{toBit(0),toBit(0),toBit(0),toBit(1)};
    public static bit[] interruptBit  = new bit[]{toBit(0),toBit(0),toBit(1),toBit(0)};
    public static bit[] jumpBit  = new bit[]{toBit(0),toBit(0),toBit(1),toBit(1)};
    public static bit[] compareBit  = new bit[]{toBit(0),toBit(1),toBit(0),toBit(0)};
    public static bit[] branchBit  = new bit[]{toBit(0),toBit(1),toBit(0),toBit(1)};
    public static bit[] stackBit  = new bit[]{toBit(0),toBit(1),toBit(1),toBit(0)};

    public static bit[] beqBit  = new bit[]{toBit(1),toBit(1)};
    public static bit[] bgtBit  = new bit[]{toBit(1),toBit(0)};
    public static bit[] bneBit  = new bit[]{toBit(0),toBit(0)};
    public static bit[] bgeBit  = new bit[]{toBit(0),toBit(1)};

    // Constructor of Computer class
    public Computer(){

        halt = new bit();
        halt.set(true);
        memory = new Memory();

        PC = new Longword(0);
        SP = new Longword(1020);
        currentInstruction = new Longword();

        register = new Longword[16];
        for (int i = 0; i < register.length; i++)
            register[i] = new Longword();
        op1 = new Longword();
        op2 = new Longword();
        result = new Longword();

        capacity = memory.M_SIZE / memory.B_SIZE; // 1024

    }

    /**
     * runs a while loop until the halted the halt bit
     */
    public void run(){
        System.out.println("=== Running ===");
        while (halt.getValue()) {
            fetch();
            bit[] operator = decode();
            result = execute(operator);
            if(result == null) continue;
            store();
        }
    }

    /**
     * halt value
     */
    public boolean getHalt(){
        return halt.getValue();
    }

    /**
     * Fetching instruction with 16bits of read(PC)
     */
    void fetch(){
        System.out.println("Fetching from "+ PC.getSigned());
        currentInstruction = memory.read(PC).rightShift(16);
        PC = rippleAdder.add(PC,new Longword(2));
    }

    /**
     * Decoding operator
     */
    bit[] decode(){

        System.out.println("=== Decoded Operator ===");
        // 16th - 19th bits for operation
        bit[] opcode = new bit[]{
                currentInstruction.getBit(16),
                currentInstruction.getBit(17),
                currentInstruction.getBit(18),
                currentInstruction.getBit(19)
        };

        // first operation value: 9-12th bit
        op1.copy(register[currentInstruction.leftShift(20).rightShift(28).getSigned()]);
        // second operation value: 5-8th bit
        op2.copy(register[currentInstruction.leftShift(24).rightShift(28).getSigned()]);

        return opcode;
    }

    /**
     * Execute instructions with ALU
     */
    public Longword execute(bit[] operator){

        Longword retVal;
        // Halt : 0000
        if (ALU.compareBit(operator,haltBit)){
            System.out.println("=== halt ===");
            halt();
            return null;
        }
        // Move : 0001
        if (ALU.compareBit(operator,moveBit)){
            System.out.println("=== move ===");
            move();
            return null;
        }
        // Interrupt : 0010
        if (ALU.compareBit(operator,interruptBit)){
            System.out.println("=== interrupt ===");
            interrupt(currentInstruction.getBit(31));
            return null;
        }

        // Jump : 0011
        if (ALU.compareBit(operator,jumpBit)){
            System.out.println("=== jump ===");
            return jump();
        }

        // Compare : 0100
        if (ALU.compareBit(operator,compareBit)){
            System.out.println("=== compare ===");
            return compare();
        }

        // Branch : 0101
        if (ALU.compareBit(operator,branchBit)){
            System.out.println("=== branch ===");
            if (branch().getValue()){
                PC = rippleAdder.add(PC, currentInstruction.leftShift(20).rightShift(20));
                return currentInstruction.leftShift(20).rightShift(20);
            }
            return null;
        }
        //Stack : 0110
        if (ALU.compareBit(operator,stackBit)) {
            System.out.println("=== Stack ===");
            stack();
            return null;
        }

        retVal = ALU.opALU(operator, op1, op2);
        return retVal;
    }

    /**
     * Store result in current Instruction: last 4 bits
     */
    public void store(){

        //checking jump, compare, branch for result
        if (jump_status){
            PC.copy(result);
            jump_status = false;
            return;
        }else if(compare_status){
            compare_status = false;

            //BranchIfGreaterThan:10
            if(result.getSigned() > 0) comp_flag = bgtBit;

            //BranchIfNotEqual:00
            //b > a -> a != b
            else if(result.getSigned() < 0) comp_flag = bneBit;

            //BranchIfEqual:11
            //a == b
            else if(result.getSigned() == 0) comp_flag = beqBit;

            //BranchIfGreaterThanOrEqual:01 (a >= b)
            else comp_flag = bgeBit;

            return;

        }else if (branch_status){
            branch_status = false;
            PC = rippleAdder.add(PC,result);
            return;
        }
        // other result value
        System.out.println("=== Store result ===");
        // store result value in 0-3th bits
        register[currentInstruction.leftShift(28).rightShift(28).getSigned()] = result;
    }

    /**
     * Halt of computer: On / Off
     */
    private void halt(){
        halt.set(false);
        System.out.println("=== Halted ===");
    }

    /**
     * This function moves value of the instruction into the register identified
     * move R2 10 – moves the literal value 10 into R2.
     * The bits for this instruction would look like this:
     * 0001 0010 0000 1010 (4 for the opcode, 4 to indicate the register, 8 to indicate the value).
     */
    public void move(){
        // 24 th - 32 th: 8 bits
        bit[] anyValues = new bit[]{
                currentInstruction.getBit(24),
                currentInstruction.getBit(25),
                currentInstruction.getBit(26),
                currentInstruction.getBit(27),
                currentInstruction.getBit(28),
                currentInstruction.getBit(29),
                currentInstruction.getBit(30),
                currentInstruction.getBit(31),
        };

        Longword longword = new Longword();
        for (int i = 24; i < longword.LONGWORD; i++)
            longword.setBit(i, anyValues[i - 24]);

        //if value is -1, extend value for longword with the sign
        if (anyValues[0].getValue()) longword = longword.signExtended(24);

        register[currentInstruction.leftShift(20).rightShift(28).getSigned()] = longword;
    }

    /**
     * Interrupts displaying for the registers or all 1024 bits of memory
     */
    public void interrupt(bit b){
        System.out.println("=== Interrupted ===");
        // if b value is 0
        if (!b.getValue()){
            //displaying registers
            for(int i = 0; i < register.length; i++){
                System.out.println("Register (" + i + "):"+register[i]);
            }
        }else {
            // if b value is 1
            //displaying memory
            for (int i = 0; i < capacity; i += 4) {
                System.out.println("4Byte in Memory ("+i + "," + (i + 3) + "):" + memory.read(new Longword(i)));
            }
        }
    }

    /**
     * Jump to address of instruction
     * @return jump value
     */
    private Longword jump(){
        jump_status = true;
        System.out.println("Jumping from an address");
        //jump value
        return currentInstruction.leftShift(20).rightShift(20);
    }

    /**
     * compare Rx, Ry
     * @return compare
     */
    private Longword compare(){
        compare_status = true;
        Longword rx, ry;
        rx = register[currentInstruction.leftShift(24).rightShift(28).getSigned()];
        ry = register[currentInstruction.leftShift(28).rightShift(28).getSigned()];
        return rippleAdder.subtract(rx,ry);
    }

    /**
     * branch case
     * checking branch condition
     * BranchIfEqual,BranchIfNotEqual,BranchIfGreaterThan,BranchIfGreaterThanOrEqual
     */
    private bit branch(){
        branch_status = true;
        PC = rippleAdder.subtract(PC,new Longword(2));
        bit[] condition_code = new bit[]{currentInstruction.getBit(20), currentInstruction.getBit(21)};

        if(ALU.compareBit(comp_flag, condition_code))
            return toBit(1);
        //comp_flag == or > and instruction >=
        else if((ALU.compareBit(comp_flag,beqBit) || ALU.compareBit(comp_flag,bgtBit))
                && ALU.compareBit(condition_code,bgeBit))
            return toBit(1);

        // instruction > && comp_flag >=
        if(ALU.compareBit(condition_code,bgtBit) && ALU.compareBit(comp_flag,bgeBit))
            return toBit(1);
        //not equal and comp_flag bits not 11 (equal)
        else if(ALU.compareBit(condition_code,bneBit) && !ALU.compareBit(comp_flag, beqBit))
            return toBit(1);
        else{
            branch_status = false;
            PC = rippleAdder.add(PC, new Longword(2));
            return toBit(0); //branch condition is false
        }
    }

    /**
     * Function for stack
     * instructions: push,pop,call,return
     * push: 0110 0000 0000 RRRR // R = register bit
     * pop:0110 0100 0000 RRRR // R = register bit
     * call:0110 1000 AAAA AAAA // A = address bit
     * return:0110 1100 0000 0000 // No variable bits
     */
    private void stack(){

        bit[] flag_bit = new bit[]{currentInstruction.getBit(20),currentInstruction.getBit(21)};
        bit[] push_bit = new bit[]{toBit(0),toBit(0)};
        bit[] pop_bit = new bit[]{toBit(0),toBit(1)};
        bit[] call_bit =  new bit[]{toBit(1),toBit(0)};
        bit[] return_bit = new bit[]{toBit(1),toBit(1)};

        // push:00
        if(ALU.compareBit(flag_bit,push_bit)){
            System.out.println("--- push ---");
            Longword reg_val = register[currentInstruction.leftShift(28).rightShift(28).getSigned()];
            memory.write(SP, reg_val);
            Longword prev = rippleAdder.subtract(SP,new Longword(4));
            SP.copy(prev);

        }else if(ALU.compareBit(flag_bit,pop_bit)){

            System.out.println("--- pop ---");
            Longword next = rippleAdder.add(SP,new Longword(4));
            SP.copy(next);
            register[currentInstruction.leftShift(28).rightShift(28).getSigned()] = memory.read(SP);

        }else if(ALU.compareBit(flag_bit,call_bit)){
            System.out.println("--- call ---");
            memory.write(SP, PC);
            PC.copy(currentInstruction.leftShift(24).rightShift(24));
            Longword prev = rippleAdder.subtract(SP,new Longword(4));
            SP.copy(prev);

        }else if(ALU.compareBit(flag_bit,return_bit)){
            System.out.println("--- return ---");
            PC.copy(memory.read(SP));
            Longword next = rippleAdder.add(SP,new Longword(4));
            SP.copy(next);
        }
    }

    /**
     * Initialization
     * Function for preloads bits into memory starting at ith
     */
    public void preload(String[] bits, int ith){

        System.out.println("Instructions: " + Arrays.toString(bits));
        Longword longword = new Longword();
        int bi = 0;

        for (int i = 0; i < bits.length; i++) {
            for (char bit : bits[i].toCharArray()) {
                // 0 in ascii is 48, 1 in ascii is 49
                longword.setBit(bi, toBit(bit - 48));
                bi++;
            }

            // An instruction added
            if(bi == 16 && i != bits.length - 1){
                memory.write(new Longword(ith), longword);
                bi = 0;
                ith += 2;
                longword = new Longword();
            }
            // if ith is 1024, break loop
            if(ith == capacity) break;
        }
        memory.write(new Longword(ith), longword);
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
