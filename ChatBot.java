import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatBot {

    private static Map<String, String> knowledgeBase = new HashMap<>();

    static {
        // Basic FAQ training data
        knowledgeBase.put("hi", "Hello! How can I help you?");
        knowledgeBase.put("hello", "Hi there! What can I do for you?");
        knowledgeBase.put("how are you", "I'm just a bot, but I'm doing great! How about you?");
        knowledgeBase.put("bye", "Goodbye! Have a nice day!");
        knowledgeBase.put("your name", "I am a simple Java Chatbot.");
        knowledgeBase.put("help", "I can answer basic questions. Try asking about my name or say hi!");
    }

    public static String getResponse(String input) {
        String lowerInput = input.toLowerCase();
        for (String key : knowledgeBase.keySet()) {
            if (lowerInput.contains(key)) {
                return knowledgeBase.get(key);
            }
        }
        return "I'm not sure how to respond to that. Try asking something else!";
    }

    public static void runConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! I'm your AI Chatbot. Type 'bye' to exit.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Goodbye!");
                break;
            }
            String response = getResponse(input);
            System.out.println(response);
        }
        scanner.close();
    }

    public static void runGUI() {
        JFrame frame = new JFrame("Java AI Chatbot");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);

        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        JScrollPane scrollPane = new JScrollPane(chatArea);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            String input = inputField.getText();
            chatArea.append("You: " + input + "\n");
            String response = getResponse(input);
            chatArea.append("Bot: " + response + "\n\n");
            inputField.setText("");
        });

        inputField.addActionListener(e -> sendButton.doClick());

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("1. Console Chatbot");
        System.out.println("2. GUI Chatbot");
        System.out.print("Choose mode: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice == 1) {
            runConsole();
        } else if (choice == 2) {
            runGUI();
        } else {
            System.out.println("Invalid choice.");
        }
    }
}
