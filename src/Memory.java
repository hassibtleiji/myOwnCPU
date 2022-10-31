/**
 * Memory class for managing 1024 byte
 */
public class Memory {

    // Member for memory management
    // 1024 BYTE = 8192 BIT
    // 1 BYTE = 8 BIT
    private bit[] memory;
    public final int M_SIZE = 8192;
    public final int B_SIZE = 8;

    /**
     * Constructor from bit class
     */
    public Memory(){
        memory = new bit[M_SIZE];
        for (int i = 0; i < M_SIZE; i++){
            bit tmp = new bit();
            tmp.set(false);
            memory[i] = tmp;
        }
    }

    /**
     * input a new Longword value after reads the bit value from memory[B_SIZE * address]
     */
    public Longword read(Longword address){
        Longword r_data = new Longword();
        int bit_index = 0;
        for (int i = address.getSigned() * B_SIZE; i < M_SIZE; i++){
            if (bit_index > r_data.LONGWORD - 1) break;
            r_data.setBit(bit_index, memory[i]);
            bit_index++;
        }

        return r_data;
    }

    /**
     * write memory values with starting address: B_SIZE * address
     */
    public void write(Longword address, Longword value){

        int bit_index = 0;
        for (int i = address.getSigned() * B_SIZE; i < M_SIZE ; i++){
            if (bit_index > value.LONGWORD - 1) break;
            memory[i] = value.getBit(bit_index);
            bit_index++;
        }
    }
}
