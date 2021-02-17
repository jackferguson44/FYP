package uk.ac.tees.t7099806.mediatracker2;

public class AddS {

    public String original;

    public AddS(String original)
    {
        this.original = original;
    }

    public String add()
    {
        String withS = "s";
        int index = 3;

        String newString = new String();

        for (int i = 0; i < original.length(); i++)
        {
            newString += original.charAt(i);

            if(i == index)
            {
                newString += withS;
            }
        }

        return newString;

    }
}
