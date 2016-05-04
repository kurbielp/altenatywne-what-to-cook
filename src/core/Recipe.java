package core;

import auxiliary.PairAmountUnit;
import auxiliary.RecipeParameters;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Mateusz on 23.03.2016.
 * Project WhatToCook
 */
/*
    IMPLEMENTUJE COMPARABLE W CELU ALFABETYCZNEGO SORTOWANIA PRZEPISÓW
 */
public class Recipe implements Comparable<Recipe>
{
    public Recipe(String name, ArrayList<Ingredient> ingredients, ArrayList<PairAmountUnit> ingredientsAmountAndUnits, String instructions,RecipeParameters parameters)
    {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.ingredientsAmountAndUnits = ingredientsAmountAndUnits;
        this.parameters = parameters;
    }
    public String getName()
    {
        return name;
    }
    public String getRecipe()
    {
        return instructions;
    }
    public Ingredient getIngredient(int i)
    {
        return ingredients.get(i);
    }
    public String getAmmount(int i){return ingredientsAmountAndUnits.get(i).getAmmount();}
    public String getUnit(int i){return ingredientsAmountAndUnits.get(i).getUnit();}
    public RecipeParameters getParameters() {return parameters;}
    public int getSize(){return ingredients.size();}
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<PairAmountUnit> ingredientsAmountAndUnits;
    private String instructions;
    private RecipeParameters parameters;

    @Override
    public int compareTo(Recipe o) {
        Collator c = Collator.getInstance(Locale.getDefault());
        return c.compare(this.getName(),o.getName());

    }
}
