import java.io.*;
import java.util.*;

class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    void updatePrice() {
        // Randomly adjust price by -5% to +5%
        double changePercent = (Math.random() * 10) - 5;
        price += price * (changePercent / 100);
        if (price < 1) price = 1; // minimum price
    }

    @Override
    public String toString() {
        return symbol + " : $" + String.format("%.2f", price);
    }
}

class Portfolio {
    String owner;
    Map<String, Integer> holdings;
    double balance;

    Portfolio(String owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
        this.holdings = new HashMap<>();
    }

    void buyStock(Stock stock, int quantity) {
        double totalCost = stock.price * quantity;
        if (totalCost > balance) {
            System.out.println("Insufficient funds to buy.");
            return;
        }
        balance -= totalCost;
        holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
        System.out.println("Bought " + quantity + " shares of " + stock.symbol);
    }

    void sellStock(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.symbol, 0);
        if (quantity > owned) {
            System.out.println("Not enough shares to sell.");
            return;
        }
        balance += stock.price * quantity;
        holdings.put(stock.symbol, owned - quantity);
        System.out.println("Sold " + quantity + " shares of " + stock.symbol);
    }

    void viewPortfolio(List<Stock> market) {
        System.out.println("=== Portfolio of " + owner + " ===");
        System.out.println("Balance: $" + String.format("%.2f", balance));
        double totalValue = balance;
        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double stockPrice = 0;
            for (Stock s : market) {
                if (s.symbol.equals(symbol)) {
                    stockPrice = s.price;
                    break;
                }
            }
            double value = qty * stockPrice;
            System.out.println(symbol + ": " + qty + " shares @ $" + String.format("%.2f", stockPrice)
                    + " = $" + String.format("%.2f", value));
            totalValue += value;
        }
        System.out.println("Total Portfolio Value: $" + String.format("%.2f", totalValue));
    }

    void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println(owner);
            out.println(balance);
            for (String symbol : holdings.keySet()) {
                out.println(symbol + "," + holdings.get(symbol));
            }
            System.out.println("Portfolio saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving portfolio: " + e.getMessage());
        }
    }

    static Portfolio loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String owner = br.readLine();
            double balance = Double.parseDouble(br.readLine());
            Portfolio p = new Portfolio(owner, balance);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                p.holdings.put(parts[0], Integer.parseInt(parts[1]));
            }
            System.out.println("Portfolio loaded from " + filename);
            return p;
        } catch (IOException e) {
            System.out.println("Error loading portfolio: " + e.getMessage());
            return null;
        }
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Stock> market = new ArrayList<>();
        market.add(new Stock("AAPL", 150));
        market.add(new Stock("GOOG", 2800));
        market.add(new Stock("TSLA", 700));
        market.add(new Stock("AMZN", 3400));
        market.add(new Stock("NFLX", 550));

        Portfolio portfolio = null;

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        File file = new File(name + "_portfolio.txt");
        if (file.exists()) {
            portfolio = Portfolio.loadFromFile(file.getName());
            if (portfolio == null) {
                portfolio = new Portfolio(name, 10000);
            }
        } else {
            portfolio = new Portfolio(name, 10000);
        }

        boolean running = true;

        while (running) {
            System.out.println("\n=== Stock Trading Platform ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Save & Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Market Data ---");
                    for (Stock s : market) {
                        s.updatePrice();
                        System.out.println(s);
                    }
                    break;
                case 2:
                    System.out.println("\n--- Buy Stock ---");
                    for (int i = 0; i < market.size(); i++) {
                        System.out.println((i + 1) + ". " + market.get(i));
                    }
                    System.out.print("Select stock to buy: ");
                    int buyIndex = scanner.nextInt() - 1;
                    System.out.print("Quantity: ");
                    int buyQty = scanner.nextInt();
                    portfolio.buyStock(market.get(buyIndex), buyQty);
                    break;
                case 3:
                    System.out.println("\n--- Sell Stock ---");
                    for (int i = 0; i < market.size(); i++) {
                        System.out.println((i + 1) + ". " + market.get(i));
                    }
                    System.out.print("Select stock to sell: ");
                    int sellIndex = scanner.nextInt() - 1;
                    System.out.print("Quantity: ");
                    int sellQty = scanner.nextInt();
                    portfolio.sellStock(market.get(sellIndex), sellQty);
                    break;
                case 4:
                    portfolio.viewPortfolio(market);
                    break;
                case 5:
                    portfolio.saveToFile(name + "_portfolio.txt");
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}
