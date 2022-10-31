import java.util.ArrayList;
import java.util.Collections;
/**
 * Assembler Class
 * An assembler is a program that takes text as input and outputs bits that correspond to that text.
 * For example, move R1 -1 is much easier than writing 0001 0001 1111 1111
 */

public class Assembler {

    /**
     * assemble function:
     * return "0000 0000 0000 0000" format instruction from Assemble code
     * ex: move R1 -1 => 0001 0001 1111 1111
     * A series of strings, like “move R1 -1”
     * and it will return the bit patterns that are represented by that command.
     */

    public static String[] assemble(String[] assembleCode) throws Exception {

        ArrayList<String> instructionArr = new ArrayList<>();
        String bitInstruction;

        for (String line : assembleCode) {
            String[] commands = line.trim().split(" ");
            // halt, length 0
            if ("halt".equalsIgnoreCase(commands[0]) && commands.length == 1)
                bitInstruction = halt(commands);
            // move, length 3
            else if("move".equalsIgnoreCase(commands[0]) && commands.length == 3)
                bitInstruction = move(commands);
            // add, length 3
            else if("add".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = add(commands);
            // subtract, length 4
            else if("subtract".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = subtract(commands);
            // multiply, length 4
            else if("multiply".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = multiply(commands);
            // leftshift, length 4
            else if("leftshift".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = leftshift(commands);
            // rightshift, length 4
            else if("rightshift".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = rightshift(commands);
            // and, length 4
            else if("and".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = and(commands);
            // or, length 4
            else if("or".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = or(commands);
            // xor, length 4
            else if("xor".equalsIgnoreCase(commands[0]) && commands.length == 4)
                bitInstruction = xor(commands);
            // not, length 3
            else if("not".equalsIgnoreCase(commands[0]) && commands.length == 3)
                bitInstruction = not(commands);
            // interrupt, length 2
            else if("interrupt".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = interrupt(commands);
            // Jump
            else if("jump".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = jump(commands);
            // Compare
            else if("compare".equalsIgnoreCase(commands[0]) && commands.length == 3)
                bitInstruction = compare(commands);

            // Branch
            // BranchIfEqual ==
            else if("BranchIfEqual".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = branch(commands,1);
            // BranchIfGreaterThan >
            else if("BranchIfGreaterThan".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = branch(commands,2);
            // BranchIfGreaterThanOrEqual >=
            else if("BranchIfGreaterThanOrEqual".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = branch(commands,3);
            // BranchIfNotEqual !=
            else if("BranchIfNotEqual".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = branch(commands,4);

            // Push, Pop, Call, Return for Stack
            else if("push".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = stack(commands,"push");
            else if("pop".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = stack(commands,"pop");
            else if("call".equalsIgnoreCase(commands[0]) && commands.length == 2)
                bitInstruction = stack(commands,"call");
            else if("return".equalsIgnoreCase(commands[0]) && commands.length == 1)
                bitInstruction = stack(commands,"return");

            else throw new Exception("Invalid command: " + commands[0]);

            Collections.addAll(instructionArr, bitInstruction.split(" "));
        }
        return instructionArr.toArray(new String[0]);
    }

    /**
     * Function for making "halt" Instruction: 0000 0000 0000 0000
     */
    private static String halt(String[] instruction) throws Exception{
        return "0000 0000 0000 0000";
    }

    /**
     * Function for making "move" instruction code
     */
    private static String move(String[] commands) throws Exception{
        return "0001" + // instruction
                " " +
                registerToBit(commands[1]) + // register
                " " +
                formattedNumber(Integer.parseInt(commands[2]));
    }

    /**
     * Function for making "add" instruction code
     */
    private static String add(String[] commands) throws Exception{

        return "1110" + //add
               " " +
               registerToBit(commands[1]) + //register 1
               " " +
               registerToBit(commands[2]) + //register 2
               " " +
               registerToBit(commands[3]); //register 3 - destination
    }
    /**
     * Function for making "subtract" instruction code
     */
    private static String subtract(String[] commands) throws Exception{
        return "1111" + //subtract
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]) + //register 2
                " " +
                registerToBit(commands[3]); //register 3 - destination
    }
    /**
     * Function for making "multiply" instruction code
     */
    private static String multiply(String[] commands) throws Exception{
        return "0111" + //multiply
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]) + //register 2
                " " +
                registerToBit(commands[3]); //register 3 - destination
    }
    /**
     * Function for making "and" instruction code
     */
    private static String and(String[] commands) throws Exception{
        return "1000" + //and
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]) + //register 2
                " " +
                registerToBit(commands[3]); //register 3 - destination
    }
    /**
     * Function for making "or" instruction code
     */
    private static String or(String[] commands) throws Exception{
        return "1001" + //or
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]) + //register 2
                " " +
                registerToBit(commands[3]); //register 3 - destination
    }
    /**
     * Function for making "xor" instruction code
     */
    private static String xor(String[] commands) throws Exception{
        return "1010" + //xor
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]) + //register 2
                " " +
                registerToBit(commands[3]); //register 3 - destination
    }
    /**
     * Function for making "not" instruction code
     */
    private static String not(String[] commands) throws Exception{
        return "1011" + //not
                " " +
                registerToBit(commands[1]) + //register 1
                " 0000" + // 0000 : ignore
                " " +
                registerToBit(commands[2]); //register 2 - destination
    }

    /**
     * Function for making "leftshift" instruction code
     */
    private static String leftshift(String[] commands) throws Exception{
        return "1100" + //leftshift
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]) + //register 2
                " " +
                registerToBit(commands[3]); //register 3 - destination
    }

    /**
     * Function for making "rightshift" instruction code
     */
    private static String rightshift(String[] commands) throws Exception{
        return "1101" + //rightshift
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]) + //register 2
                " " +
                registerToBit(commands[3]); //register 3 - destination
    }

    /**
     * Function for making "interrupt" instruction code
     */
    private static String interrupt(String[] commands) throws Exception{
        return "0010" + //interrupt
                " " +
                "0000 "+
                formattedNumber(Integer.parseInt(commands[1])); // interrupt 0 / 1
    }

    /**
     * Function for making "jump" instruction code
     * jump 10
     * 0011 0000 0000 1010
     */
    private static String jump(String[] commands) throws Exception{
        return "0011" + //jump opcode 3:0011
                " " +
                "0000 "+
                formattedNumber(Integer.parseInt(commands[1])); // jump an address
    }

    /**
     * Function for making "compare" instruction code
     * 0100
     * compare R1 R2
     */
    private static String compare(String[] commands) throws Exception{

        return "0100" + //compare opcode:0100
                " " +
                "0000" +
                " " +
                registerToBit(commands[1]) + //register 1
                " " +
                registerToBit(commands[2]); //register 2
    }

    /**
     * Function for making "Branch" instruction code
     */
    private static String branch(String[] commands, int res) throws Exception{

        String branch_code = switch (res) {
            case 1 -> //BRANCH IF EQUAL
                    "11";
            case 2 -> //BRANCH IF GREATER THAN
                    "10";
            case 3 -> //BRANCH IF GREATER THAN OR EQUAL
                    "01";
            default -> //BRANCH IF NOT EQUAL
                    "00";
        };
        return "0101" + //opcode
                " " +
                branch_code + "00"+ // sign code:0, address code:0
                " "+
                formattedNumber(Integer.parseInt(commands[1])); // address code 8 bit
    }

    /**
     * Function for Stack assembler constructing
     * push: 0110 0000 0000 RRRR // R = register bit
     * pop:0110 0100 0000 RRRR // R = register bit
     * call:0110 10AA AAAA AAAA // A = address bit
     * return:0110 1100 0000 0000 // No variable bits
     */
    private static String stack(String[] commands, String type) throws Exception{

        String ret_str = "0110"+" ";
        String type_str = "";

        switch (type) {
            case "push" -> { //push
                type_str += "0000 0000 " + registerToBit(commands[1]);
            }
            case "pop" -> { //pop
                type_str += "0100 0000 " + registerToBit(commands[1]);
            }
            case "call" -> { //call
                type_str += "1000 " + formattedNumber(Integer.parseInt(commands[1]));
            }
            default -> //return
                type_str = "1100 0000 0000";
        }
        ret_str += type_str;
        return ret_str;
    }

    /**
     * Helper methods for converting a register name (like R1) to a bit pattern (0001)
     */
    private static String registerToBit(String registerName) throws Exception{
        return switch (registerName.toUpperCase()) {
            case "R0" -> "0000";
            case "R1" -> "0001";
            case "R2" -> "0010";
            case "R3" -> "0011";
            case "R4" -> "0100";
            case "R5" -> "0101";
            case "R6" -> "0110";
            case "R7" -> "0111";
            case "R8" -> "1000";
            case "R9" -> "1001";
            case "R10" -> "1010";
            case "R11" -> "1011";
            case "R12" -> "1100";
            case "R13" -> "1101";
            case "R14" -> "1110";
            case "R15" -> "1111";
            default -> throw new Exception("Invalid a register:" + registerName);
        };
    }

    /**
     * Function for return string of formatted 0000 0000 with number(int)
     */
     private static String formattedNumber(int number) throws Exception{

         StringBuilder str  = new StringBuilder(Integer.toBinaryString(number));

         int numberLen = 8;
         int spaceLen = 4;
         String all_zeros = "00000000";

         if(number < -128 || number > 127)
            throw new Exception("Invalid number:" + number + ", its range is -128 ~ 127");

         // remove 1 of str.length()-8 in binary string for making length 8
         if (number < 0)
             str.delete(0, str.length() - numberLen);

         // add 0 of str.length()-8 in binary string for making length 8
         if(str.length() < numberLen){

             String add_zeros = all_zeros.substring(0, numberLen - str.length());
             str.insert(0, add_zeros);
         }

         // split with " "
         str.insert(spaceLen," ");

        return str.toString();
    }
}
