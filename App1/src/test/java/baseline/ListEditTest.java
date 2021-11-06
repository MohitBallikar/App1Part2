/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListEditTest {

    void addListTest(){
        //double-check if the length parameters are met here
        //Compares lists that are there within the gui to those that are actually there
        //a list of list titles can be compared using assertions to test if everything is as expected
    }

    void delListTest(){
        //logically functions the same as addListTest
        //deletion in comparison would be held as a change in a list which would be seen the same as creation
        //could also test to make sure that at least one item exists within this list?
        //if items are created for this list^
    }

    void editListTest(){
        //again would work the same in changing the title of the lists, if a list of total lists is used
        //would draw upon the above tests in ensuring that the list (title-based) actually exists
    }

    void dispCheckTest(){
        //ensure that any updated values or outputted values would only be those that are checked
    }
    void dispNotCheckTest(){
        //would function similarly to the above test but the base case would be items that exist rather than not existing or vice a versa
    }

    void saveListTest(){
        //checks the file path and file name for the required lists that would be saved
        //will compare the path as necessary to make sure that the path is correct as well as the file stored at said path
        //the save lost function could also possess a return value/prompt that confirms that the file was written
        //this return value/prompt would be used for comparison to ensure that the list(s) are saved correctly
    }

    void loadListTest(){
        //function similarly to saveListTest, could also use the return value comaparison
        //also could just test the loading capability of the save file and ensureing that the data is as expected
        //implement data parsing for this to work
    }


/*
Tests will also help as a framework to write the methods around
 */

}