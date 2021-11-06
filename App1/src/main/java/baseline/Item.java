package baseline;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Simple model class for the Item table.

public class Item {

    private final StringProperty itemName;
    private final StringProperty itemDescription;
    private final StringProperty itemDueDate;
    private final StringProperty itemStatus;
    private final BooleanProperty isComplete;

    public Item(String itemName, String itemDescription, String itemDueDate, String itemStatus , boolean isComplete)
    {
        this.itemName = new SimpleStringProperty(itemName);
        this.itemDescription = new SimpleStringProperty(itemDescription);
        this.itemDueDate = new SimpleStringProperty(itemDueDate);
        this.itemStatus = new SimpleStringProperty(itemStatus);
        this.isComplete = new SimpleBooleanProperty(isComplete);
    }


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
    public BooleanProperty isDeleteProperty() {
        return isComplete;
    }


    @Override
    public String toString() {
        return "Item [itemName=" + itemName + ", itemDescription=" + itemDescription + " , itemDueDate=" + itemDueDate + ", itemStatus=" + itemStatus + ", isComplete=" + isComplete + "]";
    }

}

