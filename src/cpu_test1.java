
public class cpu_test1 {
    public static void main(String[] args) {
        System.out.println("******* Start CPU testing 1 *******");
        runTest();
        System.out.println("*********************");
        System.out.println("Finished CPU testing!");
    }

    static void runTest(){

        Computer computer = new Computer();
        computer.preload(new String[]{"0000","0000","0000","0000"},0);
        computer.run();

        System.out.println("*** Testing Halt ***");
        if(computer.getHalt())
            System.out.println("Halt Failed");
        else
            System.out.println("Halt success!");
        System.out.println();

        //move
        System.out.println("*** Testing move ***");
        System.out.println("move R2 10");
        computer = new Computer();
        computer.preload(new String[]{"0001","0010","0000","1001"},0);
        computer.fetch();
        computer.execute(computer.decode());
        System.out.println("Move success!");
        System.out.println();

        // interrupt
        System.out.println("*** Testing interrupt 0 ***");
        System.out.println("Print all of the registers");
        computer.preload(new String[]{"0010","0000","0000","0000"},2);
        computer.fetch();
        computer.execute(computer.decode());
        System.out.println("Interrupt success!");
        System.out.println();

        // program
        System.out.println("*** Testing program1 ***");
        System.out.println("move R1 12");
        System.out.println("move R2 30");
        System.out.println("add R1 R2 R3");
        computer = new Computer();
        computer.preload(new String[]{
                "0001","0001","0000","1100",
                "0001","0010","0001","1110",
                "1110","0001","0010","0011",
                "0010","0000","0000","0000"},0);
        computer.run();
        System.out.println("Program1 success!");
        System.out.println();

        System.out.println("*** Testing Program2 ***");
        System.out.println("move R1 12");
        System.out.println("move R2 -8");
        System.out.println("add R1 R2 R8");
        computer = new Computer();
        computer.preload(new String[]{
                "0001","0001","0000","1100",
                "0001","0010","1111","1000",
                "1110","0001","0010","1000",
                "0010","0000","0000","0000",
                "0010","0000","0000","0001"},0);
        computer.run();
        System.out.println("Program2 success!");

    }
}
