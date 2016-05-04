package gui;

import auxiliary.ListHandler;
import auxiliary.PairAmountUnit;
import auxiliary.RecipeParameters;
import core.*;
import auxiliary.PairRecipeIndex;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mateusz on 20.03.2016.
 * Project WhatToCook
 */
/*
    GŁÓWNA KLASA PROGRAMU, JEST BEZPOŚRENDIO ODPOWIEDZIALNA ZA TWORZENIE GŁÓWNEGO OKNA INTERFEJSU GRAFICZNEGO,
    ALE POŚREDNIO JEST RÓWNIEŻ "FASADĄ" DO KOMUNIKACJI MIĘDZY POZOSTAŁYMI KLASAMI W PROGRAMIE
 */
public class MainWindow extends JFrame {

    public MainWindow() {
        //LEPSZY WYGLAD DLA WINDOWS'A
        String platform = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(platform);
            SwingUtilities.updateComponentTreeUI(MainWindow.this);
        } catch (Exception e) {
            System.out.println("WindowsLookAndFeel is not supported, it's not a problem, you're probably running not windows os");
            System.out.println("If you're running windows and see that information contact developers team");
        }

        RecipesList.initialize();
        IngredientsList.initialize();

        setSize(450, 600);
        setResizable(true);
        setTitle("WhatToCook");
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(340, 400));

        //MENUBAR CREATING/////////////////////////////////////////////////////////////////////////////////////////////
        mainMenu = new JMenuBar();
        setJMenuBar(mainMenu);
        fileMenu = new JMenu(WhatToCook.SelectedPackage.get(0));
        editMenu = new JMenu(WhatToCook.SelectedPackage.get(2));
        helpMenu = new JMenu(WhatToCook.SelectedPackage.get(5));
        newSubmenu = new JMenu(WhatToCook.SelectedPackage.get(45));
        mainMenu.add(fileMenu);
        mainMenu.add(editMenu);
        mainMenu.add(helpMenu);
        fileMenu.add(newSubmenu);

        settingsDialog = new SettingsWindow();
        aboutDialog = new AboutWindow();
        errorDialog = new ErrorWindow();
        Action newIngredientAction = new AbstractAction(WhatToCook.SelectedPackage.get(46)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainTable.setSelectedIndex(2);
                newIngredientTextField.requestFocus();
            }
        };
        Action newRecipeAction = new AbstractAction(WhatToCook.SelectedPackage.get(47)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isEditionTurnOn) {

                    isEditionTurnOn = true;
                    showNewEditMenu();
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), WhatToCook.SelectedPackage.get(79), WhatToCook.SelectedPackage.get(78), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        Action settingsAction = new AbstractAction(WhatToCook.SelectedPackage.get(6)) {
            public void actionPerformed(ActionEvent event) {
                settingsDialog.setVisible(true);
            }
        };

        Action exitAction = new AbstractAction(WhatToCook.SelectedPackage.get(1)) {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        };
        Action clearIngredientsAction = new AbstractAction(WhatToCook.SelectedPackage.get(3)) {
            public void actionPerformed(ActionEvent event) {
                int i = ingredientsInputListModel.getSize() - 1;
                for (; i >= 0; i--)
                    ingredientsInputListModel.removeElementAt(i);
                if (MainWindow.autoLoadIngredients) {
                    String name;
                    String altname;
                    try {
                        Scanner in = new Scanner(new File("data/ownedIngredients"));
                        while (in.hasNextLine()) {
                            String[] parts = in.nextLine().split(",");
                            name = parts[0];
                            altname = parts[1];
                            Ingredient toAdd = new Ingredient(name,altname);

                            if (IngredientsList.contain(toAdd))
                                ingredientsInputListModel.addElement("● " + name);
                        }
                        in.close();

                    } catch (FileNotFoundException | NullPointerException exception) {
                        System.err.println("Internal program error, 'ownedIngredients' not found");
                    }
                }
            }
        };
        Action clearReceipesAction = new AbstractAction(WhatToCook.SelectedPackage.get(4)) {
            public void actionPerformed(ActionEvent event) {
                int i = receipesOutputListModel.getSize() - 1;
                for (; i >= 0; i--)
                    receipesOutputListModel.removeElementAt(i);


            }

        };
        Action aboutAction = new AbstractAction(WhatToCook.SelectedPackage.get(7)) {
            public void actionPerformed(ActionEvent event) {
                aboutDialog.setVisible(true);
            }
        };
        Action exportIngredientsAction = new AbstractAction(WhatToCook.SelectedPackage.get(40)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportIngredientsList();
            }
        };
        Action importIngredientsAction = new AbstractAction(WhatToCook.SelectedPackage.get(41)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                importIngredientsList();
            }
        };
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        newSubmenu.add(newIngredientAction);
        newSubmenu.add(newRecipeAction);
        editMenu.add(exportIngredientsAction);
        editMenu.add(importIngredientsAction);
        editMenu.addSeparator();
        editMenu.add(clearIngredientsAction);
        editMenu.add(clearReceipesAction);
        editMenu.addSeparator();
        editMenu.add(settingsAction);
        helpMenu.add(aboutAction);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //KARTA WYSZUKIWANIA////////////////////////////////////////////////////////////////////////////////////////////
        ingredientsDownGridLayout = new JPanel(new GridLayout(1, 2));
        ingredientsRightDownGridLayout = new JPanel(new GridLayout(10, 1));


        mainBorderLayout = new JPanel(new BorderLayout());
        downBorderLayout = new JPanel(new BorderLayout());
        upBorderLayout = new JPanel(new BorderLayout());
        mainGridLayout = new JPanel(new GridLayout(2, 1));
        upGridLayout = new JPanel(new GridLayout(1, 2));
        upRightGridLayout = new JPanel(new GridLayout(5, 1));

        importExportInSearchGrid = new JPanel(new GridLayout(1, 2));

        isEditionTurnOn = false;

        shownRecipesList = new PairRecipeIndex();

        ingredientInSearchComboBox = new JComboBox<>();
        IngredientsList.reloadComboBox(ingredientInSearchComboBox);

        ingredientInSearchComboBox.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String newForm = "● " + ingredientInSearchComboBox.getSelectedItem();
                    boolean exist = false;
                    for (int i = 0; i < ingredientsInputListModel.getSize(); i++) {
                        if ((newForm.equals(ingredientsInputListModel.get(i)))) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        ingredientsInputListModel.addElement(newForm);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        ingredientsInputListModel = new DefaultListModel<>();
        ingredientsInputList = new JList<>();
        ingredientsInputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ingredientsInputList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        ingredientsInputList.setVisibleRowCount(-1);
        ingredientsInputList = new JList<>(ingredientsInputListModel);
        ingredientsInputListScrollPane = new JScrollPane(ingredientsInputList);
        if (MainWindow.autoLoadIngredients) {
            String name;
            String altname;
            try {
                Scanner in = new Scanner(new File("data/ownedIngredients"));
                while (in.hasNextLine()) {
                    String[] parts = in.nextLine().split(",");
                    name = parts[0];
                    altname = parts[1];
                    Ingredient toAdd = new Ingredient(name,altname);

                    if (IngredientsList.contain(toAdd))
                        ingredientsInputListModel.addElement("● " + name);
                }
                in.close();

            } catch (FileNotFoundException | NullPointerException exception) {
                JOptionPane.showMessageDialog(new JFrame(), WhatToCook.SelectedPackage.get(77), WhatToCook.SelectedPackage.get(76), JOptionPane.ERROR_MESSAGE);
            }
        }


        importIngredientsInSearch = new JButton(WhatToCook.SelectedPackage.get(68));
        importIngredientsInSearch.addActionListener(e ->
        {
            JFileChooser chooseFile = new JFileChooser();
            int save = chooseFile.showOpenDialog(null);
            if (save == JFileChooser.APPROVE_OPTION) {
                String filename = chooseFile.getSelectedFile().getPath();
                String name;
                String altname;
                try {
                    Scanner in = new Scanner(new File(filename));
                    ArrayList<String> notAdded = new ArrayList<>();
                    while (in.hasNextLine()) {
                        String[] parts = in.nextLine().split(",");
                        name = parts[0];
                        altname = parts[1];
                        Ingredient toAdd = new Ingredient(name,altname);

                        if (IngredientsList.contain(toAdd))
                            ingredientsInputListModel.addElement("● " + name);
                        else
                            notAdded.add(name);
                    }
                    if (notAdded.size() > 0) {
                        errorDialog.refresh(notAdded, WhatToCook.SelectedPackage.get(71), WhatToCook.SelectedPackage.get(70));
                        errorDialog.setVisible(true);
                    }
                    in.close();

                } catch (FileNotFoundException exception) {
                    System.err.println("Internal program error, 'ownedIngredients' not found");
                }

            }
        });
        exportIngredientsInSearch = new JButton(WhatToCook.SelectedPackage.get(69));
        exportIngredientsInSearch.addActionListener(e ->
        {
            JFileChooser chooseFile = new JFileChooser();
            int save = chooseFile.showSaveDialog(null);
            if (save == JFileChooser.APPROVE_OPTION) {
                String filename = chooseFile.getSelectedFile().getPath();
                PrintWriter writer;
                try {
                    writer = new PrintWriter(filename, "UTF-8");
                    for (int i = 0; i < ingredientsInputListModel.size(); i++)
                        writer.println(ingredientsInputListModel.get(i).substring(2));

                    writer.close();
                } catch (FileNotFoundException | UnsupportedEncodingException exception) {
                    System.err.println("Exporting ingredients list error.");
                }
            }
        });

        importExportInSearchGrid.add(importIngredientsInSearch);
        importExportInSearchGrid.add(exportIngredientsInSearch);
        ingredientInCreatingRecipeComboBox = new JComboBox<>();
        IngredientsList.reloadComboBox(ingredientInCreatingRecipeComboBox);
        execute = new JButton(WhatToCook.SelectedPackage.get(15));
        execute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                receipesOutputListModel.clear();
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                boolean[] parameters = new boolean[5];
                parameters[0] = breakfestCheckBox.isSelected();
                parameters[1] = dessertCheckBox.isSelected();
                parameters[2] = dinerCheckBox.isSelected();
                parameters[3] = supperCheckBox.isSelected();
                parameters[4] = snackCheckBox.isSelected();
                if (isFalse(parameters, 5)) {
                    for (int i = 0; i < 5; i++) {
                        parameters[i] = true;
                    }
                }
                for (int i = 0; i < ingredientsInputListModel.size(); i++) {
                    ingredients.add(new Ingredient(ingredientsInputListModel.getElementAt(i).substring(2),ingredientsInputListModel.getElementAt(i).substring(3)));
                }
                for (int i = 0; i < RecipesList.size(); i++) {
                    if (RecipesList.checkWithIngredientsList(ingredients, i, parameters, EaseToPrepare.getSelectedIndex(), PreparingTimeComboBox.getSelectedIndex()))
                        receipesOutputListModel.addElement(RecipesList.getRecipeNameAtIndex(i));
                }
            }
        });
        addIngredientButton = new JButton(WhatToCook.SelectedPackage.get(12));
        addIngredientButton.addActionListener(e -> {
            String newForm = "● " + ingredientInSearchComboBox.getSelectedItem();
            boolean exist = false;
            for (int i = 0; i < ingredientsInputListModel.getSize(); i++) {
                if ((newForm.equals(ingredientsInputListModel.get(i)))) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                ingredientsInputListModel.addElement(newForm);
            }
            PrintWriter writer;
            try {
                writer = new PrintWriter("data/ownedIngredients", "UTF-8");
                for (int i = 0; i < ingredientsInputListModel.size(); i++)
                    writer.println(ingredientsInputListModel.get(i).substring(2));

                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException exception) {
                System.err.println("Exporting ingredients list error.");
            }
        });
        removeIngredientButton = new JButton(WhatToCook.SelectedPackage.get(13));
        removeIngredientButton.addActionListener(e -> {
            int index = ingredientsInputList.getSelectedIndex();
            for (int i = ingredientsInputList.getSelectedIndices().length - 1; i >= 0; i--) {
                ingredientsInputListModel.removeElementAt(ingredientsInputList.getSelectedIndices()[i]);
            }
            PrintWriter writer;
            try {
                writer = new PrintWriter("data/ownedIngredients", "UTF-8");
                for (int i = 0; i < ingredientsInputListModel.size(); i++)
                    writer.println(ingredientsInputListModel.get(i).substring(2));

                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException exception) {
                System.err.println("Exporting ingredients list error.");
            }
        });
        //JLISTS
        receipesOutputListModel = new DefaultListModel<>();
        receipesOutputList = new JList<>();
        receipesOutputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receipesOutputList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        receipesOutputList.setVisibleRowCount(-1);
        receipesOutputList = new JList<>(receipesOutputListModel);
        receipesOutputListScrollPane = new JScrollPane(receipesOutputList);
        receipesOutputList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = receipesOutputList.getSelectedIndex();
                    int i;
                    for (i = 0; i < RecipesList.size(); i++) {
                        if (receipesOutputListModel.get(index).equals(RecipesList.recipesList.get(i).getName()))
                            break;
                    }
                    shownRecipesList.add(RecipesList.recipesList.get(i), mainTable.getTabCount(), mainTable.getSelectedIndex());
                    showRecipe(RecipesList.recipesList.get(i));
                }
            }
        });
        ingredientsRightDownGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(50), SwingConstants.CENTER));
        breakfestCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(51));
        dessertCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(52));
        dinerCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(53));
        supperCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(54));
        snackCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(55));
        ingredientsRightDownGridLayout.add(breakfestCheckBox);
        ingredientsRightDownGridLayout.add(dinerCheckBox);
        ingredientsRightDownGridLayout.add(supperCheckBox);
        ingredientsRightDownGridLayout.add(dessertCheckBox);
        ingredientsRightDownGridLayout.add(snackCheckBox);
        ingredientsRightDownGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(56), SwingConstants.CENTER));
        PreparingTimeComboBox = new JComboBox<>();
        PreparingTimeComboBox.addItem(WhatToCook.SelectedPackage.get(59));
        PreparingTimeComboBox.addItem(WhatToCook.SelectedPackage.get(60));
        PreparingTimeComboBox.addItem(WhatToCook.SelectedPackage.get(61));
        ingredientsRightDownGridLayout.add(PreparingTimeComboBox);
        ingredientsRightDownGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(57), SwingConstants.CENTER));
        EaseToPrepare = new JComboBox<>();
        EaseToPrepare.addItem(WhatToCook.SelectedPackage.get(62));
        EaseToPrepare.addItem(WhatToCook.SelectedPackage.get(63));
        EaseToPrepare.addItem(WhatToCook.SelectedPackage.get(64));
        ingredientsRightDownGridLayout.add(EaseToPrepare);
        //PANELS AND OBJECTS LOCATION
        upRightGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(11), SwingConstants.CENTER));
        upRightGridLayout.add(ingredientInSearchComboBox);
        upRightGridLayout.add(addIngredientButton);
        upRightGridLayout.add(removeIngredientButton);
        upRightGridLayout.add(importExportInSearchGrid);
        upGridLayout.add(ingredientsInputListScrollPane);
        upGridLayout.add(upRightGridLayout);
        downBorderLayout.add(new JLabel(WhatToCook.SelectedPackage.get(14), SwingConstants.CENTER), BorderLayout.NORTH);
        downBorderLayout.add(receipesOutputListScrollPane, BorderLayout.CENTER);
        downBorderLayout.add(execute, BorderLayout.SOUTH);
        ingredientsDownGridLayout.add(downBorderLayout);
        ingredientsDownGridLayout.add(ingredientsRightDownGridLayout);
        upBorderLayout.add(new JLabel(WhatToCook.SelectedPackage.get(10), SwingConstants.CENTER), BorderLayout.NORTH);
        upBorderLayout.add(upGridLayout, BorderLayout.CENTER);
        mainGridLayout.add(upBorderLayout);
        mainGridLayout.add(ingredientsDownGridLayout);
        mainBorderLayout.add(mainGridLayout);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //KARTA ZARZĄDZANIA PRZEPISAMI//////////////////////////////////////////////////////////////////////////////////
        manageReceipesMainPanel = new JPanel(new BorderLayout());
        manageReceipesGridPanel = new JPanel(new GridLayout(1, 2));
        manageReceipesLeftBorderLayout = new JPanel(new BorderLayout());
        manageReceipesLeftUpGridPanel = new JPanel(new GridLayout(2, 1));
        manageReceipesLeftDownGridPanel = new JPanel(new GridLayout(1, 3));

        searchForReceipesTextArea = new JTextField();

        searchForReceipesTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshGUILists(searchForReceipesTextArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshGUILists(searchForReceipesTextArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshGUILists(searchForReceipesTextArea.getText());
            }
        });

        newRecipe = new JButton(WhatToCook.SelectedPackage.get(17));
        newRecipe.addActionListener(e -> {
            if (!isEditionTurnOn) {

                isEditionTurnOn = true;
                showNewEditMenu();
            } else
                JOptionPane.showMessageDialog(new JFrame(), WhatToCook.SelectedPackage.get(79), WhatToCook.SelectedPackage.get(78), JOptionPane.ERROR_MESSAGE);
        });
        editRecipe = new JButton(WhatToCook.SelectedPackage.get(18));
        editRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (receipesList.getSelectedIndex() >= 0) {
                    String recipeName = receipesListModel.getElementAt(receipesList.getSelectedIndex());
                    int index = RecipesList.getIndex(recipeName);
                    if (!isEditionTurnOn) {

                        isEditionTurnOn = true;
                        showNewEditMenu(index);
                    }
                }
            }
        });
        deleteRecipe = new JButton(WhatToCook.SelectedPackage.get(31));
        deleteRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = receipesList.getSelectedIndices().length - 1; i >= 0; i--) {
                    String recipeName = receipesListModel.getElementAt(receipesList.getSelectedIndices()[i]);
                    RecipesList.remove(recipeName);
                }
                refreshGUILists();
            }
        });

        receipesListModel = new DefaultListModel<>();
        receipesList = new JList<>();
        receipesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receipesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        receipesList.setVisibleRowCount(-1);
        receipesList = new JList<>(receipesListModel);
        receipesListScrollPane = new JScrollPane(receipesList);
        receipesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = receipesList.getSelectedIndex();
                    int i;
                    for (i = 0; i < RecipesList.recipesList.size(); i++) {
                        if (receipesListModel.get(index).equals(RecipesList.recipesList.get(i).getName()))
                            break;
                    }
                    shownRecipesList.add(RecipesList.recipesList.get(i), mainTable.getTabCount(), mainTable.getSelectedIndex());
                    showRecipe(RecipesList.recipesList.get(i));
                }
            }
        });

        manageReceipesLeftUpGridPanel.add(new JLabel(WhatToCook.SelectedPackage.get(16), SwingConstants.CENTER));
        manageReceipesLeftUpGridPanel.add(searchForReceipesTextArea);
        manageReceipesLeftBorderLayout.add(manageReceipesLeftUpGridPanel, BorderLayout.NORTH);
        manageReceipesLeftDownGridPanel.add(newRecipe);
        manageReceipesLeftDownGridPanel.add(editRecipe);
        manageReceipesLeftDownGridPanel.add(deleteRecipe);
        manageReceipesLeftBorderLayout.add(manageReceipesLeftDownGridPanel, BorderLayout.SOUTH);
        manageReceipesLeftBorderLayout.add(receipesListScrollPane, BorderLayout.CENTER);
        manageReceipesGridPanel.add(manageReceipesLeftBorderLayout);
        manageReceipesMainPanel.add(manageReceipesGridPanel, BorderLayout.CENTER);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //KARTA DO ZARZĄDZANIA SKŁADNIKAMI//////////////////////////////////////////////////////////////////////////////
        ingredientsMainGridLayout = new JPanel(new GridLayout(1, 2));
        ingredientsRightBorderLayout = new JPanel(new BorderLayout());
        ingredientsRightGridLayout = new JPanel(new GridLayout(4, 1));
        manageIngredientsInputListModel = new DefaultListModel<>();
        manageIngredientsInputList = new JList<>();
        manageIngredientsInputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        manageIngredientsInputList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        manageIngredientsInputList.setVisibleRowCount(-1);
        manageIngredientsInputList = new JList<>(manageIngredientsInputListModel);
        manageIngredientsListScrollPane = new JScrollPane(manageIngredientsInputList);
        IngredientsList.rebuildModel(manageIngredientsInputListModel);
        newIngredientButton = new JButton(WhatToCook.SelectedPackage.get(29));
        newIngredientButton.addActionListener(e -> {
            if (!newIngredientTextField.getText().equals("")) {
                Ingredient toAdd = new Ingredient(newIngredientTextField.getText(),null);
                IngredientsList.addIngredient(toAdd);
            }
            newIngredientTextField.setText("");
            IngredientsList.rebuildModel(manageIngredientsInputListModel);
            IngredientsList.reloadComboBox(ingredientInSearchComboBox);
            IngredientsList.reloadComboBox(ingredientInCreatingRecipeComboBox);
        });
        newIngredientTextField = new JTextField();
        newIngredientTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (!newIngredientTextField.getText().equals("")) {
                        Ingredient toAdd = new Ingredient(newIngredientTextField.getText(),null);
                        IngredientsList.addIngredient(toAdd);
                    }
                    newIngredientTextField.setText("");
                    IngredientsList.rebuildModel(manageIngredientsInputListModel);
                    IngredientsList.reloadComboBox(ingredientInSearchComboBox);
                    IngredientsList.reloadComboBox(ingredientInCreatingRecipeComboBox);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        removeIngredientsButton = new JButton(WhatToCook.SelectedPackage.get(30));
        removeIngredientsButton.addActionListener(e -> {
            boolean ifExist = false;
            ArrayList<String> recipesContainIngredient = new ArrayList<>();
            for (int i = 0; i < RecipesList.size(); i++) {
                for (int j = 0; j < RecipesList.recipesList.get(i).getSize(); j++) {
                    if (manageIngredientsInputListModel.get(manageIngredientsInputList.getSelectedIndex()).equals(RecipesList.recipesList.get(i).getIngredient(j).getName())) {
                        ifExist = true;
                        recipesContainIngredient.add(RecipesList.recipesList.get(i).getName());
                    }
                }
            }
            if (!ifExist) {
                if (manageIngredientsInputList.getSelectedIndex() >= 0) {
                    IngredientsList.removeIngredient(manageIngredientsInputList.getSelectedValue());
                }
                IngredientsList.rebuildModel(manageIngredientsInputListModel);
                IngredientsList.reloadComboBox(ingredientInSearchComboBox);
                IngredientsList.reloadComboBox(ingredientInCreatingRecipeComboBox);
            } else {
                errorDialog.refresh(recipesContainIngredient, WhatToCook.SelectedPackage.get(38), WhatToCook.SelectedPackage.get(39));
                errorDialog.setVisible(true);
            }

        });
        ingredientsRightGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(28), SwingConstants.CENTER));
        ingredientsRightGridLayout.add(newIngredientTextField);
        ingredientsRightGridLayout.add(newIngredientButton);
        ingredientsRightGridLayout.add(removeIngredientsButton);

        ingredientsRightBorderLayout.add(ingredientsRightGridLayout, BorderLayout.NORTH);
        ingredientsMainGridLayout.add(manageIngredientsListScrollPane);
        ingredientsMainGridLayout.add(ingredientsRightBorderLayout);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ODŚWIEŻENIE INTERFEJSU

        refreshGUILists();

        //MENU "ZAKŁADKOWE"

        mainTable = new JTabbedPane();
        mainTable.addTab(WhatToCook.SelectedPackage.get(8), mainBorderLayout);
        mainTable.add(WhatToCook.SelectedPackage.get(9), manageReceipesMainPanel);
        mainTable.add(WhatToCook.SelectedPackage.get(27), ingredientsMainGridLayout);
        add(mainTable);
        repaint();
    }

    //FUNCKJA OTWIERA NOWĄ KARTĘ Z KONKRETNYM PRZEPISEM
    private void showRecipe(Recipe recipeToShow) {
        recipesBorderLayout = new JPanel(new BorderLayout());
        recipeTextArea = new JTextArea();
        recipeTextArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        recipeTextArea.setEditable(false);
        recipeTextArea.setLineWrap(true);

        String toShow = "";
        toShow += WhatToCook.SelectedPackage.get(65) + recipeToShow.getName() + "\n\n\n";
        toShow += WhatToCook.SelectedPackage.get(57) + " ";
        if (recipeToShow.getParameters().getPreparingEase() == 0)
            toShow += WhatToCook.SelectedPackage.get(62);
        if (recipeToShow.getParameters().getPreparingEase() == 1)
            toShow += WhatToCook.SelectedPackage.get(63);
        if (recipeToShow.getParameters().getPreparingEase() == 2)
            toShow += WhatToCook.SelectedPackage.get(64);
        toShow += "\n\n";
        toShow += WhatToCook.SelectedPackage.get(56) + " ";
        if (recipeToShow.getParameters().getPreparingTime() == 0)
            toShow += WhatToCook.SelectedPackage.get(59);
        if (recipeToShow.getParameters().getPreparingTime() == 1)
            toShow += WhatToCook.SelectedPackage.get(60);
        if (recipeToShow.getParameters().getPreparingTime() == 2)
            toShow += WhatToCook.SelectedPackage.get(61);
        toShow += "\n\n";
        toShow += WhatToCook.SelectedPackage.get(50);
        if (recipeToShow.getParameters().getParameters()[0])
            toShow += " " + WhatToCook.SelectedPackage.get(51);
        if (recipeToShow.getParameters().getParameters()[1])
            toShow += " " + WhatToCook.SelectedPackage.get(52);
        if (recipeToShow.getParameters().getParameters()[2])
            toShow += " " + WhatToCook.SelectedPackage.get(53);
        if (recipeToShow.getParameters().getParameters()[3])
            toShow += " " + WhatToCook.SelectedPackage.get(54);
        if (recipeToShow.getParameters().getParameters()[4])
            toShow += " " + WhatToCook.SelectedPackage.get(55);
        toShow += "\n\n";
        toShow += WhatToCook.SelectedPackage.get(27) + "\n\n";
        for (int i = 0; i < recipeToShow.getSize(); i++) {
            toShow += recipeToShow.getIngredient(i).toString() + " " + recipeToShow.getAmmount(i) + " " + recipeToShow.getUnit(i) + "\n";

        }
        toShow += "\n\n";
        toShow += WhatToCook.SelectedPackage.get(66) + "\n\n" + recipeToShow.getRecipe();

        recipeTextArea.setText(toShow);
        final String toExport = toShow;
        recipeTextAreaScrollPane = new JScrollPane(recipeTextArea);
        recipesBorderLayout.add(recipeTextAreaScrollPane, BorderLayout.CENTER);
        mainTable.addTab(recipeToShow.getName(), recipesBorderLayout);
        if (MainWindow.getToNewCard) {
            mainTable.setSelectedIndex(mainTable.getTabCount() - 1);
        }

        JPopupMenu showRecipePopup;
        showRecipePopup = new JPopupMenu();
        Action exportRecipeAction = new AbstractAction(WhatToCook.SelectedPackage.get(44)) {
            public void actionPerformed(ActionEvent event) {
                exportTab(recipeToShow, toExport);
            }
        };
        Action closeRecipeAction = new AbstractAction(WhatToCook.SelectedPackage.get(23)) {
            public void actionPerformed(ActionEvent event) {
                closeTab();
            }
        };

        showRecipePopup.add(exportRecipeAction);
        showRecipePopup.add(closeRecipeAction);
        recipeTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showRecipePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showRecipePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void refreshGUILists() {
        receipesListModel.clear();
        for (int i = 0; i < RecipesList.recipesList.size(); i++) {
            receipesListModel.addElement(RecipesList.recipesList.get(i).getName());
        }
    }

    private void refreshGUILists(String StartWith) {
        receipesListModel.clear();
        for (int i = 0; i < RecipesList.recipesList.size(); i++) {
            if (extendedStartsWith(RecipesList.recipesList.get(i).getName().split(" "), StartWith)) {
                receipesListModel.addElement(RecipesList.recipesList.get(i).getName());
            }
        }
    }

    private void exportTab(Recipe recipeToShow, String toExport) {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setSelectedFile(new File(recipeToShow.getName() + ".rtf"));
        int save = chooseFile.showSaveDialog(null);
        if (save == JFileChooser.APPROVE_OPTION) {
            String filename = chooseFile.getSelectedFile().getPath();
            PrintWriter writer;
            try {
                writer = new PrintWriter(filename, "UTF-8");

                writer.println(toExport);

                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException exception) {
                System.err.println("Exporting recipe error.");
            }
        }
    }

    private void closeTab() {
        int SelectedIndex = mainTable.getSelectedIndex();
        mainTable.setSelectedIndex(shownRecipesList.getStartPage(mainTable.getSelectedIndex()));
        shownRecipesList.remove(SelectedIndex);
        mainTable.removeTabAt(SelectedIndex);
    }

    private boolean extendedStartsWith(String[] words, String start) {
        for (String word : words) {
            if (word.toLowerCase().startsWith(start.toLowerCase()))
                return true;
        }
        return false;
    }

    //FUNKCJA TWORZY KARTĘ DO EDYCJI PRZEPISU
    private void showNewEditMenu(int index) {
        ArrayList<ListHandler> ingredientsListInput = new ArrayList<>();
        class addIngredientToRecipe implements KeyListener
        {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    if (!ingredientInCreatingRecipeComboBox.getSelectedItem().equals("")) {
                        String newForm = "● " + ingredientInCreatingRecipeComboBox.getSelectedItem();
                        newForm += " " + newEditAmmountTextArea.getText() + " " + newEditUnitTextArea.getText();
                        boolean exist = false;
                        for (int i = 0; i < ingredientsInputInRecipeListModel.getSize(); i++) {
                            if ((newForm.equals(ingredientsInputInRecipeListModel.get(i)))) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            ingredientsInputInRecipeListModel.addElement(newForm);
                            ingredientsListInput.add(new ListHandler(ingredientInCreatingRecipeComboBox.getSelectedItem().toString(), newEditAmmountTextArea.getText(), newEditUnitTextArea.getText()));
                            newEditAmmountTextArea.setText("");
                            newEditUnitTextArea.setText("");
                        }
                    }
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        }
        addIngredientToRecipe addIngredientKeyListener = new addIngredientToRecipe();
        //ingredientInCreatingRecipeComboBox.addKeyListener(addIngredientKeyListener);
        creatingRecipeTable = new JTabbedPane();
        newEditMainBorderLayout = new JPanel(new BorderLayout());
        newEditMainGridLayout = new JPanel(new GridLayout(2, 1));
        newEditUpGridLayout = new JPanel((new GridLayout(1, 2)));
        newEditDownGridLayout = new JPanel(new GridLayout(1, 2));
        newEditUpRightGridLayout = new JPanel(new GridLayout(6, 1));
        newEditParametersGrid = new JPanel(new GridLayout(10, 1));
        newEditMainDownBorderLayout = new JPanel(new BorderLayout());
        newEditMainUpBorderLayout = new JPanel(new BorderLayout());
        newEditTopGridLayout = new JPanel(new GridLayout(1, 2));
        newEditAmmountAndUnitGridLayoutUp = new JPanel(new GridLayout(1, 2));
        newEditAmmountAndUnitGridLayoutDown = new JPanel(new GridLayout(1, 2));

        newEditAmmountTextArea = new JTextField();
        newEditUnitTextArea = new JTextField();
        newEditAmmountTextArea.addKeyListener(addIngredientKeyListener);
        newEditUnitTextArea.addKeyListener(addIngredientKeyListener);

        newEditAmmountAndUnitGridLayoutUp.add(new JLabel(WhatToCook.SelectedPackage.get(48)));
        newEditAmmountAndUnitGridLayoutUp.add(newEditAmmountTextArea);

        newEditAmmountAndUnitGridLayoutDown.add(new JLabel(WhatToCook.SelectedPackage.get(49)));
        newEditAmmountAndUnitGridLayoutDown.add(newEditUnitTextArea);

        newEditAmmountAndUnitGridLayoutUp.setBorder(new EmptyBorder(2, 2, 2, 2));
        newEditAmmountAndUnitGridLayoutDown.setBorder(new EmptyBorder(2, 2, 2, 2));

        newEditParametersGrid.add(new JLabel(WhatToCook.SelectedPackage.get(50), SwingConstants.CENTER));

        JCheckBox NewEditbreakfestCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(51));
        JCheckBox NewEditdessertCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(52));
        JCheckBox NewEditdinerCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(53));
        JCheckBox NewEditsupperCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(54));
        JCheckBox NewEditsnackCheckBox = new JCheckBox(WhatToCook.SelectedPackage.get(55));
        newEditParametersGrid.add(NewEditbreakfestCheckBox);
        newEditParametersGrid.add(NewEditdinerCheckBox);
        newEditParametersGrid.add(NewEditsupperCheckBox);
        newEditParametersGrid.add(NewEditdessertCheckBox);
        newEditParametersGrid.add(NewEditsnackCheckBox);

        newEditParametersGrid.add(new JLabel(WhatToCook.SelectedPackage.get(56), SwingConstants.CENTER));

        JComboBox<String> NewEditPreparingTimeComboBox = new JComboBox<>();
        NewEditPreparingTimeComboBox.addItem(WhatToCook.SelectedPackage.get(59));
        NewEditPreparingTimeComboBox.addItem(WhatToCook.SelectedPackage.get(60));
        NewEditPreparingTimeComboBox.addItem(WhatToCook.SelectedPackage.get(61));

        newEditParametersGrid.add(NewEditPreparingTimeComboBox);

        newEditParametersGrid.add(new JLabel(WhatToCook.SelectedPackage.get(57), SwingConstants.CENTER));

        JComboBox<String> NewEditEaseToPrepare = new JComboBox<>();
        NewEditEaseToPrepare.addItem(WhatToCook.SelectedPackage.get(62));
        NewEditEaseToPrepare.addItem(WhatToCook.SelectedPackage.get(63));
        NewEditEaseToPrepare.addItem(WhatToCook.SelectedPackage.get(64));
        newEditParametersGrid.add(NewEditEaseToPrepare);

        editNewExitWithoutSaving = new JButton(WhatToCook.SelectedPackage.get(22));
        editNewExitWithoutSaving.addActionListener(e -> {
            isEditionTurnOn = false;
            mainTable.removeTabAt(mainTable.getSelectedIndex());
            mainTable.setSelectedIndex(1);
        });
        editNewExitWithSaving = new JButton(WhatToCook.SelectedPackage.get(21));
        editNewExitWithSaving.addActionListener(e -> {
            String name1 = recipeNameTextField.getText();
            String instructions = instructionsInsertTextArea.getText();
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ArrayList<PairAmountUnit> ammountsAndUnits = new ArrayList<>();
            for (ListHandler handler : ingredientsListInput) {
                Ingredient ingredient;
                ingredient = new Ingredient(handler.getIngredient(),null);
                ammountsAndUnits.add(new PairAmountUnit(handler.getAmmount(), handler.getUnit()));
                ingredients.add(ingredient);
            }
            boolean parameters[] = new boolean[5];
            parameters[0] = NewEditbreakfestCheckBox.isSelected();
            parameters[1] = NewEditdessertCheckBox.isSelected();
            parameters[2] = NewEditdinerCheckBox.isSelected();
            parameters[3] = NewEditsupperCheckBox.isSelected();
            parameters[4] = NewEditsnackCheckBox.isSelected();
            Recipe newRecipe1 = new Recipe(name1, ingredients, ammountsAndUnits, instructions, new RecipeParameters(parameters, NewEditEaseToPrepare.getSelectedIndex(), NewEditPreparingTimeComboBox.getSelectedIndex()));
            if (index < 0) {
                if ((!name1.equals("")) && (!instructions.equals("")) && (!ingredients.isEmpty()) && (!RecipesList.isRecipe(newRecipe1))) {
                    RecipesList.add(newRecipe1);
                    refreshGUILists();
                    isEditionTurnOn = false;
                    mainTable.removeTabAt(mainTable.getSelectedIndex());
                    mainTable.setSelectedIndex(1);
                } else
                    JOptionPane.showConfirmDialog(null, WhatToCook.SelectedPackage.get(32), WhatToCook.SelectedPackage.get(33), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
            if (index >= 0) if ((!name1.equals("")) && (!instructions.equals("")) && (!ingredients.isEmpty())) {
                if (RecipesList.recipesList.get(index).getName().equals(name1) || (!RecipesList.isRecipe(newRecipe1))) {
                    RecipesList.remove(RecipesList.recipesList.get(index).getName());
                    RecipesList.add(newRecipe1);
                    refreshGUILists();
                    isEditionTurnOn = false;
                    mainTable.removeTabAt(mainTable.getSelectedIndex());
                    mainTable.setSelectedIndex(1);
                }
            } else
                JOptionPane.showConfirmDialog(null, WhatToCook.SelectedPackage.get(32), WhatToCook.SelectedPackage.get(33), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        });

        newEditAddIngredientButton = new JButton(WhatToCook.SelectedPackage.get(12));
        newEditAddIngredientButton.addActionListener(event -> {
            if (!ingredientInCreatingRecipeComboBox.getSelectedItem().equals("")) {
                String newForm = "● " + ingredientInCreatingRecipeComboBox.getSelectedItem();
                newForm += " " + newEditAmmountTextArea.getText() + " " + newEditUnitTextArea.getText();
                boolean exist = false;
                for (int i = 0; i < ingredientsInputInRecipeListModel.getSize(); i++) {
                    if ((newForm.equals(ingredientsInputInRecipeListModel.get(i)))) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    ingredientsInputInRecipeListModel.addElement(newForm);
                    ingredientsListInput.add(new ListHandler(ingredientInCreatingRecipeComboBox.getSelectedItem().toString(), newEditAmmountTextArea.getText(), newEditUnitTextArea.getText()));
                    newEditAmmountTextArea.setText("");
                    newEditUnitTextArea.setText("");
                }
            }
        });
        ingredientsInputInRecipeListModel = new DefaultListModel<>();
        ingredientsInputinRecipeList = new JList<>();
        ingredientsInputinRecipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ingredientsInputinRecipeList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        ingredientsInputinRecipeList.setVisibleRowCount(-1);
        ingredientsInputinRecipeList = new JList<>(ingredientsInputInRecipeListModel);
        ingredientsInputinRecipeListScrollPane = new JScrollPane(ingredientsInputinRecipeList);

        newEditRemoveIngredientButton = new JButton(WhatToCook.SelectedPackage.get(13));
        newEditRemoveIngredientButton.addActionListener(event -> {
            for (int i = ingredientsInputinRecipeList.getSelectedIndices().length - 1; i >= 0; i--) {
                ingredientsListInput.remove(ingredientsInputinRecipeList.getSelectedIndices()[i]);
                ingredientsInputInRecipeListModel.removeElementAt(ingredientsInputinRecipeList.getSelectedIndices()[i]);
            }
        });


        instructionsInsertTextArea = new JTextArea();
        instructionsInsertTextArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        instructionsInsertTextArea.setLineWrap(true);
        instructionAreaJScrollPane = new JScrollPane(instructionsInsertTextArea);
        recipeNameTextField = new JTextField();

        newEditTopGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(34), SwingConstants.CENTER));
        newEditTopGridLayout.add(recipeNameTextField);
        newEditMainUpBorderLayout.add(newEditTopGridLayout, BorderLayout.NORTH);

        if (index >= 0) {
            recipeNameTextField.setText(RecipesList.recipesList.get(index).getName());
            instructionsInsertTextArea.setText(RecipesList.recipesList.get(index).getRecipe());
            for (int i = 0; i < RecipesList.recipesList.get(index).getSize(); i++) {
                String toAdd;
                toAdd = "● " + RecipesList.recipesList.get(index).getIngredient(i).getName();
                toAdd += " " + RecipesList.recipesList.get(index).getAmmount(i);
                toAdd += " " + RecipesList.recipesList.get(index).getUnit(i);

                ingredientsInputInRecipeListModel.addElement(toAdd);
                ingredientsListInput.add(new ListHandler(RecipesList.recipesList.get(index).getIngredient(i).getName(), RecipesList.recipesList.get(index).getAmmount(i), RecipesList.recipesList.get(index).getUnit(i)));
            }
            if (RecipesList.recipesList.get(index).getParameters().getParameters()[0])
                NewEditbreakfestCheckBox.setSelected(true);
            if (RecipesList.recipesList.get(index).getParameters().getParameters()[1])
                NewEditdessertCheckBox.setSelected(true);
            if (RecipesList.recipesList.get(index).getParameters().getParameters()[2])
                NewEditdinerCheckBox.setSelected(true);
            if (RecipesList.recipesList.get(index).getParameters().getParameters()[3])
                NewEditsupperCheckBox.setSelected(true);
            if (RecipesList.recipesList.get(index).getParameters().getParameters()[4])
                NewEditsnackCheckBox.setSelected(true);
            NewEditEaseToPrepare.setSelectedIndex(RecipesList.recipesList.get(index).getParameters().getPreparingEase());
            NewEditPreparingTimeComboBox.setSelectedIndex(RecipesList.recipesList.get(index).getParameters().getPreparingTime());
            repaint();
        }

        newEditUpRightGridLayout.add(ingredientsInputinRecipeListScrollPane);
        newEditUpRightGridLayout.add(new JLabel(WhatToCook.SelectedPackage.get(11), SwingConstants.CENTER));
        newEditUpRightGridLayout.add(ingredientInCreatingRecipeComboBox);
        newEditUpRightGridLayout.add(newEditAmmountAndUnitGridLayoutUp);
        newEditUpRightGridLayout.add(newEditAmmountAndUnitGridLayoutDown);
        newEditUpRightGridLayout.add(newEditAddIngredientButton);
        newEditUpRightGridLayout.add(newEditRemoveIngredientButton);


        newEditDownGridLayout.add(editNewExitWithSaving);
        newEditDownGridLayout.add(editNewExitWithoutSaving);
        newEditMainDownBorderLayout.add(new JLabel(WhatToCook.SelectedPackage.get(20), SwingConstants.CENTER), BorderLayout.NORTH);
        newEditMainDownBorderLayout.add(instructionAreaJScrollPane, BorderLayout.CENTER);
        newEditUpGridLayout.add(ingredientsInputinRecipeListScrollPane);
        creatingRecipeTable.addTab(WhatToCook.SelectedPackage.get(27), newEditUpRightGridLayout);
        creatingRecipeTable.addTab(WhatToCook.SelectedPackage.get(58), newEditParametersGrid);
        newEditUpGridLayout.add(creatingRecipeTable);
        newEditMainUpBorderLayout.add(newEditUpGridLayout, BorderLayout.CENTER);

        newEditMainDownBorderLayout.add(newEditDownGridLayout, BorderLayout.SOUTH);


        newEditMainGridLayout.add(newEditMainUpBorderLayout);
        newEditMainGridLayout.add(newEditMainDownBorderLayout);

        newEditMainBorderLayout.add(newEditMainGridLayout, BorderLayout.CENTER);
        if (index == -1) {
            mainTable.addTab(WhatToCook.SelectedPackage.get(17), newEditMainBorderLayout);
        } else {
            mainTable.addTab(RecipesList.recipesList.get(index).getName(), newEditMainBorderLayout);
        }
        if (MainWindow.getToNewCard) {
            mainTable.setSelectedIndex(mainTable.getTabCount() - 1);
        }

    }

    private void showNewEditMenu() {
        showNewEditMenu(-1);
    }

    private void exportIngredientsList() {
        JFileChooser chooseFile = new JFileChooser();
        int save = chooseFile.showSaveDialog(null);
        if (save == JFileChooser.APPROVE_OPTION) {
            String filename = chooseFile.getSelectedFile().getPath();
            PrintWriter writer;
            try {
                writer = new PrintWriter(filename, "UTF-8");
                for (int i = 0; i < IngredientsList.Size(); i++) {
                    writer.println(IngredientsList.Get(i).getName());
                }
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                System.err.println("Internal program error, can't export ingredients list to demanded localization");
            }
        }
    }

    private void importIngredientsList() {
        JFileChooser chooseFile = new JFileChooser();
        int save = chooseFile.showOpenDialog(null);
        if (save == JFileChooser.APPROVE_OPTION) {
            String filename = chooseFile.getSelectedFile().getPath();
            String name;
            String altname;
            try {
                Scanner in = new Scanner(new File(filename));
                while (in.hasNextLine()) {
                    String[] parts = in.nextLine().split(",");
                    name = parts[0];
                    altname = parts[1];
                    Ingredient toAdd = new Ingredient(name,altname);
                    IngredientsList.addIngredient(toAdd);
                }
                IngredientsList.rebuildModel(manageIngredientsInputListModel);
                IngredientsList.reloadComboBox(ingredientInSearchComboBox);
                IngredientsList.reloadComboBox(ingredientInCreatingRecipeComboBox);
                in.close();

            } catch (FileNotFoundException e) {
                System.err.println("File " + filename + "don't exist.");
            }

        }
    }

    private boolean isFalse(boolean parameters[], int n) {
        for (int i = 0; i < n; i++) {
            if (parameters[i]) {
                return false;
            }
        }

        return true;
    }

    void exportOwnedIngredients() {
        PrintWriter writer;
        try {
            writer = new PrintWriter("data/ownedIngredients", "UTF-8");
            for (int i = 0; i < ingredientsInputListModel.size(); i++)
                writer.println(ingredientsInputListModel.get(i).substring(2));

            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException exception) {
            System.err.println("Exporting ingredients list error.");
        }
    }


    //ELEMENTY GUI BAZY SKŁADNIKÓW
    private JTabbedPane mainTable;
    private JTabbedPane creatingRecipeTable;

    private JScrollPane ingredientsInputListScrollPane;
    private JScrollPane receipesOutputListScrollPane;
    private JScrollPane receipesListScrollPane;
    private JScrollPane ingredientsInputinRecipeListScrollPane;
    private JScrollPane manageIngredientsListScrollPane;
    private JScrollPane recipeTextAreaScrollPane;
    private JScrollPane instructionAreaJScrollPane;

    private JCheckBox breakfestCheckBox;
    private JCheckBox dessertCheckBox;
    private JCheckBox dinerCheckBox;
    private JCheckBox supperCheckBox;
    private JCheckBox snackCheckBox;

    private JComboBox<String> ingredientInSearchComboBox;
    private JComboBox<String> ingredientInCreatingRecipeComboBox;
    private JComboBox<String> PreparingTimeComboBox;
    private JComboBox<String> EaseToPrepare;

    private JButton newIngredientButton;
    private JButton removeIngredientsButton;
    private JButton importIngredientsInSearch;
    private JButton exportIngredientsInSearch;
    private JButton editNewExitWithoutSaving;
    private JButton editNewExitWithSaving;
    private JButton execute;
    private JButton addIngredientButton;
    private JButton removeIngredientButton;
    private JButton newRecipe;
    private JButton editRecipe;
    private JButton deleteRecipe;
    private JButton newEditAddIngredientButton;
    private JButton newEditRemoveIngredientButton;

    private JPanel ingredientsMainGridLayout;
    private JPanel ingredientsRightBorderLayout;
    private JPanel ingredientsRightGridLayout;
    private JPanel ingredientsDownGridLayout;
    private JPanel ingredientsRightDownGridLayout;
    private JPanel recipesBorderLayout;
    private JPanel recipesGridLayout;
    private JPanel newEditMainBorderLayout;
    private JPanel newEditMainGridLayout;
    private JPanel newEditUpGridLayout;
    private JPanel newEditDownGridLayout;
    private JPanel newEditUpRightGridLayout;
    private JPanel newEditMainDownBorderLayout;
    private JPanel newEditMainUpBorderLayout;
    private JPanel newEditTopGridLayout;
    private JPanel newEditAmmountAndUnitGridLayoutUp;
    private JPanel newEditAmmountAndUnitGridLayoutDown;
    private JPanel newEditParametersGrid;
    private JPanel manageReceipesMainPanel;
    private JPanel manageReceipesGridPanel;
    private JPanel manageReceipesLeftBorderLayout;
    private JPanel manageReceipesLeftUpGridPanel;
    private JPanel manageReceipesLeftDownGridPanel;
    private JPanel mainBorderLayout;
    private JPanel downBorderLayout;
    private JPanel upBorderLayout;
    private JPanel importExportInSearchGrid;

    private JTextArea recipeTextArea;
    private JTextArea instructionsInsertTextArea;

    private JTextField newIngredientTextField;
    private JTextField newEditAmmountTextArea;
    private JTextField newEditUnitTextArea;
    private JTextField recipeNameTextField;
    private JTextField searchForReceipesTextArea;

    private JPanel mainGridLayout;
    private JPanel upGridLayout;
    private JPanel upRightGridLayout;

    private JMenuBar mainMenu;

    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenu newSubmenu;

    private JList<String> ingredientsInputList;
    private final DefaultListModel<String> ingredientsInputListModel;

    private JList<String> receipesOutputList;
    private final DefaultListModel<String> receipesOutputListModel;

    private JList<String> receipesList;
    private final DefaultListModel<String> receipesListModel;

    private JList<String> ingredientsInputinRecipeList;
    private DefaultListModel<String> ingredientsInputInRecipeListModel;

    private JList<String> manageIngredientsInputList;
    private final DefaultListModel<String> manageIngredientsInputListModel;

    private SettingsWindow settingsDialog;
    private AboutWindow aboutDialog;
    private ErrorWindow errorDialog;

    private PairRecipeIndex shownRecipesList;

    public static boolean getToNewCard;
    public static boolean autoLoadIngredients;
    private boolean isEditionTurnOn;
}
