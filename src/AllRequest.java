import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

class AllRequest {
    private ArrayList<ElevatorRequest> list;
    private boolean full = true;
    private Elevator[] elevators;
    
    AllRequest() {
        list = new ArrayList<>();
    }
    
    synchronized void addRequest(PersonRequest request) {
        while (true) {
            if (full) {
                lock();
                list.add(new ElevatorRequest(request.getFromFloor(),
                        request.getToFloor(), request.getPersonId()));
                unlock();
                break;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    synchronized void addRequest(ElevatorRequest elevatorRequest) {
        while (true) {
            if (full) {
                lock();
                list.add(elevatorRequest);
                unlock();
                break;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    synchronized ArrayList<ElevatorRequest> getList() {
        while (true) {
            if (full) {
                return list;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    synchronized boolean movingCheck(ArrayList<People> guest, Elevator el) {
        int floor = el.getFloor();
        int status = el.getStatus();
        int capacity = el.getCapacity();
        char name = el.getElevatorName();
        while (true) {
            if (full) {
                lock();
                if (!guest.isEmpty()) {
                    for (People p :
                            guest) {
                        if (p.getToFloor() == floor) {
                            unlock();
                            return true;
                        }
                    }
                }
                if (!list.isEmpty()) {
                    for (ElevatorRequest pr :
                            list) {
                        if (guest.size() == capacity) {
                            unlock();
                            return false;
                        }
                        if (pr.getFromFloor() != floor) {
                            continue;
                        }
                        if (pr.getFromFloor() < pr.getToFloor()
                                && status == 1) {
                            if (!el.canTake(pr.getFromFloor(),
                                    pr.getToFloor(), name)) {
                                continue;
                            }
                            unlock();
                            return true;
                        }
                        if (pr.getFromFloor() > pr.getToFloor()
                                && status == 2) {
                            if (!el.canTake(pr.getFromFloor(),
                                    pr.getToFloor(), name)) {
                                continue;
                            }
                            unlock();
                            return true;
                        }
                    }
                }
                unlock();
                return false;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    synchronized void eatPeople(ArrayList<People> guest, Elevator el) {
        int floor = el.getFloor();
        int capacity = el.getCapacity();
        char name = el.getElevatorName();
        while (true) {
            if (full) {
                lock();
                for (int i = 0; i < list.size(); i++) {
                    if (guest.size() < capacity &&
                            list.get(i).getFromFloor() == floor &&
                            el.canTake(floor, list.get(i).getToFloor(), name)) {
                        guest.add(new People(list.get(i).getToFloor(),
                                list.get(i).getPersonId(), el));
                        TimableOutput.println("IN-" + list.get(i).getPersonId()
                                + "-" + floor + "-" + name);
                        list.remove(i);
                        i--;
                    }
                }
                unlock();
                break;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private synchronized void lock() {
        full = false;
    }
    
    private synchronized void unlock() {
        full = true;
        notifyAll();
    }
    
    synchronized void touchFish() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    void setElevators(Elevator[] els) {
        elevators = els;
    }
    
    boolean isSpecialTaskFinished() {
        for (Elevator el :
                elevators) {
            if (el.getSpecialTask()) {
                return false;
            }
        }
        return true;
    }
}
