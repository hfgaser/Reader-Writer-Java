import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class project {
    public static void main(String [] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ReadWriteLock RW = new ReadWriteLock();


        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));

        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));


    }
}


class ReadWriteLock {
    private Semaphore mutex = new Semaphore(1);
    private Semaphore writeLock = new Semaphore(1);
    private int readersCount = 0;

    public void readLock() {
        try {
            mutex.acquire();
            readersCount++;
            if (readersCount == 1) {
                writeLock.acquire();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeLock() {
        try {
            writeLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void readUnlock() {
        try {
            mutex.acquire();
            readersCount--;
            if (readersCount == 0) {
                writeLock.release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeUnlock() {
        writeLock.release();
    }
}



class Writer implements Runnable
{
    private ReadWriteLock RW_lock;


    public Writer(ReadWriteLock rw) {
        RW_lock = rw;
    }

    public void run() {
        while (true){
            RW_lock.writeLock();
            RW_lock.writeUnlock();
        }
    }


}



class Reader implements Runnable
{
    private ReadWriteLock RW_lock;
    public Reader(ReadWriteLock rw) {
        RW_lock = rw;
    }
    public void run() {
        while (true){
            RW_lock.readLock();
            RW_lock.readUnlock();

        }
    }


}