package com.example.allgasnobrakes.models;

/**
* generates a unique visual representation
* @author zhaoyu4
* @version 2.0
*/
public class CarGenerator {
    public static String Generate(String hashedQR) {
      String line1;
      String line2;
      String line3;
      String line4;
      String line5;
      String line6;

      // type: 0 = Car, 1 = SUV
      if (Integer.parseInt(String.format("%c", hashedQR.charAt(0)), 16) % 2 == 0) {
          line1 = "       _______      ";
          line2 = "      //  ||\\ \\      ";
          line3 = "_____//___||_\\ \\___  ";
          line4 = ")  _          _    \\ ";
          line5 = "|_/ \\________/ \\___|  ";
          line6 = "  \\_/        \\_/     ";
      } else {
          line1 = "       ____________ ";
          line2 = "      //  ||  ||  || ";
          line3 = "_____//___||__||__|| ";
          line4 = ")  _          _    | ";
          line5 = "|_/ \\________/ \\___|  ";
          line6 = "  \\_/        \\_/     ";
      }
      // shaker scoop: 0 = no shaker scoop, 1 = shaker scoop
      if (Integer.parseInt(String.format("%c", hashedQR.charAt(1)), 16) % 2 == 1) {
          line3 = line3.substring(0,2)+"m"+line3.substring(3,20);
      }

      // siren lights: 0 = no siren lights, 1 = siren lights
      if (Integer.parseInt(String.format("%c", hashedQR.charAt(2)), 16) % 2 == 1) {
          line1 = line1.substring(0,8)+"o"+line1.substring(9,20);
      }

      // exhaust pipe: 0 = no exhaust pipe, 1 = exhaust pipe
      if (Integer.parseInt(String.format("%c", hashedQR.charAt(3)), 16) % 2 == 1) {
          line5 = line5.substring(0,20)+"=3";
      }

      // bone line: 0 = no bone line, 1 = bone line
      if (Integer.parseInt(String.format("%c", hashedQR.charAt(4)), 16) % 2 == 1) {
          line4 = line4.substring(0,1)+"-"+line4.substring(2,5)+"--------"+line4.substring(13,16)+"--"+line4.substring(18,20);
      }

      // road: 0 = no road, 1 = road
      if (Integer.parseInt(String.format("%c", hashedQR.charAt(5)), 16) % 2 == 1) {
          line6 = "__"+line6.substring(2,5)+"________"+line6.substring(13,16)+"_____";
      }

      String newLine = System.getProperty("line.separator");
      String car = line1.concat(newLine).concat(line2).concat(newLine)
              .concat(line3).concat(newLine).concat(line4).concat(newLine)
              .concat(line5).concat(newLine).concat(line6).concat(newLine);


      return car;
 
    }
}
