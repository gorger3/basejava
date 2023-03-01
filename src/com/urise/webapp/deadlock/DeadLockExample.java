package com.urise.webapp.deadlock;

public class DeadLockExample {
    public static void main(String[] args) {
        Account acc1 = new Account(100);
        Account acc2 = new Account(100);
        Thread t1 = new Thread(() -> synchronizedTransfer(acc1, acc2, 50, 500));
        Thread t2 = new Thread(() -> synchronizedTransfer(acc2, acc1, 50, 1));
        t1.start();
        t2.start();
        System.out.println("acc1.Balance = " + acc1.getBalance() + ", acc2.Balance = " + acc2.getBalance());
        System.out.println("t1.State = " + t1.getState() + ", t2.State = " + t2.getState());
    }

    private static void synchronizedTransfer(Account acc1, Account acc2, double amount, long millis) {
        synchronized (acc1) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (acc2) {
                acc1.transfer(acc1, acc2, amount);
            }
        }
    }


}