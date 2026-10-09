
package com.gdb.domain;

public class FixedDepositAccount extends Account {

    private int tenureMonths;
    private double interestRate;

    public FixedDepositAccount(String accountNumber, String name, int age,
                               double balance, String status, String pin,
                               int tenureMonths, double interestRate) {
        super(accountNumber, name, age, balance, "FIXED_DEPOSIT", status, pin);
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    public double calculateMaturityAmount() {
        double years = tenureMonths / 12.0;
        return getBalance() * Math.pow(1 + interestRate / 100.0, years);
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
