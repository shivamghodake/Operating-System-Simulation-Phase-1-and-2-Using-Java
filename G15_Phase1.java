import java.io.*;
import java.util.*;

public class G15_Phase1 {

    private static char[][] memory = new char[100][4];

    private static char[] IR = new char[4];
    private static char[] R = new char[4];
    private static int IC, SI;
    private static boolean toggle;
    private static BufferedReader input;
    private static BufferedWriter output;

    private static void INIT() {
        for (int i = 0; i < 100; i++) {
            Arrays.fill(memory[i], ' ');
        }
        Arrays.fill(IR, ' ');
        Arrays.fill(R, ' ');
        IC = 0;
        toggle = false;
        SI = 0;
    }

    private static void MOS() throws IOException {
        switch (SI) {
            case 1 -> READ();
            case 2 -> WRITE();
            case 3 -> TERMINATE();
        }
        SI = 0;
    }

    private static void READ() throws IOException {
        String buffer = input.readLine();
        if (buffer != null) {
            int k = 0;
            int add = (IR[2] - '0') * 10 + (IR[3] - '0');
            for (int i = 0; i < buffer.length() && i < 40; i++) {
                memory[add][k++] = buffer.charAt(i);
                if (k == 4) {
                    k = 0;
                    add++;
                }
            }
        }
    }

    private static void WRITE() throws IOException {
        int add = (IR[2] - '0') * 10 + (IR[3] - '0');
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                out.append(memory[add][j]);
            }
            add++;
        }
        output.write(out.toString() + "\n");
    }

    private static void TERMINATE() throws IOException {
        output.write("\n\n");
    }

    private static void EXECUTEUSERPROGRAM() throws IOException {
        int I = 0;
        while (true) {
            for (int i = 0; i < 4; i++) {
                IR[i] = memory[I][i];
            }
            I++;
            if (IR[0] == 'G' && IR[1] == 'D') {
                SI = 1;
                MOS();
            } else if (IR[0] == 'P' && IR[1] == 'D') {
                SI = 2;
                MOS();
            } else if (IR[0] == 'L' && IR[1] == 'R') {
                int add = (IR[2] - '0') * 10 + (IR[3] - '0');
                System.arraycopy(memory[add], 0, R, 0, 4);
            } else if (IR[0] == 'S' && IR[1] == 'R') {
                int add = (IR[2] - '0') * 10 + (IR[3] - '0');
                System.arraycopy(R, 0, memory[add], 0, 4);
            } else if (IR[0] == 'C' && IR[1] == 'R') {
                boolean flag = false;
                int add = (IR[2] - '0') * 10 + (IR[3] - '0');
                for (int i = 0; i < 4; i++) {
                    if (R[i] != memory[add][i]) {
                        flag = true;
                        break;
                    }
                }
                toggle = !flag;
            } else if (IR[0] == 'B' && IR[1] == 'T') {
                int add = (IR[2] - '0') * 10 + (IR[3] - '0');
                if (toggle) {
                    I = add;
                }
            } else if (IR[0] == 'H') {
                SI = 3;
                MOS();
                break;
            }
        }
    }

    private static void LOAD() throws IOException {
        String buffer;
        int block = 0;

        while ((buffer = input.readLine()) != null) {
            buffer = buffer.trim();

            if (buffer.startsWith("$AMJ")) {
                block = 0;
                INIT();
            } else if (buffer.startsWith("$DTA")) {
                EXECUTEUSERPROGRAM();
            } else if (buffer.startsWith("$END")) {
            } else {
                if (block >= 100) {
                    System.out.println("Out of memory.");
                } else {
                    int k = 0;
                    for (int i = block; i < block + 10 && k < buffer.length(); i++) {
                        for (int j = 0; j < 4; j++) {
                            memory[i][j] = (k < buffer.length()) ? buffer.charAt(k++) : ' ';
                        }
                    }
                    block += 10;
                }
            }
        }
    }

    private static void PRINTMEMORY() {
        System.out.println("Memory:");
        for (int i = 0; i < 100; i++) {
            System.out.print("M[" + i + "]:");
            for (int j = 0; j < 4; j++) {
                System.out.print(memory[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            input = new BufferedReader(new FileReader("input.txt"));
            output = new BufferedWriter(new FileWriter("output.txt"));
            LOAD();
            PRINTMEMORY();
        } catch (IOException e) {
            System.out.println("Problem in opening file");
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
            } catch (IOException e) {
                System.out.println("Problem in closing file");
                e.printStackTrace();
            }
        }
    }
}