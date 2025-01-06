import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class theGreedyGnome_Exhaustive {
    private static int rows, cols;
    private static String[][] map;
    private static int earning;
    private static int maxGold=-99999, minStep=99999;
    private static List<Character> list = new ArrayList<>();
    static List<Character> finallist = new ArrayList<>();
    static Stack<Integer> intStack = new Stack<>();
    private static boolean back = false;

    public static void main(String[] args) throws FileNotFoundException {
        fileRead(args[0]);

        long startTime = System.currentTimeMillis();
        long startNano = System.nanoTime();

        printPaths(map,rows-1,cols-1,0,0,list,'A');
        resultPrint();

        long endNano = System.nanoTime();
        long timeElapsed = endNano - startNano;

        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime;
        System.out.printf("Running time: %d milli-seconds\n", diff);
        System.out.printf("Running time: %d ns\n", timeElapsed);
    }

    // Method to read the maze from the file
    private static int fileRead(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        rows = scan.nextInt();
        cols = scan.nextInt();
        map = new String[rows][cols];

        // To validate for the valid maze
        if (rows > 27 || cols > 27||rows <0 || cols <0){
            System.out.println("rows/cols is not valid!");
            return -1;
        }

        // scan through each character and store in 2D-array
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                map[i][j] = scan.next();
            }
        }
        scan.close();
        return 0;
    }

    // Method to find the path
    private static void printPaths(String map[][], int rows, int cols, int x, int y, List<Character> list, char Direction){
        // if reach out of map or reach 'x', return and back = true
        if (x > rows || y > cols || Objects.equals(map[x][y], "x") || Objects.equals(map[x][y], "X")) {
            intStack.push(0);
            back = true;
            return;
        }

        // if back == true, perform pop operation
        if (back){
            popOperation();
        }

        // else perform push operation
        pushOperation(map[x][y], Direction);

        // down
        printPaths(map,rows, cols, x+1, y,list,'D');
        popOperation();

        // right
        printPaths(map,rows, cols, x, y+1,list,'R');
        popOperation();

        list.remove(list.size()-1);
    }

    // method to pop integer from stack and minus that integer from earning
    private static void popOperation(){
        int tempSize = intStack.pop();
        earning -= tempSize;
        back = false;
    }

    // method to push integer to stack
    private static void pushOperation(String x, char Direction){
        // base on "Direction", add D or R to list
        char temp = 0;
        if (Direction=='R'){
            temp='R';
        } else if (Direction=='D'){
            temp='D';
        }
        list.add(temp);

        // if character is digit, convert to numeric value, then push to stack and update earning
        if (isNumeric(x)){
            int tempValue = Integer.parseInt(x);
            intStack.push(tempValue);
            earning += tempValue;
            int tempSize = list.size();
            // if current earning is max, add the steps to finallist, and update maxGold as current earning
            if ((earning > maxGold) || (earning == maxGold && tempSize < minStep)){
                maxGold = earning;
                minStep = list.size();
                finallist.clear();
                finallist.addAll(list);
            }
        } else {
            // in other cases, push number 0 to the stack
            intStack.push(0);
        }
    }

    // Method to print out final results
    private static void resultPrint(){
        for(int i=0; i<finallist.size(); i++){
            System.out.printf("%c",finallist.get(i));
        }
        System.out.println();
        System.out.printf("max gold is: %d \nsteps is: %d\n",maxGold, (minStep-1));
    }

    private static boolean isNumeric(String string) {
        int intValue;

        if(string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }
}
