package busevent;

import org.greenrobot.eventbus.EventBus;

public class LoginBusEvent {
    public String getStrEvent() {
        return strEvent;
    }

    public void setStrEvent(String strEvent) {
        this.strEvent = strEvent;
    }

    private String strEvent;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;


    public LoginBusEvent(String strEvent,int active,String status) {
        this.strEvent = strEvent;
        this.active = active;
        this.status=status;

    }


    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    private int active;





}
