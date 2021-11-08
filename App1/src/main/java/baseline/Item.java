/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Simple model class for the Item table.
//Getter and setter used to simplify calling for values, testing this can be seen through the controller
public class Item {

    private final StringProperty itemName;
    private final StringProperty itemDescription;
    private final StringProperty itemDueDate;
    private final StringProperty itemStatus;
    private final BooleanProperty isComplete;
//deinfes the attribute of the item with its parameters listed
    public Item(String itemName, String itemDescription, String itemDueDate, String itemStatus , boolean isComplete)
    {
        this.itemName = new SimpleStringProperty(itemName);
        this.itemDescription = new SimpleStringProperty(itemDescription);
        this.itemDueDate = new SimpleStringProperty(itemDueDate);
        this.itemStatus = new SimpleStringProperty(itemStatus);
        this.isComplete = new SimpleBooleanProperty(isComplete);
    }

//getters and setters below
    public String getItemName() {
        return itemName.get();
    }
    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }
    public StringProperty itemNameProperty() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription.get();
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription.set(itemDescription);
    }
    public StringProperty itemDescriptionProperty() {
        return itemDescription;
    }

    public String getItemDueDate() {
        return itemDueDate.get();
    }
    public void setItemDueDate(String itemDueDate) {
        this.itemDueDate.set(itemDueDate);
    }
    public StringProperty itemDueDateProperty() {
        return itemDueDate;
    }

    public String getItemStatus() {
        return itemStatus.get();
    }
    public void setItemStatus(String itemStatus) {
        this.itemStatus.set(itemStatus);
    }
    public StringProperty itemStatusProperty() {
        return itemStatus;
    }


    public boolean getIsComplete() {
        return isComplete.get();
    }
    public void setIsComplete(boolean isComplete) {
        this.isComplete.set(isComplete);
    }
    public BooleanProperty isCompleteProperty() {
        return isComplete;
    }


//concatenation for the overall output of the string with item information
    @Override
    public String toString() {
        return "Item [itemName=" + itemName + ", itemDescription=" + itemDescription + " , itemDueDate=" + itemDueDate + ", itemStatus=" + itemStatus + ", isComplete=" + isComplete + "]";
    }

}
/*
Provide values for the controller and other aspects to call from to avoid redundancy

 */