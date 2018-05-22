package AgentModule;

public class DecipheringStatus {
    private boolean isPaused = false;
    private boolean isStopped = false;

    public synchronized boolean checkIfToContinue()
    {
        if(this.isStopped)
            return false;
        while(this.isPaused)
        {
            try{
                this.wait();
                if(this.isStopped)
                    return false;
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public synchronized void pauseDeciphering()
    {
        this.isPaused = true;
    }

    public  synchronized void continueDeciphering()
    {
        this.isPaused = false;
        this.notifyAll();
    }


    public void stopDeciphering() {
        this.isStopped = true;
    }
}
