/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

public class ItemEdit extends ListEdit{
//variable initialization or reader setup would begin here

    public int addItem(){
        //this function adds a new item to an exiting to do list
        //the function will ask for user input, which will be stored and used to create a new item within the respective list that is defined
        return 0;
    }

    public int delItem(){
        /*
        this function deletes an existing item from an exiting to do list
        the deletion will take place after the user inputs the title of the list that they want deleted, as well as the item
        again, bad inputs will be ignored
         */
        return 0;
    }

    public int editItem(){
        /*
        this function edits an existing item from a list
        will ask for user input in checking for the necessary item within the list
        will branch out into calling for either description or due date of the items
        will ensure bad inputs are not allowed
         */
        return 0;
    }

    public int editItemDesc(){
        //the values will be edited based on the user-desired names/inputs
        //will be called if item description needs to changed
    return 0;
    }

    public int editItemDue(){
        //work similarly to the editItemDesc function, except for a separate field
        //will be called if the item due date needs to changed
        return 0;
    }

    public int checkItem(){
        //will check if an item is complete
        //this value for an item will be handled for the filtering that will be done by other functions
        //could output said value for comparison and testing purposes
        return 0;
    }

    public int saveItem(){
        //function will individually save all the items within a loop for the length of the respective list that these items are saved under
        return 0;
    }

}
