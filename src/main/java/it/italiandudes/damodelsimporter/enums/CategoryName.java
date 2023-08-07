package it.italiandudes.damodelsimporter.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public enum CategoryName {
    NPCs,
    Creatures
    ;

    // Attributes
    @NotNull public static final ArrayList<String> CATEGORY_NAMES = new ArrayList<>();
    static {
        for (CategoryName categoryName : CategoryName.values()) {
            CATEGORY_NAMES.add(categoryName.name());
        }
    }
}
