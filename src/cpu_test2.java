/**
 * cpu_test2 class for a simple program with assembler
 */
public class cpu_test2 {
    public static void main(String[] args) throws Exception {
        System.out.println("******* Start CPU testing 2 *******");
        runTest();
        System.out.println("*********************");
        System.out.println("Finished CPU testing!");
    }
    static void runTest() throws Exception {
        Computer computer = new Computer();
        String[] assembledCode = Assembler.assemble(new String[]{
                "jump 4",
                "move R1 5",
                "interrupt 0",
                "halt"
        });

        computer.preload(assembledCode,0);
        computer.run();
        System.out.println("********* First Program Passed ************");
        computer = new Computer();
        assembledCode = Assembler.assemble(new String[]{
                "move R0 2",
                "move R1 3",
                "compare R0 R1",
                "move R15 4",
                "BranchIfNotEqual 5",
                "BranchIfGreaterThan -5",
                "BranchIfEqual 5",
                "move R15 2",
                "jump 24",
                "move R15 6",
                "interrupt 0",
                "halt",
        });
        computer.preload(assembledCode,0);
        computer.run();
        System.out.println("********* Second Program Passed ************");

    }
}
