package gui;

import core.WhatToCook;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mateusz on 21.04.2016.
 * Project WhatToCook
 */
/*
    WYŚWIETLA INFORMACJE O AUTORACH PROGRAMU I WERSJI
 */
public class AboutWindow extends JDialog{
    public AboutWindow()
    {
        setSize(250,250);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle(WhatToCook.SelectedPackage.get(35));
        JButton exit = new JButton(WhatToCook.SelectedPackage.get(36));
        exit.addActionListener(e -> setVisible(false));
        JLabel name = new JLabel("<html><h1>WhatToCook 2016</h1></html>");
        JLabel description = new JLabel("<html>" + WhatToCook.SelectedPackage.get(37) +":<br>Radosław Churski<br>Robert Górnicki" +
                "<br>Mateusz Kalinowski<br>Paweł Kurbiel<br>Oksana Stechkevych<br><br>" + WhatToCook.version +"</html>");
        JPanel mainBorderLayout = new JPanel(new BorderLayout());
        mainBorderLayout.add(exit,BorderLayout.SOUTH);
        mainBorderLayout.add(description,BorderLayout.CENTER);
        mainBorderLayout.add(name,BorderLayout.NORTH);
        add(mainBorderLayout);
    }
}
