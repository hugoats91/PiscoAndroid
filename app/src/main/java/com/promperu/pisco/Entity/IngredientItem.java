package com.promperu.pisco.Entity;

public class IngredientItem {

    private int ingrId;
    private String ingrTitle, ingrIcon, ingrDescription;

    public IngredientItem(int ingrId, String ingrTitle, String ingrIcon, String ingrDescription) {
        this.ingrId = ingrId;
        this.ingrTitle = ingrTitle;
        this.ingrIcon = ingrIcon;
        this.ingrDescription = ingrDescription;
    }

    public int getIngrId() {
        return ingrId;
    }

    public String getIngrTitle() {
        return ingrTitle;
    }

    public String getIngrIcon() {
        return ingrIcon;
    }

    public String getIngrDescription() {
        return ingrDescription;
    }

}