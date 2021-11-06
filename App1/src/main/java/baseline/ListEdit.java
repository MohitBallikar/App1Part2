/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

public class ListEdit {
    //initialize a list of all the list names

public int addList(){
    //this function adds a new list to the gui
    //the function will ask for user input, which will be stored and used to create a new list
    //the input for the title will be checked to ensure that the input is > 3 characters
    return 0;
    }

    public int delList(){
        //this function deletes an existing list to the gui
        //the deletion will take place after the user inputs the title of the list that they want deleted
        //again, bad inputs will be ignored
        return 0;
    }

    public int editList(){
        //this function edits an existing list to the gui
        //will ask for user input in checking for the necessary list and editing its title
        return 0;
    }

    public int dispList(){
    //this function will ensure that the items within each list will be displayed within the GUI
        //will need to take the outputs for any user item deletion/creation as well as edits
        return 0;
    }

    public int dispCheck(){
    //this function will parse through the items in the list and only display the values that have not been marked by the checklist function
    return 0;
    }

    public int dispNotCheck(){
    //essentially performs the inverse of the above function
    //displays only the incompleted or not checked items
        return 0;
    }


    public int saveList(){
    //multiple selected lists will have the process below looped to ensure that they are stored in a single file
    //the list(s) shall be saved to a specific file path, a formatted text file
        //file path and name will be asked from the user
        //this input will most likely be within a while loop, waiting for a certain keystroke to end the names of the lists
        return 0;
    }

    public int loadList(){
    //will use the file path and name defined within saveList to bring up previously saved lists
    return 0;
}




}


/*
Each function will be called and utilized to ensure that the front-end user interaction is recognized
Button presses and checkboxes will have dynamic storage to a file, which will update the state of the data involved

 The values or displays done by each function will be solely viewable through the GUI

Storage of data may be done through the use of a text file

    May need to create a larger list that stores at least the names of the lists
    I/O and parsing will need to be done to read the data on the file as well as opening/closing it
 */