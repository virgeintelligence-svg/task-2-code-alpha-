// Portfolio.java
import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private Map<String, Integer> holdings;
    private double cashBalance;

    public Portfolio(double initialCash) {
        this.holdings = new HashMap<>();
        this.cashBalance = initialCash;
    }

    public void buyStock(Stock stock, int shares) {
        if (stock == null) {
            System.out.println("Invalid stock!");
            return;
        }
        
        double cost = stock.getCurrentPrice() * shares;
        if (cost > cashBalance) {
            System.out.println("Insufficient funds! You need $" + String.format("%.2f", cost) + 
                             " but have $" + String.format("%.2f", cashBalance));
            return;
        }
        
        cashBalance -= cost;
        String symbol = stock.getSymbol();
        int currentShares = holdings.getOrDefault(symbol, 0);
        holdings.put(symbol, currentShares + shares);
        
        System.out.println("✓ Bought " + shares + " shares of " + symbol + " at $" + 
                          String.format("%.2f", stock.getCurrentPrice()));
        System.out.println("Remaining cash: $" + String.format("%.2f", cashBalance));
    }

    public void sellStock(Stock stock, int shares) {
        if (stock == null) {
            System.out.println("Invalid stock!");
            return;
        }
        
        String symbol = stock.getSymbol();
        int currentShares = holdings.getOrDefault(symbol, 0);
        
        if (currentShares < shares) {
            System.out.println("You don't have enough shares to sell! You have " + 
                             currentShares + " shares.");
            return;
        }
        
        double revenue = stock.getCurrentPrice() * shares;
        cashBalance += revenue;
        
        if (currentShares == shares) {
            holdings.remove(symbol);
        } else {
            holdings.put(symbol, currentShares - shares);
        }
        
        System.out.println("✓ Sold " + shares + " shares of " + symbol + " at $" + 
                          String.format("%.2f", stock.getCurrentPrice()));
        System.out.println("Cash balance: $" + String.format("%.2f", cashBalance));
    }

    public double getTotalValue(StockMarket market) {
        double total = cashBalance;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock stock = market.getStock(entry.getKey());
            if (stock != null) {
                total += stock.getCurrentPrice() * entry.getValue();
            }
        }
        return total;
    }

    public void displayPortfolio(StockMarket market) {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║           PORTFOLIO                   ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("Cash Balance: $" + String.format("%.2f", cashBalance));
        System.out.println("\nHoldings:");
        
        if (holdings.isEmpty()) {
            System.out.println("  No stocks owned");
        } else {
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                Stock stock = market.getStock(entry.getKey());
                if (stock != null) {
                    double value = stock.getCurrentPrice() * entry.getValue();
                    System.out.printf("  %-6s %d shares @ $%-8.2f = $%-8.2f%n", 
                        entry.getKey(), entry.getValue(), stock.getCurrentPrice(), value);
                }
            }
            System.out.println("\nTotal Portfolio Value: $" + 
                             String.format("%.2f", getTotalValue(market)));
        }
        System.out.println();
    }

    public double getCashBalance() { 
        return cashBalance; 
    }
    
    public Map<String, Integer> getHoldings() { 
        return holdings; 
    }
}