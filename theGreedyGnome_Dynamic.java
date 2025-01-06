import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class theGreedyGnome_Dynamic {
    private static int rows, cols;
    private static int[][] map;
    static Stack<Character> charStack = new Stack<>();

    public static void main(String[] args) throws FileNotFoundException {
        fileRead(args[0]);
        long startTime = System.currentTimeMillis();
        long startNano = System.nanoTime();

        findPath(map);

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
        map = new int[rows][cols];

        // To validate for the valid maze
        if (rows > 27 || cols > 27||rows <0 || cols <0){
            System.out.println("rows/cols is not valid!");
            return -1;
        }

        // scan through each character and store in 2D-array
        // and convert it into a integers
        // X -> -99
        // Dot (".") -> 0
        // else keeps the same values
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                String temp = scan.next();
                if (Objects.equals(temp, "x") || Objects.equals(temp, "X")){
                    map[i][j] = -99;
                } else if (Objects.equals(temp, ".")){
                    map[i][j] = 0;
                } else {
                    map[i][j]= Integer.valueOf(temp);
                }
            }
        }
        scan.close();
        return 0;
    }

    private static void findPath(int[][] map){
        // create a new 2D array has the same size to the original
        int[][] dp = new int[rows][cols];

        // x, y, max will be the position where the MAX earning is updated
        int x = 0, y = 0, max = 0;

        // calculate the earning of from [0][0] -> [rows][cols]
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dp[i][j] = map[i][j];

                // case 1: in the row 0
                if (i == 0 && j > 0) {
                    dp[0][j] += dp[0][j - 1];
                }
                // case 2: in the col 0
                else if (i > 0 && j == 0) {
                    dp[i][0] += dp[i - 1][0];
                }
                // case 3: for cols and rows > 0
                else if (i > 0 && j > 0) {
                    int temp = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    dp[i][j] = map[i][j] + temp;
                }

                // update the value and position of where the MAX occurs
                if (dp[i][j] > max) {
                    max = dp[i][j];
                    x = i;
                    y = j;
                }
            }
        }

        // assign the position of MAX to i and j
        int i = x, j = y;
        // backtrace from the MAX back to [0][0]
        // push the steps to stack
        // for more clear explanation, please refer to our report
        while (i != 0 || j != 0) {
            // case 1: reach [0][0] -> break loop
            if (i==0 && j==0){
                break;
            }
            // case 2: for the row 0 (first row)
            else if (i==0){
                charStack.push('R');
                j--;
            }
            // case 3: for the col 0 (first col)
            else if (j==0) {
                charStack.push('D');
                i--;
            }
            // case 4: for rows and cols > 0
            else if (dp[i][j] - map[i][j] == dp[i - 1][j]) {
                charStack.push('D');
                i--;
            } else if (dp[i][j] - map[i][j] == dp[i][j-1]){
                charStack.push('R');
                j--;
            }
        }

        // print steps from stack
        // whenever steps is printed, steps will increase
        int steps = 0;
        while (charStack.size() > 0) {
            char temp = charStack.pop();
            steps++;
            System.out.printf("%c", temp);
        }

        // print out MAX earning & steps
        System.out.printf("\nmax earning is: %d\nsteps is: %d\n",max,steps);
    }
}

