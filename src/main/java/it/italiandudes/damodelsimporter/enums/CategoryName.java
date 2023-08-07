package it.italiandudes.damodelsimporter.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public enum CategoryName {
    PLACE_OBJECTS("Place Objects"),
    CHARACTERS("Characters"),
    MONSTERS("Monsters"),
    NPCS("NPCs"),

    ;

    // Attributes
    @NotNull public static final ArrayList<String> CATEGORY_NAMES = new ArrayList<>();
    static {
        for (CategoryName categoryName : CategoryName.values()) {
            CATEGORY_NAMES.add(categoryName.getName());
        }
    }
    @NotNull private final String name;

    // Constructors
    CategoryName(@NotNull final String name) {
        this.name = name;
    }

    // Methods
    @NotNull
    public String getName() {
        return name;
    }
}
