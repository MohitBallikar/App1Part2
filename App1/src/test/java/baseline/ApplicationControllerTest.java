/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationControllerTest {
    @Test
    public void createItemTest() {
        ApplicationController app = new ApplicationController();
        String expected = "Test";
        String[] a = {"Test","TEST1","2020-12-05","Complete","true"};
        Item actual = app.createItem(a);

        assertEquals(expected,actual.getItemName());
    }

    @Test
    public void LoadDataFileTest() {
        ApplicationController app = new ApplicationController();
        int expected = 5;
        List<Item> a = app.LoadDatafromFileToCollections("data/Item.txt");
        assertEquals(expected,a.size());
    }





}