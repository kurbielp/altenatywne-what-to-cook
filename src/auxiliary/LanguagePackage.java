package auxiliary;

import java.util.ArrayList;

/**
 * Created by Mateusz on 23.04.2016.
 * Project WhatToCook
 */
/*
    ZARZĄDZA OBSŁUGĄ JĘZYKÓW W CAŁYM PROGRAMIE
 */
public class LanguagePackage
    {
        public LanguagePackage()
        {
            ingredientsPath = "";
            recipesPath = "";
            language = new ArrayList<String>();
        }
        public LanguagePackage(String ingredientsPath, String recipesPath,ArrayList<String> language)
        {
            this.ingredientsPath = ingredientsPath;
            this.recipesPath = recipesPath;
            this.language = language;
        }

        public String GetIngredientsPath()
        {
            return ingredientsPath;
        }
        public String get(int i)
        {
            return language.get(i);
        }
        public String GetRecipesPath()
        {
            return recipesPath;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LanguagePackage that = (LanguagePackage) o;

            if (ingredientsPath != null ? !ingredientsPath.equals(that.ingredientsPath) : that.ingredientsPath != null)
                return false;
            if (recipesPath != null ? !recipesPath.equals(that.recipesPath) : that.recipesPath != null) return false;
            return language != null ? language.equals(that.language) : that.language == null;

        }

        @Override
        public int hashCode() {
            int result = ingredientsPath != null ? ingredientsPath.hashCode() : 0;
            result = 31 * result + (recipesPath != null ? recipesPath.hashCode() : 0);
            result = 31 * result + (language != null ? language.hashCode() : 0);
            return result;
        }

        private String ingredientsPath;
        private String recipesPath;
        private ArrayList<String> language;
    }
