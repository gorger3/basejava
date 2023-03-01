package com.urise.webapp.deadlock;

public class DeadLockExample {
    public static void main(String[] args) {
        Account acc1 = new Account(100);
        Account acc2 = new Account(100);
        Thread t1 = new Thread(() -> {
            synchronized (acc1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (acc2) {
                    acc1.transfer(acc1, acc2, 50);
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (acc2) {
                synchronized (acc1) {
                    acc2.transfer(acc2, acc1, 50);
                }
            }
        });
        t1.start();
        t2.start();
        System.out.println("acc1.Balance = " + acc1.getBalance() + ", acc2.Balance = " + acc2.getBalance());
        System.out.println("t1.State = " + t1.getState() + ", t2.State = " + t2.getState());
    }


}