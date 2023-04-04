package com.example.allgasnobrakes.models;

public class SixBySixAVGenerator {
    public static String Generate() {
        String line1 = "            _________       ";
        String line2 = " ==========/       __|      ";
        String line3 = "     ______\\______/____    ";
        String line4 = "   /  __    __    __  |O   ";
        String line5 = "   \\_/  \\__/  \\__/  \\_|   ";
        String line6 = "     \\__/  \\__/  \\__/     ";

        String newLine = System.getProperty("line.separator");

        return line1.concat(newLine).concat(line2).concat(newLine)
                .concat(line3).concat(newLine).concat(line4).concat(newLine)
                .concat(line5).concat(newLine).concat(line6).concat(newLine);
    }

    public static String GenerateTank() {
        String line1 = "            ________        ";
        String line2 = " ==========/       _\\      ";
        String line3 = "      ____/_______|____    ";
        String line4 = "    / __    __    __  |O ";
        String line5 = "   /_/ O\\__/ O\\__/ O\\_|   ";
        String line6 = "      \\______________/       ";

        String newLine = System.getProperty("line.separator");

        return line1.concat(newLine).concat(line2).concat(newLine)
                .concat(line3).concat(newLine).concat(line4).concat(newLine)
                .concat(line5).concat(newLine).concat(line6).concat(newLine);
    }
}
