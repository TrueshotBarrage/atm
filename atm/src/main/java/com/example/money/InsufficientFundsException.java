package com.example.money;

class InsufficientFundsException extends RuntimeException {

    InsufficientFundsException(double balance, double withdrawAmount) {
        super("Withdraw amount of " + String.format("%.02f", withdrawAmount) + " exceeds available balance of "
                + String.format("%.02f", balance));
    }
}