package core;

import auxiliary.LanguagePackage;
import gui.MainWindow;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Mateusz on 09.03.2016.
 * Project WhatToCook
 */
/*
    KLASA PODSTAWOWA, URUCHOMIENIOWA, SŁUŻY DO UTWORZENIA GŁÓWNEGO OKNA PROGRAMU I PACZEK JĘZYKOWYCH
    WCZYTUJE PLIK KONFIGURACYJNY "cfg"
 */
public class WhatToCook implements Runnable {

    public static void main(String[] args) {
        new WhatToCook();
    }
    public WhatToCook() {

        selectedLanguagePack = new ArrayList<>();
        buildPolishLanguage();
        buildEnglishLanguage();
        selectedLanguagePack = polishLanguagePack;
        PolishPackage = new LanguagePackage("data/ingredientsPL","data/recipesPL",polishLanguagePack);
        EnglishPackage = new LanguagePackage("data/ingredientsENG","data/recipesENG",englishLanguagePack);
        SelectedPackage = new LanguagePackage();

        Scanner in;
        try
        {
            in = new Scanner(new File("data/cfg"));
            String language = in.next();
            if(language.equals("english")) {
                SelectedPackage = EnglishPackage;
            }
            if(language.equals("polish")) {
                SelectedPackage = PolishPackage;
            }
            MainWindow.getToNewCard = in.nextBoolean();
            MainWindow.autoLoadIngredients = in.nextBoolean();
        }
        catch (FileNotFoundException | NoSuchElementException e)
        {
            System.err.println("Error during loading 'cfg' file, program will load with default settings");
        }

        frame = new MainWindow();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);


