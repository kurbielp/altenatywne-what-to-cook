package gui;

import core.WhatToCook;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Mateusz on 22.04.2016.
 * Project WhatToCook
 */
/*
    UNIWERSALNE OKNO DO WYSWIETLANIA KOMUNIKATÓW
    METODA "REFRESH" OTRZYMUJE LISTE ELEMENTOW DO WYSWIETLENIA, WIADOMOŚĆ I TYTUŁ OKNA
    OKNO USTALA SWOJĄ WYSOKOŚĆ NA PODSTAWIE ILOŚĆI ELEMENTÓW DO WYŚWIETLENIA
 */
class ErrorWindow extends JDialog {
    ErrorWindow()
    {
        setModal(true);
        setResizable(false);
        Height = 160;
        setLocationRelativeTo(null);
        JPanel mainBorderLayout = new JPanel(new BorderLayout());
        ErrorMessage = "";
        JButton exit = new JButton(WhatToCook.SelectedPackage.get(36));
        exit.addActionListener(e -> setVisible(false));
        ErrorMessageLabel = new JLabel();
        mainBorderLayout.add(ErrorMessageLabel,BorderLayout.NORTH);
        mainBorderLayout.add(exit,BorderLayout.SOUTH);

        add(mainBorderLayout);
    }
    void refresh(ArrayList<String> errorsList,String errorMessage,String windowName)
    {
        setTitle(windowName);
        ErrorMessage = "<html><h4>" + errorMessage + "</h4>";
        for(String toPrint : errorsList)
        {
            ErrorMessage+="<br>" + toPrint;
            Height +=12;
        }
        ErrorMessage+="</hmlt>";
        ErrorMessageLabel.setText(ErrorMessage);
        setSize(300,Height);
        repaint();
    }
    private JLabel ErrorMessageLabel;
    private String ErrorMessage;
    private int Height;
}
