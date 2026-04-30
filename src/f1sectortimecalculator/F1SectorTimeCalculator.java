/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package f1sectortimecalculator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.lang.reflect.Method;

public class F1SectorTimeCalculator extends JFrame {

    private JTextField[] sectorFields = new JTextField[3];
    private JTextField totalField;
    private JCheckBox onTopCheckBox;

    public static void main(String[] args) {
        // Set macOS-specific properties
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "F1 Sector Time Calculator");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "F1 Sector Time Calculator");
        System.setProperty("apple.awt.application.appearance", "system");

        // These properties help with macOS integration
        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
        System.setProperty("com.apple.macos.smallTabs", "true");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                F1SectorTimeCalculator calculator = new F1SectorTimeCalculator();
                calculator.setVisible(true);

                // These calls help macOS recognize the application
                calculator.setAlwaysOnTop(true);
                calculator.setAlwaysOnTop(false);
                calculator.toFront();

                // Force the application to activate
                try {
                    Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
                    Object application = applicationClass.getMethod("getApplication").invoke(null);
                    Method requestActivationMethod = applicationClass.getMethod("requestActivation", boolean.class);
                    requestActivationMethod.invoke(application, true);
                } catch (Exception e) {
                    // Ignore if not on macOS or method not available
                }
            }
        });
    }

    public F1SectorTimeCalculator() {
        
        setTitle("F1 Fastest Lap Calculator");
        
        setLayout(new BorderLayout());
        setSize(250, 250); 
        setResizable(false);
        setupMacOSApplication();    
        setMacApplicationIcon();

        try {
            java.net.URL iconUrl = getClass().getResource("/f1sectortimecalculator/racing-car.png");
            if (iconUrl != null) {
                setIconImage(Toolkit.getDefaultToolkit().getImage(iconUrl));
            } else {
                System.out.println("Icon file not found in classpath");
            }
        } catch (Exception e) {
            System.out.println("Could not load icon: " + e.getMessage());
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] labels = {"Sector 1:", "Sector 2:", "Sector 3:"};
        for (int i = 0; i < 3; i++) {
            JPanel sectorPanel = new JPanel(new BorderLayout(5, 0));
            JLabel label = new JLabel(labels[i]);
            sectorFields[i] = createSectorTextField();
            sectorPanel.add(label, BorderLayout.WEST);
            sectorPanel.add(sectorFields[i], BorderLayout.CENTER);
            sectorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, sectorPanel.getPreferredSize().height));
            mainPanel.add(sectorPanel);
            mainPanel.add(Box.createVerticalStrut(5)); 
        }

        
        JPanel totalPanel = new JPanel(new BorderLayout(5, 0));
        JLabel totalLabel = new JLabel("Possible Fastest Lap:");
        totalField = new JTextField();
        totalField.setEditable(false);
        totalField.setBackground(Color.WHITE);
        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(totalField, BorderLayout.CENTER);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalPanel.getPreferredSize().height));
        mainPanel.add(totalPanel);
        mainPanel.add(Box.createVerticalStrut(10)); 

       
        JPanel bottomPanel = new JPanel(new BorderLayout());

    
        onTopCheckBox = new JCheckBox("Always on Top");
        onTopCheckBox.setSelected(true);
        setAlwaysOnTop(true);
        onTopCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAlwaysOnTop(onTopCheckBox.isSelected());
            }
        });
        bottomPanel.add(onTopCheckBox, BorderLayout.WEST);

        JButton resetButton = new JButton("X");
        resetButton.setToolTipText("Reset all fields");
        resetButton.setMargin(new Insets(2, 6, 2, 6)); 
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JTextField field : sectorFields) {
                    field.setText("");
                }
                totalField.setText("");
            }
        });
        bottomPanel.add(resetButton, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        calculateTotal();
    }
    private void setupMacOSApplication() {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("mac")) {
                try {
                    Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
                    Object application = applicationClass.getMethod("getApplication").invoke(null);

                    // For Java 7, use the older about handler approach
                    try {
                        Class<?> aboutHandlerClass = Class.forName("com.apple.eawt.AboutHandler");
                        Object aboutHandler = java.lang.reflect.Proxy.newProxyInstance(
                            getClass().getClassLoader(),
                            new Class<?>[] { aboutHandlerClass },
                            new java.lang.reflect.InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    // Handle about menu action if needed
                                    JOptionPane.showMessageDialog(null, "F1 Sector Time Calculator\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE);
                                    return null;
                                }
                            }
                        );

                        Method setAboutHandlerMethod = applicationClass.getMethod("setAboutHandler", aboutHandlerClass);
                        setAboutHandlerMethod.invoke(application, aboutHandler);
                    } catch (ClassNotFoundException e) {
                        System.out.println("AboutHandler not available in this Java version");
                    }

                    System.out.println("macOS application handlers set successfully");
                } catch (ClassNotFoundException e) {
                    // Expected on non-macOS systems
                    System.out.println("macOS Application class not found");
                }
            }
        } catch (Exception e) {
            System.out.println("Error setting macOS application handlers: " + e.getMessage());
        }
    }
    private void setMacApplicationIcon() {
        try {
            java.net.URL iconUrl = getClass().getResource("/f1sectortimecalculator/racing-car.png");
            if (iconUrl != null) {
                ImageIcon icon = new ImageIcon(iconUrl);

                // Check if we're on macOS first
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.contains("mac")) {
                    try {
                        Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
                        Object application = applicationClass.getMethod("getApplication").invoke(null);

                        // For Java 7, use the older method names
                        Method setDockIconImageMethod = applicationClass.getMethod("setDockIconImage", java.awt.Image.class);
                        setDockIconImageMethod.invoke(application, icon.getImage());

                        // Try the Java 7 compatible method for application name
                        try {
                            // This method exists in older Java versions
                            Method setApplicationNameMethod = applicationClass.getMethod("setName", String.class);
                            setApplicationNameMethod.invoke(application, "F1 Sector Time Calculator");
                        } catch (NoSuchMethodException e) {
                            // Fallback: try the newer method name (might not exist in Java 7)
                            try {
                                Method setApplicationNameMethod = applicationClass.getMethod("setApplicationName", String.class);
                                setApplicationNameMethod.invoke(application, "F1 Sector Time Calculator");
                            } catch (NoSuchMethodException ex) {
                                System.out.println("setApplicationName method not available in this Java version");
                            }
                        }

                        System.out.println("macOS dock icon set successfully");
                    } catch (ClassNotFoundException e) {
                        System.out.println("macOS Application class not found (expected on non-macOS systems)");
                    } catch (Exception e) {
                        System.out.println("Error setting macOS dock properties: " + e.getMessage());
                        e.printStackTrace(); // Add this to see the full error
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not set macOS dock icon: " + e.getMessage());
        }
    }

    private JTextField createSectorTextField() {
        final JTextField field = new JTextField();
        field.setDocument(new DigitOnlyDocument());

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String rawDigits = field.getText().replaceAll("\\D", "");
                if (!rawDigits.isEmpty()) {
                    formatFieldValue(field); 
                }
                calculateTotal();
            }
        });
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                
                calculateTotal();
            }
        });
        /*
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void checkForAutoFormat() {
                String text = field.getText().replaceAll("\\D", "");
                if (text.length() == 6) {
          
                    formatFieldValue(field);
                }
                calculateTotal();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkForAutoFormat();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkForAutoFormat();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkForAutoFormat();
            }
        });
         */

        field.setColumns(10);
        return field;
    }

    class DigitOnlyDocument extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }

            String filteredStr = str.replaceAll("\\D", "");
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + filteredStr + currentText.substring(offs);

            if (newText.length() <= 6) {
                super.insertString(offs, filteredStr, a);

                
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        calculateTotal();
                    }
                });
            }
        }
    }

    private void formatFieldValue(JTextField field) {
        String rawDigits = field.getText().replaceAll("\\D", "");

        if (rawDigits.isEmpty()) {
            field.setText("");
            return;
        }

        try {
            
            if (rawDigits.length() == 6) {
                int minute = Integer.parseInt(rawDigits.substring(0, 1));
                int seconds = Integer.parseInt(rawDigits.substring(1, 3));
                int milliseconds = Integer.parseInt(rawDigits.substring(3, 6));
                String formattedTime = String.format("%d:%02d.%03d", minute, seconds, milliseconds);
                field.setText(formattedTime);
            } else {
                
                field.setText(rawDigits);
            }
        } catch (NumberFormatException e) {
            field.setText(rawDigits);
        }
    }

    private double parseTime(String formattedTime) {
        if (formattedTime == null || formattedTime.trim().isEmpty()) {
            return 0.0;
        }

        try {
            
            if (formattedTime.contains(":") && formattedTime.contains(".")) {
                String[] parts = formattedTime.split("[:.]");
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                int milliseconds = Integer.parseInt(parts[2]);
                return minutes * 60 + seconds + milliseconds / 1000.0;
            }

            
            String rawDigits = formattedTime.replaceAll("\\D", "");
            if (!rawDigits.isEmpty()) {
                int length = rawDigits.length();

                if (length <= 3) {
                    
                    return Integer.parseInt(rawDigits) / 1000.0;
                } else if (length <= 5) {
                    
                    int secondsPart = Integer.parseInt(rawDigits.substring(0, length - 3));
                    int millisPart = Integer.parseInt(rawDigits.substring(length - 3));
                    return secondsPart + millisPart / 1000.0;
                } else {
                    
                    
                    int minute = Integer.parseInt(rawDigits.substring(0, 1));
                    int seconds = Integer.parseInt(rawDigits.substring(1, 3));
                    int milliseconds = Integer.parseInt(rawDigits.substring(3, 6));
                    return minute * 60 + seconds + milliseconds / 1000.0;
                }
            }

            return 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void calculateTotal() {
        double totalSeconds = 0.0;
        for (JTextField field : sectorFields) {
            totalSeconds += parseTime(field.getText());
        }

        if (totalSeconds > 0) {
            int totalMinutes = (int) (totalSeconds / 60);
            double remainingSeconds = totalSeconds % 60;

            
            
            totalField.setText(String.format("%d:%06.3f", totalMinutes, remainingSeconds));
        } else {
            totalField.setText("");
        }
    }

}