        new Thread(this).start();
    }




    public void run()
    {

    }
    private void buildEnglishLanguage()
    {
        englishLanguagePack = new ArrayList<>();
        englishLanguagePack.add("File");
        englishLanguagePack.add("Exit");
        englishLanguagePack.add("Edit");
        englishLanguagePack.add("Clear inserted ingredients");
        englishLanguagePack.add("Clear search results");
        englishLanguagePack.add("Help");
        englishLanguagePack.add("Options");
        englishLanguagePack.add("About");
        englishLanguagePack.add("Search");
        englishLanguagePack.add("Recipes Database");
        englishLanguagePack.add("Insert ingredients");
        englishLanguagePack.add("Choose ingredients:");
        englishLanguagePack.add("Add ingredients");
        englishLanguagePack.add("Remove ingredients");
        englishLanguagePack.add("Founds receipes");
        englishLanguagePack.add("Search for receipes");
        englishLanguagePack.add("Search");
        englishLanguagePack.add("New Recipe");
        englishLanguagePack.add("Edit Recipe");
        englishLanguagePack.add("Insert Recipe Name");
        englishLanguagePack.add("Write making instrictions below");
        englishLanguagePack.add("Save and Exit");
        englishLanguagePack.add("Exit without saving");
        englishLanguagePack.add("Close the Recipe");
        englishLanguagePack.add("Auto-Move to the new tab");
        englishLanguagePack.add("Language");
        englishLanguagePack.add("General");
        englishLanguagePack.add("Ingredients");
        englishLanguagePack.add("Ingredient Name:");
        englishLanguagePack.add("Add");
        englishLanguagePack.add("Remove");
        englishLanguagePack.add("Remove recipe");
        englishLanguagePack.add("Check input ingredients, name and preparing instructions. Maybe the same recipe is already in the database");
        englishLanguagePack.add("Recipe Error");
        englishLanguagePack.add("Insert recipe name:");
        englishLanguagePack.add("About");
        englishLanguagePack.add("OK");
        englishLanguagePack.add("Authors");
        englishLanguagePack.add("This ingredients can't be deleted, cause it is used in recipes:");
        englishLanguagePack.add("Deleting ingredients error");//39 index
        englishLanguagePack.add("Export Ingredients");
        englishLanguagePack.add("Inport Ingredients");
        englishLanguagePack.add("Are you sure? It will cause restart.");
        englishLanguagePack.add("Confirmation");
        englishLanguagePack.add("Export Recipe");
        englishLanguagePack.add("New");
        englishLanguagePack.add("Ingredient");
        englishLanguagePack.add("Recipe");
        englishLanguagePack.add("Ammount:");
        englishLanguagePack.add("Unit:");
        englishLanguagePack.add("Dish for:");//51
        englishLanguagePack.add("Breakfest");
        englishLanguagePack.add("Dessert");
        englishLanguagePack.add("Diner");
        englishLanguagePack.add("Supper");
        englishLanguagePack.add("Snack");
        englishLanguagePack.add("Preparing time:");
        englishLanguagePack.add("Preparing ease:");
        englishLanguagePack.add("Parameters");
        englishLanguagePack.add("Quick");//59
        englishLanguagePack.add("Average");
        englishLanguagePack.add("Long");
        englishLanguagePack.add("Easy");
        englishLanguagePack.add("Average");
        englishLanguagePack.add("Difficult");
        englishLanguagePack.add("Recipe: ");
        englishLanguagePack.add("Instructions: ");
        englishLanguagePack.add("The change of language will cause change of database.");
        englishLanguagePack.add("Import");
        englishLanguagePack.add("Export");
        englishLanguagePack.add("Importing ingredients error");
        englishLanguagePack.add("Following ingredients was not imported, cause they are not in the database");
        englishLanguagePack.add("Ingredients");
        englishLanguagePack.add("Save program state:");
        englishLanguagePack.add("Path: ");
        englishLanguagePack.add("Change path");
        englishLanguagePack.add("It was impossible to read the file with owned ingredients, check its path in the options");
        englishLanguagePack.add("Creating recipe error");
        englishLanguagePack.add("You can create/edit only one recipe in the time");

    }

    private void buildPolishLanguage() {
        polishLanguagePack = new ArrayList<>();
        polishLanguagePack.add("Plik");
        polishLanguagePack.add("Zakończ");
        polishLanguagePack.add("Edycja");
        polishLanguagePack.add("Wyczyść wprowadzone składniki");
        polishLanguagePack.add("Wyczyść wyniki wyszukiwania");
        polishLanguagePack.add("Pomoc");
        polishLanguagePack.add("Opcje");
        polishLanguagePack.add("O Programie");
        polishLanguagePack.add("Wyszukiwanie");
        polishLanguagePack.add("Baza Przepisów");
        polishLanguagePack.add("Wprowadź składniki");
        polishLanguagePack.add("Wybierz składniki:");
        polishLanguagePack.add("Dodaj składnik");
        polishLanguagePack.add("Usuń składnik");
        polishLanguagePack.add("Znalezione przepisy");
        polishLanguagePack.add("Szukaj przepisów");
        polishLanguagePack.add("Wyszukaj");
        polishLanguagePack.add("Nowy Przepis");
        polishLanguagePack.add("Edytuj Przepis");
        polishLanguagePack.add("Podaj nazwę przepisu");
        polishLanguagePack.add("Napisz instrukcję przygotowania posiłku");
        polishLanguagePack.add("Zapisz i wyjdź");
        polishLanguagePack.add("Wyjdź bez Zapisywania");
        polishLanguagePack.add("Zamknij przepis");
        polishLanguagePack.add("Automatyczne przechodzenie do nowej karty");
        polishLanguagePack.add("Język");
        polishLanguagePack.add("Ogólne");
        polishLanguagePack.add("Składniki");//27
        polishLanguagePack.add("Nazwa Składnika:");
        polishLanguagePack.add("Dodaj");
        polishLanguagePack.add("Usuń");
        polishLanguagePack.add("Usuń przepis");
        polishLanguagePack.add("Sprawdź czy podałeś składniki, nazwę i instrukcję przygotowania. Być może taki przepis już jest w bazie");
        polishLanguagePack.add("Błąd Przepisu");
        polishLanguagePack.add("Podaj nazwę przepisu:");
        polishLanguagePack.add("O programie");
        polishLanguagePack.add("OK");
        polishLanguagePack.add("Autorzy");
        polishLanguagePack.add("Ten składnik nie może być usunięty ponieważ jest wykorzystywany w przepisach na:");
        polishLanguagePack.add("Błąd usuwania składnika");
        polishLanguagePack.add("Eksportuj składniki");
        polishLanguagePack.add("Importuj składniki");
        polishLanguagePack.add("Jesteś pewien? Spowoduje to restart programu.");
        polishLanguagePack.add("Potwierdzenie");
        polishLanguagePack.add("Eksportuj przepis");
        polishLanguagePack.add("Nowy");
        polishLanguagePack.add("Składnik");
        polishLanguagePack.add("Przepis");
        polishLanguagePack.add("Ilość:");
        polishLanguagePack.add("Jednostka:");
        polishLanguagePack.add("Danie na:");//50
        polishLanguagePack.add("Śniadanie");//51
        polishLanguagePack.add("Deser");
        polishLanguagePack.add("Obiad");
        polishLanguagePack.add("Kolację");
        polishLanguagePack.add("Przekąskę");
        polishLanguagePack.add("Czas przygotowywania:");
        polishLanguagePack.add("Łatwość przygotowania:");
        polishLanguagePack.add("Parametry");
        polishLanguagePack.add("Szybko");
        polishLanguagePack.add("Średnio");
        polishLanguagePack.add("Wolno");
        polishLanguagePack.add("Łatwe");
        polishLanguagePack.add("Średnie");
        polishLanguagePack.add("Trudne");
        polishLanguagePack.add("Przepis: ");//65
        polishLanguagePack.add("Wykonianie: ");
        polishLanguagePack.add("Zmiana przepisu spowoduje zmianę bazy danych przepisów.");
        polishLanguagePack.add("Importuj");
        polishLanguagePack.add("Eksportuj");
        polishLanguagePack.add("Błąd dodawania składników");
        polishLanguagePack.add("Poniższe składniki nie zostały zaimportowane, gdyż nie znajdują się w bazie danych");
        polishLanguagePack.add("Posiadane składniki");//72
        polishLanguagePack.add("Zapisuj stan programu:");
        polishLanguagePack.add("Ścieżka:");
        polishLanguagePack.add("Zmień ścieżkę");
        polishLanguagePack.add("Błąd importowania");
        polishLanguagePack.add("Nie można było odczytać pliku z posiadanymi składnikami, sprawdź jego śnieżkę w opcjach");
        polishLanguagePack.add("Błąd tworzenia przepisu");
        polishLanguagePack.add("W jednym momencie możesz tworzyć/edytować tylko jeden przepis");
    }
    private static ArrayList<String> polishLanguagePack;
    private static ArrayList<String> englishLanguagePack;
    private static ArrayList<String> selectedLanguagePack;
    public static MainWindow frame;
    public static String version = "1.5";

    public static LanguagePackage SelectedPackage;

    public static LanguagePackage PolishPackage;
    public static LanguagePackage EnglishPackage;

    //public static RecipesList recipesDatabase;
}
