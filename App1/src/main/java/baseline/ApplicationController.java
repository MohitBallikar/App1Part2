/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//View-Controller for the Item table.

public class ApplicationController  {
//initialize the parts of the gui that are seen for the user as well as within the fxml file
    @FXML
    private TextField filterField;
    @FXML
    public TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, String> itemDescriptionColumn;
    @FXML
    private TableColumn<Item, String> itemDueDateColumn;
    @FXML
    private TableColumn<Item, String> itemStatusColumn;
    @FXML
    private TableColumn<Item, Boolean> isCompleteColumn;

    @FXML private TextField itemNameField;
    @FXML private TextField itemDescriptionField;
    @FXML private DatePicker itemDueDateField;
    @FXML private CheckBox itemStatusField;
    @FXML
    private CheckBox checkBox;
    private Stage controllerStage;
    private ObservableList<Item> masterData = FXCollections.observableArrayList();
    private static boolean chck;
    //default file path that can be changed by the user
    private static final String ITEMS_FILE_NAME = "data/Item2.txt";

    /*
      Just add some sample data in the constructor.
     */
    public ApplicationController() {

        List<Item> items = LoadDatafromFileToCollections(ITEMS_FILE_NAME);
        for (Item b : items)
        {
            masterData.add(new Item( b.getItemName(), b.getItemDescription(), b.getItemDueDate(), b.getItemStatus(), false));
        }

    }

