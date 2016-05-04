package gui;

import core.WhatToCook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Mateusz on 23.03.2016.
 * Project WhatToCook
 */
/*
    OKNO Z USTAWIENIAMI
    UŻYTKOWNIK MOŻE WYBRAĆ
        -AUTOMATYCZNE PRZECHODZENIE DO NOWO OTWARTEJ KARTY
        -JĘZYK INTERFACE'U PROGRAMU I ZARAZEM PACZKĘ JĘZYKOWĄ
        -AUTOMATYCZNE WCZYTYWANIE LISTY POSIADANYCH SKŁADNIKÓW
        -ŚCIEŻKĘ DO PLIKU ZE SKŁADNIKAMI
 */
class SettingsWindow extends JDialog {
     SettingsWindow() {
        setSize(640, 150);
        setModal(true);
         setResizable(false);
        setTitle(WhatToCook.SelectedPackage.get(6));
        setLocationRelativeTo(null);
        mainTable = new JTabbedPane();
        languageComboBox = new JComboBox<>();
        languageComboBox.addItem("Polski");
        languageComboBox.addItem("English");
        languageComboBox.setToolTipText(WhatToCook.SelectedPackage.get(67));
        mainGridLayout = new JPanel(new GridLayout(3 , 2));

        toNewCardCheckbox = new JCheckBox();
        if (MainWindow.getToNewCard) {
            toNewCardCheckbox.setSelected(true);
        }
        toNewCardCheckbox.addActionListener(e -> {
            MainWindow.getToNewCard = toNewCardCheckbox.isSelected();
            exportSettings();
        });

        autoImportIngredientsCheckbox = new JCheckBox();
        autoImportIngredientsCheckbox.addActionListener(e -> {
            MainWindow.autoLoadIngredients = autoImportIngredientsCheckbox.isSelected();
            if(!autoImportIngredientsCheckbox.isSelected())
            {
                PrintWriter writer;
                try {
                    writer = new PrintWriter(new File("src/ownedIngredients"));
                    writer.close();
                } catch (FileNotFoundException exception)
                {
                    System.err.println("File 'ownedIngredients' not found, it is internal jar error, download program once again");
                }

            }
            if(autoImportIngredientsCheckbox.isSelected())
            {
                WhatToCook.frame.exportOwnedIngredients();
            }
            exportSettings();
        });
        autoImportIngredientsCheckbox.setSelected(MainWindow.autoLoadIngredients);

        mainGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(24), SwingConstants.CENTER));
        mainGridLayout.add(toNewCardCheckbox);
        mainGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(73),SwingConstants.CENTER));
        mainGridLayout.add(autoImportIngredientsCheckbox);
        mainGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(25), SwingConstants.CENTER));
        mainGridLayout.add(languageComboBox);

        mainTable.addTab(WhatToCook.SelectedPackage.get(26), mainGridLayout);

        if (WhatToCook.SelectedPackage.equals(WhatToCook.PolishPackage)) {
            languageComboBox.setSelectedIndex(0);
        }
        if (WhatToCook.SelectedPackage.equals(WhatToCook.EnglishPackage)) {
            languageComboBox.setSelectedIndex(1);
        }

        languageComboBox.addActionListener(e -> {
            if (languageComboBox.getSelectedItem() == "Polski") {
                int selection;
                selection = JOptionPane.showConfirmDialog(null, WhatToCook.SelectedPackage.get(42), WhatToCook.SelectedPackage.get(43), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (selection == JOptionPane.OK_OPTION) {
                    WhatToCook.SelectedPackage = WhatToCook.PolishPackage;
                    WhatToCook.frame.dispose();
                    WhatToCook.frame = new MainWindow();
                    WhatToCook.frame.setVisible(true);
                    WhatToCook.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    setTitle(WhatToCook.SelectedPackage.get(6));
                    repaint();
                    setVisible(false);
                    toFront();
                } else languageComboBox.setSelectedIndex(1);
            }
            if (languageComboBox.getSelectedItem() == "English") {
                int selection;
                selection = JOptionPane.showConfirmDialog(null, WhatToCook.SelectedPackage.get(42), WhatToCook.SelectedPackage.get(43), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (selection == JOptionPane.OK_OPTION) {
                    // WhatToCook.SelectedPackage = WhatToCook.englishLanguagePack;
                    WhatToCook.SelectedPackage = WhatToCook.EnglishPackage;
                    WhatToCook.frame.dispose();
                    WhatToCook.frame = new MainWindow();
                    WhatToCook.frame.setVisible(true);
                    WhatToCook.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    setTitle(WhatToCook.SelectedPackage.get(6));
                    repaint();
                    setVisible(false);
                    toFront();
                } else
                    languageComboBox.setSelectedIndex(0);

            }
            exportSettings();
        });
        add(mainTable);

    }

    public String getLanguage() {
        return (String) languageComboBox.getSelectedItem();
    }

    public boolean getToNewCardCheckbox() {
        return toNewCardCheckbox.isSelected();
    }

    private void exportSettings() {
        try {
            PrintWriter writer = new PrintWriter(new File("src/cfg"));
            if (languageComboBox.getSelectedItem() == "Polski") {
                writer.println("polish");
            }
            if (languageComboBox.getSelectedItem() == "English") {
                writer.println("english");
            }
            writer.println(toNewCardCheckbox.isSelected());
            writer.println(autoImportIngredientsCheckbox.isSelected());
            writer.close();

        } catch (FileNotFoundException e) {
            System.err.println("It was impossible to export settings");
        }

    }

    JComboBox<String> languageComboBox;
    JTabbedPane mainTable;
    JPanel mainGridLayout;
    JCheckBox toNewCardCheckbox;
    JCheckBox autoImportIngredientsCheckbox;


}
