package PageObjects;

import java.util.Random;

public class CommonLib {
    /**
     //#########################################################################
     //# Page Name: CommonItems
     //# Description: This class will provide common support to all pages
     //# -----------------------------------------------------------------------
     //# Objective: This class will store all common elements and will have generic
     //#            methods
     //# -----------------------------------------------------------------------
     //# Created By: Varsha Singh
     //# Created Dated: 20-Aug-2021
     //# -----------------------------------------------------------------------
     //# History:
     //#########################################################################
     */



    public String generateRandString(int targetStringLength){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }


}