    /*
     Initializes the controller class. This method is automatically called
     after the fxml file has been loaded.

     Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {

        //Add Tool Tips, for each field.
        Tooltip itemNameToolTip = new Tooltip("Add an Item");
        itemNameToolTip.setFont(Font.font("Verdana", 10));
        itemNameField.setTooltip(itemNameToolTip);

        Tooltip itemDescToolTip = new Tooltip("Add an Item Description (1 to 256 Chars)");
        itemDescToolTip.setFont(Font.font("Verdana", 10));
        itemDescriptionField.setTooltip(itemDescToolTip);

        Tooltip itemDueDateToolTip = new Tooltip("Select a Due Date (Optional)");
        itemDueDateToolTip.setFont(Font.font("Verdana", 10));
        itemDueDateField.setTooltip(itemDueDateToolTip);

        Tooltip itemStatusToolTip = new Tooltip("Mark Item Complete or Pending");
        itemStatusToolTip.setFont(Font.font("Verdana", 10));
        itemStatusField.setTooltip(itemStatusToolTip);


        itemTable.getSelectionModel().cellSelectionEnabledProperty().set(true);

        // 0. Initialize the columns.
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

        //allows for user alteration and initialization
        itemDescriptionColumn.setEditable(true);
        itemDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        itemDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory<Item, String> countryCellValueFactory = new PropertyValueFactory<>("itemDescription");
        itemDescriptionColumn.setCellValueFactory(countryCellValueFactory);
        itemDueDateColumn.setCellValueFactory(cellData -> cellData.getValue().itemDueDateProperty());
        itemDueDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemStatusColumn.setCellValueFactory(cellData -> cellData.getValue().itemStatusProperty());
        isCompleteColumn.setCellValueFactory(cellData->cellData.getValue().isCompleteProperty());
        isCompleteColumn.setCellFactory(cellData ->new CheckBoxTableCell<Item, Boolean>());

        itemTable.getColumns().setAll(itemNameColumn, itemDescriptionColumn,itemDueDateColumn,itemStatusColumn, isCompleteColumn);
        itemTable.setEditable(true);

        //Only Update the Item Description.

        //Modifying the Description property


    //filtering and sorting

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Item> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                // If filter text is empty, display all items.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getItemStatus().toLowerCase().trim().contains(lowerCaseFilter))
                {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Item> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(itemTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        itemTable.setItems(sortedData);
    }

//defines and initializes the stage
    public void setStage(Stage stg)
    {
        controllerStage = stg;
    }
//checkbox defined
    @FXML
    void checkBoxInitialize(ActionEvent event) {
        chck = checkBox.isSelected() ? true : false;
        for (Item n : masterData) {
            n.setIsComplete(chck);
        }
    }
//load function
    public void LoadDatafromFileToCollections()
    {
        List<Item> items = LoadDatafromFileToCollections(ITEMS_FILE_NAME);


    }

    //reads filename and provides respective items
    public  List<Item> LoadDatafromFileToCollections(String fileName)
    {
        List<Item> items = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII))
        {
            String line = br.readLine();
            // loop until all lines are read
            while (line != null)
            {
                String[] attributes = line.split("\\|");

                Item itm = createItem(attributes);
                // adding Item into ArrayList
                items.add(itm);

                // read next line before looping // if end of file reached, line would be null
                line = br.readLine();
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return items;
    }

    //creates items with their fields as seen in the table
    public static Item createItem(String[] metadata)
    {
        String itemname = metadata[0];
        String itemdescription = metadata[1];
        String itemdate = metadata[2];
        String status = metadata[3];

        boolean isDelete = false;

        // create and return book of this metadata
        if ( status.equals("Complete"))
        {
            isDelete = true;
        }
        return new Item(itemname, itemdescription,itemdate, status, isDelete );
    }
    //reset the name, desc, and checkbox
    @FXML
    protected void resetFields(ActionEvent event)
    {
        itemNameField.setText("");
        itemDescriptionField.setText("");
        itemStatusField.setSelected(false);
    }


    //loads the data using the previous function, but with more user control
    @FXML
    protected void loadDataItem(ActionEvent event)
    {
        boolean bStatusFlag  = false;
        String filename = "";
        ObservableList<Item> reloadData = FXCollections.observableArrayList();

        Alert a =
                new Alert(Alert.AlertType.CONFIRMATION,
                        "Do you want to reload the data from the File.?", ButtonType.YES,
                        ButtonType.NO);
        Optional<ButtonType> confirm = a.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Item Data File");
            File selectedFile = fileChooser.showOpenDialog(controllerStage);
            if (selectedFile != null) {
                System.out.println("File PATH " + selectedFile);
                filename = selectedFile.getPath();
            }
            else
            {
                System.out.println("File not found");

            }

            System.out.println("File name " + filename);

            List<Item> items = LoadDatafromFileToCollections(filename);

            System.out.println("Size of items "+ items.size());

            for (Item b : items)
            {
                if ( b.getItemStatus().equals("Complete"))
                {
                    bStatusFlag = true;
                }
                reloadData.add(new Item( b.getItemName(), b.getItemDescription(), b.getItemDueDate(), b.getItemStatus(), bStatusFlag));
            }

            itemTable.setItems(reloadData);
        }
    }


    //adds an item to the table
    @FXML
    protected void addItem(ActionEvent event)
    {

        String sDateField = "";
        String sStatusField = "Pending";
        Boolean bStatusField = false;
        Boolean bUniqueItemExists = true;

        if ( itemDueDateField != null) {
            LocalDate date = itemDueDateField.getValue();
            if ( date != null) {
                sDateField = date.toString();
                if (sDateField.equals("") || sDateField == null) {
                    sDateField = "9999-99-99";
                }
            }
        }
        else
            sDateField = " ";


        if (itemStatusField != null) {
            if (itemStatusField.isSelected()) {
                bStatusField = true;
                sStatusField = "Complete";
            }
        }

        //Check if the Description > 256
        if (itemDescriptionField.getText().length() > 256 )
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Length Exceeded");
            alert.setHeaderText("Maximum length = 256");
            alert.setContentText("Please limit the Description to 256 chars only.");
            alert.showAndWait();
        }
        else if (itemDescriptionField.getText().equals("") || itemDescriptionField.getText().length() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Item Description");
            alert.setHeaderText("Item Description is empty");
            alert.setContentText("Please input a valid item description.");
            alert.showAndWait();
        }

        else if (itemNameField.getText().equals("") || itemNameField.getText().length() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Item");
            alert.setHeaderText("Item Name Empty");
            alert.setContentText("Please input a valid item name.");
            alert.showAndWait();
        }

        else {

            //Create a new List to hold values.
            ObservableList<Item> newItemdata = FXCollections.observableArrayList();
            ObservableList<Item> data = itemTable.getItems();

            //Check if the item is unique.
            //Also ensure that the program can handle said items
            int itemsize = data.size();
            if (itemsize > 999999999)
            {
                // Nothing selected.
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Maximum Items Limit Reached");
                alert.setHeaderText("Maximum Items Limit Reached");
                alert.setContentText("Maximum Limit [100] Items Reached.");
                alert.showAndWait();
                bUniqueItemExists = false;

            }
            else {
                for (int i = 0; i < itemsize; i++) {
                    if (data.get(0).getItemName().equals(itemNameField.getText())) {
                        // Nothing selected.
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Not Valid Entry");
                        alert.setHeaderText("Duplicate Item");
                        alert.setContentText("Please enter a Unique Item Name. This item is already in the List.");
                        alert.showAndWait();
                        bUniqueItemExists = false;
                        break;
                    }
                }
            }
            if (bUniqueItemExists) {
                newItemdata.add(new Item(itemNameField.getText(), itemDescriptionField.getText(), sDateField, sStatusField, bStatusField));
                for (Item b : data) {
                    newItemdata.add(b);
                }
                itemTable.setItems(newItemdata);
                itemTable.refresh();

                //Clear the Input fields
                itemStatusField.setText("");
                itemDescriptionField.setText("");
                itemStatusField.setText("");
            }
        }

    }
    //deletes the item from the table
    @FXML
    protected void deleteItem(ActionEvent event)
    {
        int selectedIndex = itemTable.getSelectionModel().getSelectedIndex();
        System.out.println("Deleted Index ==" + selectedIndex);

        if (selectedIndex >= 0)
        {

            //Create a new List to hold values.
            ObservableList<Item> newItemdata = FXCollections.observableArrayList();
            ObservableList<Item> data = itemTable.getItems();
            TreeItem<Item> itemRoot = null;
            for (Item b : data)
            {
                newItemdata.add(b);
            }
            newItemdata.remove(selectedIndex);
            itemTable.setItems(newItemdata);
            itemTable.refresh();

        } else {

            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Item Selected");
            alert.setContentText("Please select an Item in the table.");
            alert.showAndWait();
        }
    }

    //clears out everything in the table, but does not touch the file itself, unless it is saved
    @FXML
    protected void clearAllItems(ActionEvent event)
    {
        try
        {
            Alert a =
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to Clear All Items?", ButtonType.YES,
                            ButtonType.NO);
            Optional<ButtonType> confirm = a.showAndWait();
            if (confirm.isPresent() && confirm.get() == ButtonType.YES) {


                //Create a new List to hold values.
                ObservableList<Item> newItemdata = FXCollections.observableArrayList();
                ObservableList<Item> data = itemTable.getItems();
                for (Item b : data) {
                    newItemdata.add(b);
                }

                for (Item c : data) {
                    newItemdata.remove(c);
                }

                itemTable.setItems(newItemdata);
                itemTable.refresh();
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR = " + e.toString());
        }

    }

    //writes table data to a user defined file
    @FXML
    public void saveData(ActionEvent event) throws IOException
    {
        String filename = "";

        Alert a =
                new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to Save, this will overwrite on existing data file?", ButtonType.YES,
                        ButtonType.NO);
        Optional<ButtonType> confirm = a.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Item Data File");
            File selectedFile = fileChooser.showSaveDialog(controllerStage);
            if (selectedFile != null) {
                System.out.println("File name " + selectedFile);
                filename = selectedFile.getPath();
            }
            else
            {
                System.out.println("File not found");

            }

            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }


            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename), true /* append = true */))) {
                for (int row = 0; row < itemTable.getItems().size(); row++) {
                    StringBuilder stringBuilder = new StringBuilder(255);
                    Item readItem = itemTable.getItems().get(row);
                    stringBuilder.append(readItem.getItemName() + "|" + readItem.getItemDescription() + "|" + readItem.getItemDueDate() + "|" + readItem.getItemStatus());
                    writer.println(stringBuilder.toString());
                }
                writer.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Exception e " + ex.toString());
            }
        }
    }

    //exits the application
    @FXML
    protected void closeApplication(ActionEvent event)
    {
        try {
            Alert a =
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to quit?",
                            ButtonType.YES,
                            ButtonType.NO);
            Optional<ButtonType> confirm = a.showAndWait();
            if (confirm.isPresent() && confirm.get() == ButtonType.YES) {

                Platform.exit();
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR = " + e.toString());
        }
    }


}
/*
Handle all the aspects of the table as well as the File I/O associated with it
Events are defined by user input through the GUI which can be recorded in the back-end portion of the code
Account for errors and bugs in user program manipulation and other mishaps
Ensure content restrictions are in place as well

A user shall be able to add a new item to the list
A user shall be able to remove an item from the list
A user shall be able to clear the list of all items
A user shall be able to edit the description of an item within the list
A user shall be able to edit the due date of an item within the list
A user shall be able to mark an item in the list as either complete or incomplete
A user shall be able to display all of the existing items in the list
A user shall be able to display only the incomplete items in the list
A user shall be able to display only the completed items in the list
 */