package com.example.allgasnobrakes;
/**
 * generates a unique name
 * - 16 to the power of 6 unique possible names
 * @author zhaoyu5 zhaoyu4
 * @version 2.0
 */
public class NameGenerator {
    public static String Generate(String hashedQR) {
        String[] bit1 = {"Frigid ","Freezing ","Cold ","Chilly ", "Brisk ","Cool ","Mild ","Warm ","Hot ", "Scorching ","Roasting ", "Blazing ", "Toasty ", "Sweltering ","Scalding ","Steaming "};
        String[] bit2 = {"Flo","Glo","Plo","Klo", "Tlo","Rlo","Qlo","Wlo","Slo", "Hlo","Blo", "Dlo", "Clo", "Mlo","Nlo","Vlo"};
        String[] bit3 = {"Fo","Go","Po","Ko", "To","Ro","Qo","Wo","So", "Ho","Bo", "Do", "Co", "Mo","No","Vo"};
        String[] bit4 = {"Big","Colossal","Gigantic","Huge", "Immense","Large","Little","Massive","Microscopic", "Miniature","Puny", "Small", "Tiny", "Teeny","Petite","Short"};
        String[] bit5 = {"Attractive","Bald","Beautiful","Chubby", "Clean","Dazzling","Elegant","Fancy","Handsome", "Gorgeous","Muscular", "Skinny", "Stocky", "Lazy","Scary","Grumpy"};
        String[] bit6 = {"Tesla", "BMW", "Ferrari", "Ford", "Porsche", "Honda", "Lamborghini", "Toyota", "Bentley","Maserati", "Audi", "lexus","Acura","Rolls Royce", "Mercedes-Benz","Bugatti"};
        String name = bit1[Integer.parseInt(String.format("%c",hashedQR.charAt(0)), 16)];
        name += bit2[Integer.parseInt(String.format("%c",hashedQR.charAt(1)), 16)];
        name += bit3[Integer.parseInt(String.format("%c",hashedQR.charAt(2)), 16)];
        name += bit4[Integer.parseInt(String.format("%c",hashedQR.charAt(3)), 16)];
        name += bit5[Integer.parseInt(String.format("%c",hashedQR.charAt(4)), 16)];
        name += bit6[Integer.parseInt(String.format("%c",hashedQR.charAt(5)), 16)];
        return name;
    }
}