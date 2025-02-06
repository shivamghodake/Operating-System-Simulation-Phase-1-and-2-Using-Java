public class App {
    public static void main(String[] args) throws Exception {
        Load load = new Load();
        try{
            load.LOAD("input.txt", "output2.txt");
            load.printMemory();
            load.printPageTable(load.PTR);

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}