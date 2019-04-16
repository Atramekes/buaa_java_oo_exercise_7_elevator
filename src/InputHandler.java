import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;

class InputHandler extends Thread {
    private AllRequest allRequest;
    private Elevator[] elevators;
    
    InputHandler(AllRequest all, Elevator[] el) {
        allRequest = all;
        elevators = el;
    }
    
    public synchronized void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                try {
                    elevatorInput.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                for (Elevator el :
                        elevators) {
                    el.inputFinish();
                }
                return;
            }
            allRequest.addRequest(request);
        }
    }
}
