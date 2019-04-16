import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private int floor;
    private int targetFloor;
    private short status; //0:lazy 1:up 2:down
    private AllRequest allRequest;
    private ArrayList<People> guest;
    private boolean inputFinished;
    private char name;
    private int speed;
    private int capacity;
    private boolean specialTask;
    private ArrayList<Integer> reach;
    
    Elevator(AllRequest all, char nm, int sp, int cap, ArrayList<Integer> r) {
        floor = 1;
        targetFloor = 0;
        guest = new ArrayList<>();
        inputFinished = false;
        allRequest = all;
        status = 0;
        TimableOutput.initStartTimestamp();
        name = nm;
        speed = sp;
        capacity = cap;
        reach = r;
        specialTask = false;
    }
    
    void inputFinish() {
        inputFinished = true;
    }
    
    private void lift() {
        try {
            sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floor += 1;
        if (floor == 0) {
            floor += 1;
        }
        arrive();
    }
    
    private void lower() {
        try {
            sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floor -= 1;
        if (floor == 0) {
            floor -= 1;
        }
        arrive();
    }
    
    private void open() {
        TimableOutput.println("OPEN-" + floor + "-" + name);
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void close() {
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println("CLOSE-" + floor + "-" + name);
    }
    
    int getCapacity() {
        return capacity;
    }
    
    int getFloor() {
        return floor;
    }
    
    short getStatus() {
        return status;
    }
    
    char getElevatorName() {
        return name;
    }
    
    private boolean canStop(int fl, char nam) {
        if (nam == 'A') {
            return isInA(fl);
        } else if (nam == 'B') {
            return isInB(fl);
        } else {
            return isInC(fl);
        }
    }
    
    boolean isInA(int num) {
        return !(num > 1 && num < 16);
    }
    
    boolean isInB(int num) {
        return !(num == -3 || num == 3 || num > 15);
    }
    
    boolean isInC(int num) {
        return !(num % 2 != 1 || num > 15 || num < 1);
    }
    
    boolean canTake(int from, int to, char name) {
        if (name == 'A') {
            if (!isInA(from)) {
                return false;
            }
            return !(from == 1 && !isInA(to));
        } else if (name == 'B') {
            if (!isInB(from)) {
                return false;
            }
            if (from == 1 && isInA(to)) {
                return false;
            }
            return !(to == 3 && isInC(from));
        } else {
            if (!isInC(from)) {
                return false;
            }
            if (from == 1 && isInA(to)) {
                return false;
            }
            return (from == 3 || (from % 2 == 1 && to % 2 == 1));
        }
    }
    
    private void moveto(int floorNum) {
        if (floor == floorNum) {
            handlePeople();
            return;
        }
        targetFloor = floorNum;
        if (floorNum > floor) {
            status = 1;
            return;
        }
        status = 2;
    }
    
    private void arrive() {
        TimableOutput.println("ARRIVE-" + floor + "-" + name);
    }
    
    private void handlePeople() {
        open();
        for (int i = 0; i < guest.size(); i++) {
            if (guest.get(i).getToFloor() == floor) {
                TimableOutput.println("OUT-" +
                        guest.get(i).getPersonId() + "-" + floor + "-" + name);
                if (guest.get(i).isSpecial()) {
                    allRequest.addRequest(new ElevatorRequest(
                            floor, guest.get(i).getToNewFloor(),
                            guest.get(i).getPersonId()
                    ));
                }
                guest.remove(i);
                i--;
            }
        }
        allRequest.eatPeople(guest, this);
        close();
        if (status != 0) {
            if (floor == targetFloor) {
                status = 0;
            } else if (floor < targetFloor) {
                status = 1;
            } else {
                status = 2;
            }
        }
    }
    
    boolean getSpecialTask() {
        for (People p : guest
        ) {
            if (p.isSpecial()) {
                return true;
            }
        }
        return false;
    }
    
    public void run() {
        ArrayList<ElevatorRequest> list = allRequest.getList();
        while (!inputFinished || !list.isEmpty() ||
                !allRequest.isSpecialTaskFinished() || !guest.isEmpty()) {
            switch (status) {
                case 0: { //lazy
                    if (!guest.isEmpty()) {
                        moveto(guest.get(0).getToFloor());
                    } else {
                        boolean hasWork = false;
                        for (ElevatorRequest pr :
                                list) {
                            if (canTake(pr.getFromFloor(),
                                    pr.getToFloor(), name)) {
                                moveto(pr.getFromFloor());
                                hasWork = true;
                                break;
                            }
                        }
                        if (!hasWork) {
                            allRequest.touchFish();
                        }
                    }
                    break;
                }
                case 1:
                case 2: { //up or down
                    if (targetFloor == floor || (canStop(floor, name) &&
                            allRequest.movingCheck(guest, this))) {
                        handlePeople();
                    }
                    if (status == 1) {
                        lift();
                    } else if (status == 2) {
                        lower();
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            
        }
    }
}
