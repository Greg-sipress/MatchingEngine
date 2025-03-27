package org.example.matchingengine.model;

public class PortfolioItem {
    private String symbol;        // Ticker symbol
    private int quantity;         // Quantity held
    private double purchasePrice; // Purchase price per unit
    private double currentPrice;  // Current market price per unit

    // Constructor
    public PortfolioItem(String symbol, int quantity, double purchasePrice, double currentPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
    }

    // Calculated Fields
    public double getMarketValue() {
        return quantity * currentPrice;
    }

    public double getPnL() {
        return getMarketValue() - (quantity * purchasePrice); // Absolute PnL in $
    }

    public double getPnLPercent() {
        double costBasis = quantity * purchasePrice;
        return (getPnL() / costBasis) * 100; // PnL in %
    }

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}