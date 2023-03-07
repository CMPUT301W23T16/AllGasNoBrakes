package com.example.allgasnobrakes;
/**
 * geneartes a unique name
 * - 16 to the power of 6 unique possible names
 * @author zhaoyu5
 * @version 1.0
 */
public class NameGenerator {
    private String hashedQR;

    public NameGenerator(String hashedQR) {
        this.hashedQR = hashedQR;
    }

    public String Generate(){
        String[] bit1 = {"Frigid ","Freezing ","Cold ","Chilly ", "Brisk ","Cool ","Mild ","Warm ","Hot ", "Scorching ","Roasting ", "Blazing ", "Toasty ", "Sweltering ","Scalding ","Steaming "};
        String[] bit2 = {"Flo","Glo","Plo","Klo", "Tlo","Rlo","Qlo","Wlo","Slo", "Hlo","Blo", "Dlo", "Clo", "Mlo","Nlo","Vlo"};
        String[] bit3 = {"Fo","Go","Po","Ko", "To","Ro","Qo","Wo","So", "Ho","Bo", "Do", "Co", "Mo","No","Vo"};
        String[] bit4 = {"Big","Colossal","Gigantic","Huge", "Immense","Large","Little","Massive","Microscopic", "Miniature","Puny", "Small", "Tiny", "Teeny","Petite","Short"};
        String[] bit5 = {"Attractive","Bald","Beautiful","Chubby", "Clean","Dazzling","Elegant","Fancy","Handsome", "Gorgeous","Muscular", "Skinny", "Stocky", "Lazy","Scary","Grumpy"};
        String[] bit6 = {"Chicken","Bear","Bat","Crab", "Crow","Duck","Elephant","Fish","Hamster", "Gorilla","Moose", "Shark", "Snail", "Lobster","Scorpion","Tiger"};
        String name = bit1[Integer.parseInt(String.format("%c",hashedQR.charAt(0)), 16)];
        name += bit2[Integer.parseInt(String.format("%c",hashedQR.charAt(1)), 16)];
        name += bit3[Integer.parseInt(String.format("%c",hashedQR.charAt(2)), 16)];
        name += bit4[Integer.parseInt(String.format("%c",hashedQR.charAt(3)), 16)];
        name += bit5[Integer.parseInt(String.format("%c",hashedQR.charAt(4)), 16)];
        name += bit6[Integer.parseInt(String.format("%c",hashedQR.charAt(5)), 16)];
        return name;
    }
}