/**
 * CPU testing 3 Class
 */
public class cpu_test3 {

    public static void main(String[] args) throws Exception {
        System.out.println("*** CPU Testing 3 ***");
        runTests();
        System.out.println("*********************");
        System.out.println("CPU Testing Finished");
    }
    /**
     * Function for testing cases
     */
    public static void runTests() throws Exception {

        Computer computer = new Computer();
        String[] assembledCode = Assembler.assemble(new String[]{
                "move R1 3",//0
                "move R2 4",
                "push R1",
                "push R2",
                "call 14",//8
                "interrupt 0",//10
                "halt",//12
                "pop R15",//14
                "pop R2",
                "pop R1",
                "add R1 R2 R3",
                "push R3",
                "push R15",
                "return"//28
        });
        computer.preload(assembledCode,0);
        computer.run();

    }
}
