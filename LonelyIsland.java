import java.util.*;
import java.io.*;
import java.time.*;

class Island {
    int index;
    
    // Island constructor
    public Island(int x) {
        index = x;
    }
}

class Bridge {
    Island start, finish;

    // Bridge constructor
    public Bridge(Island A, Island B) {
        start = A;
        finish = B;
    }
}

class ArrIsland {
    Island[] array;
    int length;

    // ArrIsland constructor
    public ArrIsland(int size) {
        array = new Island[size];
        length = 0;
    }

    // adds an Island to the array
    public void add(Island X) {
        if (!exists(X)) {
            array[length] = X;
            length++;
        }
    }

    // copies an ArrIsland
    public ArrIsland copy() {
        ArrIsland temp = new ArrIsland(array.length);
        temp.length = length;
        for (int i = 0; i < length; i++) {
            temp.array[i] = array[i];
        }
        return temp;
    }

    // checks if Island X already exists in ArrIsland
    public Boolean exists(Island X) {
        Boolean Found = false;
        for (int i = 0; i < length; i++) {
            if (array[i].index == X.index) {
                Found = true;
            }
        }
        return Found;
    }

    // for debugging: prints ArrIsland in array format [1, 2, ..., n]
    public void print() {
        if (length > 0) {
            System.out.printf("[%d", array[0].index);
            for(int i = 1; i < length; i++) {
                System.out.printf(", %d", array[i].index);
            }
            System.out.println("]");
        } else {
            System.out.println("Empty array");
        }
    }

    // prints ArrIsland in path format 1 -> 2 -> ... -> n
    public void printAsPath() {
        if (length > 0) {
            System.out.printf("%d", array[0].index);
            for(int i = 1; i < length; i++) {
                System.out.printf(" -> %d", array[i].index);
            }
            System.out.println();
        } else {
            System.out.println("Empty array");
        }
    }

}

class ArrBridge {
    Bridge[] array;
    int length;

    // constructor
    public ArrBridge(int size) {
        array = new Bridge[size];
        length = 0;
    }

    // adds Bridge X to ArrBridge
    public void add(Bridge X) {
        array[length] = X;
        length++;
    }

    // finds all islands the player can move to from current position
    public ArrIsland FindIslands(Island x, ArrIsland traveled, int max_size) {
        ArrIsland result = new ArrIsland(max_size);
        for (int i = 0; i < length; i++) {
            if (array[i].start.index == x.index) {
                if (!traveled.exists(array[i].finish)) {
                    result.add(array[i].finish);
                }
            }
        }
        return result;
    }

    // for debugging: prints ArrBridge in array format [(1,2), (1,3), ..., (x,y)]
    public void print() {
        if (length > 0) {
            System.out.printf("[(%d,%d)", array[0].start.index, array[0].finish.index);
            for(int i = 1; i < length; i++) {
                System.out.printf(", (%d,%d)", array[i].start.index, array[i].finish.index);
            }
            System.out.println("]");
        } else {
            System.out.println("Empty array");
        }
    }
}

public class LonelyIsland {
    // declaring global variables
    static int num_islands, num_bridges;
    static Island origin;
    static ArrIsland islands;
    static ArrBridge bridges;
    static String source;
    static ArrIsland result;
    static double timeElapsed;

    // recursive method (displaying all possible paths and saving solutions)
    public static void Travel(ArrIsland traveled, Island location) {
        // adding current location to traveled array
        ArrIsland newTraveled = traveled.copy();
        newTraveled.add(location);
        // looking for islands to move to
        ArrIsland tempArr = bridges.FindIslands(location, traveled, num_islands);
        if (tempArr.length == 0) {
            // if there are no islands to move to, print path and save solution
            result.add(location);
            newTraveled.printAsPath();
        } else {
            // else, for every island to move to, move to island, add movement count, and repeat method
            for (int i = 0; i < tempArr.length; i++) {
                Travel(newTraveled, tempArr.array[i]);
            }
        }
    }

    // mapping method (reading input from file)
    public static void Map() {
        try {
            File file = new File(source);
            Scanner filex = new Scanner(file);

            // reading n, m, and r
            num_islands = filex.nextInt();
            num_bridges = filex.nextInt();
            origin = new Island(filex.nextInt());

            // initiating arrays
            islands = new ArrIsland(num_islands);
            bridges = new ArrBridge(num_bridges);
            result = new ArrIsland(num_islands);
            
            // reading islands and bridges from file
            for (int i = 0; i < num_bridges; i++) {
                Island A = new Island(filex.nextInt());
                islands.add(A);
                Island B = new Island(filex.nextInt());
                islands.add(B);
                Bridge newBridge = new Bridge(A, B);
                bridges.add(newBridge);
            }

            filex.close();
        
        } catch (FileNotFoundException x) {
            System.out.println("FileNotFoundException");
        }
    }

    // printing solution array
    public static void PrintSolution() {
        System.out.println();
        System.out.println("===============================================");
        System.out.printf("Pemain bisa terjebak di pulau: %d", result.array[0].index);
        for (int i = 1; i < result.length; i++) {
            System.out.printf(", %d", result.array[i].index);
        }
        System.out.println();
        System.out.println("===============================================");
        System.out.println();
        System.out.printf("Executed in %f miliseconds\n", timeElapsed);
    }

    // main program
    public static void main(String[] args) {
        Instant start = Instant.now();

        source = args[0];
        Map();
        ArrIsland traveled = new ArrIsland(num_islands);
        System.out.println();
        Travel(traveled, origin);

        Instant finish = Instant.now();
        
        timeElapsed = Duration.between(start, finish).toMillis();
        PrintSolution();
    }
}