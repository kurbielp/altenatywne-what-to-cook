package core;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Mateusz on 20.04.2016.
 * Project WhatToCook
 */
/*
    KLASA "STATYCZNA", PONIEKĄD ABSKTRAKCYJNA, SLUŻY DO OBSŁUGI LISTY PRZEPISÓW, NIE TWORZY OBIEKTÓW
    BO POTRZEBNA JEST TYLKO JEDNA TAKA LISTA
 */
public class IngredientsList {

        static public void initialize(){
        IngredientsList = new ArrayList<>();
        Scanner in;
        String tmp;
        IngredientsList.clear();
        try {
            in = new Scanner(new File(WhatToCook.SelectedPackage.GetIngredientsPath()));
            while (in.hasNextLine()) {
                tmp = in.nextLine();
                if(in.nextLine().contains(",")){
                    String[] parts = tmp.split(",");
                    Ingredient toAdd = new Ingredient(parts[0],parts[1]);
                    addIngredient(toAdd);
                }
                //tmp = in.nextLine();
                else{
                Ingredient toAdd = new Ingredient(tmp,null);
                addIngredient(toAdd);
                }
                Collections.sort(IngredientsList);
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error during loading ingredients");
        }
    }

        static public void addIngredient(Ingredient newIngredient)
    {
        boolean exist = false;

        for(Ingredient ingredient : IngredientsList) {
            if (newIngredient.equals(ingredient)) {
                exist = true;
                break;
            }
        }
        if(!exist) {
            IngredientsList.add(newIngredient);
            Collections.sort(IngredientsList);

            PrintWriter writer;

            try
            {
                writer = new PrintWriter(new File(WhatToCook.SelectedPackage.GetIngredientsPath()));
                for(Ingredient ingredient : IngredientsList)
                {
                    writer.println(ingredient.getName());
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
    static public void removeIngredient(String name)
    {
        for(int i = 0; i < IngredientsList.size();i++)
        {
            if(IngredientsList.get(i).getName().equals(name))
            {
                IngredientsList.remove(i);
                PrintWriter writer;

                try
                {
                    writer = new PrintWriter(new File(WhatToCook.SelectedPackage.GetIngredientsPath()));
                    for(Ingredient ingredient : IngredientsList)
                    {
                        writer.println(ingredient.getName());
                    }
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    static public void rebuildModel(DefaultListModel<String> toInsert)
    {
        toInsert.clear();
        for(Ingredient ingredient : IngredientsList)
        {
            toInsert.addElement(ingredient.getName());
        }
    }
    static public void reloadComboBox(JComboBox<String> comboBox)
    {
        comboBox.removeAllItems();
        for(Ingredient ingredient : IngredientsList)
        {
            comboBox.addItem(ingredient.getName());
        }
    }
    static public boolean contain(Ingredient toCheck)
    {
        for(Ingredient ingredient : IngredientsList)
        {
            if(ingredient.equals(toCheck))
                return true;
        }
        return false;
    }
    static public int Size()
    {
        return IngredientsList.size();
    }
    static public Ingredient Get(int i)
    {
        return IngredientsList.get(i);
    }
    private static  List<Ingredient> IngredientsList;
}
