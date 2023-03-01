package com.urise.webapp.deadlock;

class Account {
    private double balance;

    Account(double balance) {
        this.balance = balance;
    }

    double getBalance() {
        return balance;
    }

    void deposit(double amount) {
        checkAmountNonNegative(amount);
        balance += amount;
    }

    void withdraw(double amount) {
        checkAmountNonNegative(amount);
        if (balance < amount) {
            throw new IllegalArgumentException("not enough money");
        }
        balance -= amount;
    }

    void transfer(Account from, Account to, double amount) {
        from.withdraw(amount);
        to.deposit(amount);
    }

    private void checkAmountNonNegative(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount shouldn't be below zero");
        }
    }
}
