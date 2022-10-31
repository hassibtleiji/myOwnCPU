import java.util.Arrays;

/**
 * Assembler test class
 */
public class Assembler_test {
    public static void main(String[] args) throws Exception {

        System.out.println("*** Assembler Testing ***");
        assemblerTest();
        System.out.println("*** Completed Testing ***");
    }

    public static void assemblerTest() throws Exception {

        System.out.println("HALT");
        System.out.println("MOVE R1 -1");
        System.out.println(Arrays.toString(Assembler.assemble(new String[]{"HALT"})));
        System.out.println(Arrays.toString(Assembler.assemble(new String[]{"MOVE R1 -1"})));

        System.out.println("ADD R0 R3 R3");
        System.out.println("SUBTRACT R3 R1 R4");
        System.out.println("MULTIPLY R3 R5 R7");
        System.out.println(Arrays.toString(Assembler.assemble(
                new String[]{
                        "ADD R0 R3 R3",
                        "SUBTRACT R3 R1 R4",
                        "MULTIPLY R3 R5 R7"})));

        System.out.println("LEFTSHIFT R1 R0 R15");
        System.out.println("RIGHTSHIFT R1 R0 R14");
        System.out.println(Arrays.toString(Assembler.assemble(
                new String[]{
                        "LEFTSHIFT R1 R0 R15",
                        "RIGHTSHIFT R1 R0 R14"})));

        System.out.println("NOT R1 R0");
        System.out.println("XOR R1 R0 R3");
        System.out.println(Arrays.toString(Assembler.assemble(
                new String[]{
                        "NOT R1 R0",
                        "XOR R1 R0 R3"})));
        System.out.println("INTERRUPT 0");
        System.out.println(Arrays.toString(Assembler.assemble(new String[]{"INTERRUPT 0"})));
    }
}
