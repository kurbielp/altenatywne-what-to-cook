package core;

import com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Mateusz on 23.03.2016.
 * Project WhatToCook
 */
/*
    IMPLEMENTUJE COMPARABLE W CELU ALFABETYCZNEGO SORTOWANIA SKLADNIKÓW
 */
public class Ingredient implements Comparable<Ingredient> {
String altname;

    public Ingredient(String name, String alternativeName) {
        altname = alternativeName;
        this.name = name;

    }
    public String getAltname() {return altname;}

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    private String name;

    @Override
    public int compareTo(Ingredient o) {
        Collator c = Collator.getInstance(Locale.getDefault());
        if (c.compare(this.getName(), o.getName()) == 0) {
            return c.compare(this.getName(), o.getName());
        } else if (c.compare(this.getName(), altname) == 0) {
            return c.compare(this.getName(), altname);
        } else {
            return c.compare(this.getName(), o.getName());
        }
    }
}
