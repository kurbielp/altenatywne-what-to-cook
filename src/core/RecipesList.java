package core;

import auxiliary.PairAmountUnit;
import auxiliary.RecipeParameters;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by Mateusz on 23.03.2016.
 * Project WhatToCook
 */
/*
    KLASA "STATYCZNA", PONIEKĄD ABSKTRAKCYJNA, SLUŻY DO OBSŁUGI LISTY PRZEPISÓW, NIE TWORZY OBIEKTÓW
    BO POTRZEBNA JEST TYLKO JEDNA TAKA LISTA
 */
public class RecipesList
{
    static public void initialize()
    {
        recipesList = new ArrayList<>();

        recipesList.clear();
        int preparingTime;
        int preparingEase;
        final File[] listOfFiles = new File(WhatToCook.SelectedPackage.GetRecipesPath()).listFiles();
        int i = 0;
        while (i < listOfFiles.length) {
            String name;
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ArrayList<PairAmountUnit> ammountsAndUnits = new ArrayList<>();
            boolean parameters[] = new boolean[5];
            int ingredientsAmmount;
            String instructions = "";

            try{
                Scanner in = new Scanner(listOfFiles[i]);
                name = in.nextLine();
                preparingEase = in.nextInt();
                preparingTime = in.nextInt();
                in.nextLine();
                String parametersString = in.nextLine();
                String dividedParameters[] = parametersString.split(" ");
                for(int j = 0; j < 5; j ++) {
                    parameters[j] = dividedParameters[j].equals("true");
                }
                ingredientsAmmount = in.nextInt();
                in.nextLine();
                for(int j = 0; j < ingredientsAmmount;j++)
                {
                    String nextName = in.nextLine();
                    String IngredientName = "";
                    String[] ingredient;
                    ingredient = nextName.split(" ");
                    for(int k = 0;k < ingredient.length-2;k++) {
                        IngredientName += ingredient[k];
                        if(k<ingredient.length-3)
                            IngredientName+=" ";
                    }
                    Ingredient newIngredient = new Ingredient(IngredientName,null);
                    ingredients.add(newIngredient);

                    String ammount = "";
                    String unit = "";
                    if(ingredient[ingredient.length-2].equals("true"))
                        ammount = in.nextLine();
                    if(ingredient[ingredient.length-1].equals("true"))
                        unit = in.nextLine();

                    ammountsAndUnits.add(new PairAmountUnit(ammount,unit));



                }
                while(in.hasNextLine())
                {
                    instructions+=in.nextLine();
                }
                recipesList.add(new Recipe(name,ingredients,ammountsAndUnits,instructions,new RecipeParameters(parameters,preparingEase,preparingTime)));
                Collections.sort(recipesList);
                in.close();
            }
            catch (FileNotFoundException e)
            {

            }
            i++;
        }


    }
    static public void add(Recipe recipe)
    {
        recipesList.add(recipe);
        Collections.sort(recipesList);
        String filename;
        filename = WhatToCook.SelectedPackage.GetRecipesPath() +"/"+ recipe.getName();
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename)));
           writer.println(recipe.getName());
            writer.println(recipe.getParameters().getPreparingEase());
            writer.println(recipe.getParameters().getPreparingTime());
            String parameters = "";
            parameters+=recipe.getParameters().getParameters()[0]+ " ";
            parameters+=recipe.getParameters().getParameters()[1]+ " ";
            parameters+=recipe.getParameters().getParameters()[2]+ " ";
            parameters+=recipe.getParameters().getParameters()[3]+ " ";
            parameters+=recipe.getParameters().getParameters()[4]+ " ";
            writer.println(parameters);
            writer.println(recipe.getSize());
            for(int i = 0; i < recipe.getSize();i++)
            {
                String NameAndBooleans;
                NameAndBooleans = recipe.getIngredient(i).getName();
                if(recipe.getAmmount(i).equals(""))
                    NameAndBooleans+=" " + false;
                else
                    NameAndBooleans+=" " + true;
                if(recipe.getUnit(i).equals(""))
                    NameAndBooleans+=" " + false;
                else
                    NameAndBooleans+=" " + true;
                writer.println(NameAndBooleans);
                if(!recipe.getAmmount(i).equals(""))
                    writer.println(recipe.getAmmount(i));
                if(!recipe.getUnit(i).equals(""))
                    writer.println(recipe.getUnit(i));
            }
            writer.println((recipe.getRecipe()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static public boolean isRecipe(Recipe toCheck)
    {

        for(Recipe recipe : recipesList)
        {
            if(toCheck.getName().equals(recipe.getName()))
                return true;
        }
        return false;
    }
    static public int getIndex(String toFind)
    {
        int i;
        for(i = 0; i < recipesList.size();i++)
        {
            if(recipesList.get(i).getName().equals(toFind))
                return i;
        }
        return i;
    }
    static public void remove(String toDelete)
    {
        for(int i = 0; i < recipesList.size();i++)
        {
            String path;
            if(toDelete.equals(recipesList.get(i).getName())) {
                path = WhatToCook.SelectedPackage.GetRecipesPath() + "/" + recipesList.get(i).getName();
                recipesList.remove(i);
                File fileToDelete = new File(path);
                fileToDelete.delete();
                return;
            }
        }
    }
    static public int size()
    {
        return recipesList.size();
    }
    static public String getRecipeNameAtIndex(int i)
    {
        return recipesList.get(i).getName();
    }
    static public boolean checkWithIngredientsList(ArrayList<Ingredient> aviableIngredients,int index,boolean parameters[],int ease,int time)
    {
        boolean contains;
        for(int i = 0; i < recipesList.get(index).getSize();i++)
        {
            contains = false;
            for (Ingredient aviableIngredient : aviableIngredients)
                if (recipesList.get(index).getIngredient(i).equals(aviableIngredient))
                    contains = true;

            if(!contains)
                return false;
        }
        if(recipesList.get(index).getParameters().getPreparingEase()<=ease && recipesList.get(index).getParameters().getPreparingTime()<=time) {
            for(int i = 0; i < 5;i++)
            {
                if(recipesList.get(index).getParameters().getParameters()[i]==true && parameters[i]==false)
                    return false;
            }
            return true;
        }
        else
            return false;

    }
    static public ArrayList<Recipe> recipesList;
}
