import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.Arrays;

public class MainFunction {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        AllRequest all = new AllRequest();
        ArrayList<Integer> reachA = new ArrayList<>(Arrays.asList(
                -3, -2, -1, 1, 15, 16, 17, 18, 19, 20));
        ArrayList<Integer> reachB = new ArrayList<>(Arrays.asList(
                -2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        ArrayList<Integer> reachC = new ArrayList<>(Arrays.asList(
                1, 3, 5, 7, 9, 11, 13, 15));
        Elevator elevator1 = new Elevator(all, 'A', 400, 6, reachA);
        Elevator elevator2 = new Elevator(all, 'B', 500, 8, reachB);
        Elevator elevator3 = new Elevator(all, 'C', 600, 7, reachC);
        elevator1.start();
        elevator2.start();
        elevator3.start();
        Elevator[] elevators = {elevator1, elevator2, elevator3};
        InputHandler inputHandler = new InputHandler(all, elevators);
        inputHandler.start();
        all.setElevators(elevators);
    }
}
