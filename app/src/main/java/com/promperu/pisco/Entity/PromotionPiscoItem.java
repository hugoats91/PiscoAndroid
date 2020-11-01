package com.promperu.pisco.Entity;

public class PromotionPiscoItem {

    private String promImage;
    private String promTitle;
    private String promPromotion;
    private String promSubTitle;

    public PromotionPiscoItem(String promImage, String promTitle, String promPromotion, String promSubTitle) {
        this.promImage = promImage;
        this.promTitle = promTitle;
        this.promPromotion = promPromotion;
        this.promSubTitle = promSubTitle;
    }

    public String getPromImage() {
        return promImage;
    }

    public String getPromTitle() {
        return promTitle;
    }

    public String getPromPromotion() {
        return promPromotion;
    }

    public String getPromSubTitle() {
        return promSubTitle;
    }

}