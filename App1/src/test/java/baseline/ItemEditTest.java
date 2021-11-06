/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemEditTest {
    void addItemTest(){
        //ensure that the description and due date exist
        //Compares the items within a list to those that are there within the gui (assert)
        //would function like the ListTest counterpart, but on a smaller level
        //random values could be created/deleted and this could be compared to values that are actually created/deleted
    }

    void delItemTest(){
        //logically functions the same as addItemTest
        //deletion in comparison would be held as a change in a list which would be seen the same as creation
        //random values could be created/deleted and this could be compared to values that are actually created/deleted
    }

//ItemEdit's functionality could just be broken down into these two functions, in terms of testing
    //the existence of such lists containing items would also be similar in functionality

    void editItemDueTest(){
        //change due date's and use the changes to track differences between actual and expected and vice-a-versa
    //the due date format can be tested here
    }
    void editItemDescTest(){
        //again would work the same in changing the due date of the items
        //would draw upon the above tests in ensuring that the item description actually exists
    }

    void checkItemTest(){
        //ensure that any updated values or outputted values would only be those that are checked
        //a return value within the original function can be used to validate whether the "checking" is being done for each item
    }


    void saveItemTest(){
        //looped version of the List test at a smaller scale, or i can make the list test to be a larger version of this test
        //checks the file path and file name for the required lists and items that would be saved
        //will compare the path as necessary to make sure that the path is correct as well as the file stored at said path
        //the save lost function could also possess a return value/prompt that confirms that the file was written
        //this return value/prompt would be used for comparison to ensure that the items(s) are saved correctly
    }

    void loadItemTest(){
        //function similarly to saveListTest, could also use the return value comparison
        //also could just test the loading capability of the save file and ensuring that the data is as expected
        //implement data parsing for this to work
    }


/*
Tests will also help as a framework to write the methods around
 */



}