/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//View-Controller for the Item table.

public class ApplicationController {

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

    @FXML private TextField itemNameField;
    @FXML private TextField itemDescriptionField;
    @FXML private DatePicker itemDueDateField;
    @FXML private CheckBox itemStatusField;


    private ObservableList<Item> masterData = FXCollections.observableArrayList();

    private static final String ITEMS_FILE_NAME = "data/Item.txt";

    /**
     * Just add some sample data in the constructor.
     */
    public ApplicationController() {

        List<Item> items = LoadDatafromFileToCollections(ITEMS_FILE_NAME);
        for (Item b : items)
        {
            masterData.add(new Item( b.getItemName(), b.getItemDescription(), b.getItemDueDate(), b.getItemStatus(), false));
        }
        /*
        masterData.add(new Person("Hans", "Muster"));
        masterData.add(new Person("Ruth", "Mueller"));
        masterData.add(new Person("Heinz", "Kurz"));
        masterData.add(new Person("Cornelia", "Meier"));
        masterData.add(new Person("Werner", "Meyer"));
        masterData.add(new Person("Lydia", "Kunz"));
        masterData.add(new Person("Anna", "Best"));
        masterData.add(new Person("Stefan", "Meier"));
        masterData.add(new Person("Martin", "Mueller"));

         */
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {

        itemTable.getSelectionModel().cellSelectionEnabledProperty().set(true);

        // 0. Initialize the columns.
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().itemDescriptionProperty());
        itemDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemDueDateColumn.setCellValueFactory(cellData -> cellData.getValue().itemDueDateProperty());
        itemDueDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemStatusColumn.setCellValueFactory(cellData -> cellData.getValue().itemStatusProperty());


        itemTable.getColumns().setAll(itemNameColumn, itemDescriptionColumn,itemDueDateColumn,itemStatusColumn);
        itemTable.setEditable(true);

        //Only Update the Item Description.

        //Modifying the firstName property
        itemDescriptionColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, String> t) {
                ((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setItemDescription(t.getNewValue());
                itemTable.refresh();
            }
        });


        //Only Update the Item Due Date.
        itemDueDateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, String> t)
            {
                ((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setItemDueDate(t.getNewValue());
            }
        });




        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Item> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                // If filter text is empty, display all items.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                System.out.println("Inside search");
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getItemStatus().toLowerCase().trim().indexOf(lowerCaseFilter) != -1)
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

        // itemTable.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

    }



    public void LoadDatafromFileToCollections()
    {
        List<Item> items = LoadDatafromFileToCollections(ITEMS_FILE_NAME);

        // let's print all the meta data.
        /*
        for (Item b : items)
        {
            System.out.println("Array == " + b);
        }
        */

    }

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
                //System.out.println("****Value of line " + line);
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

    private static Item createItem(String[] metadata)
    {
        String itemname = metadata[0];
        String itemdescription = metadata[1];
        String itemdate = metadata[2];
        String status = metadata[3];

        //boolean isDelete = metadata[5];
        boolean isDelete = false;

        // create and return book of this metadata
        if ( status.equals("Complete"))
        {
            isDelete = true;
        }
        return new Item(itemname, itemdescription,itemdate, status, isDelete );
    }

    @FXML
    protected void loadDataItem(ActionEvent event)
    {

    }

    @FXML
    protected void addItem(ActionEvent event)
    {
        try {
            String sDateField = "";
            String sStatusField = "Pending";
            Boolean bStatusField = false;

            LocalDate date = itemDueDateField.getValue();
            sDateField = date.toString();
            if (sDateField.equals("") || sDateField == null) {
                sDateField = "9999-99-99";
            }
            System.out.println("Selected date: " + sDateField);

            if (itemStatusField.isSelected()) {
                bStatusField = true;
                sStatusField = "Complete";
            }
            //ObservableList<Item> data = itemTable.getItems();

            ObservableList<Item> data =  FXCollections.observableArrayList();
            data = itemTable.getItems();
            System.out.println("Value of data = " + data.size());

            data.add(new Item(itemNameField.getText(), itemDescriptionField.getText(), sDateField , sStatusField, false));


        }
        catch (Exception e)
        {
            System.out.println("ERROR = " + e.toString());
        }

    }

    public void saveData(ActionEvent event) throws IOException
    {
        //If the file exist delete it and save new info
        File file = new File(ITEMS_FILE_NAME);
        if(file.exists()) {
            file.delete();
        }

        try(PrintWriter writer = new PrintWriter(new FileOutputStream(new File(ITEMS_FILE_NAME),true /* append = true */)))
        {
            //writer.println(root.getValue() + "=" + parent);
            for (int row = 0; row < itemTable.getItems().size(); row++)
            {
                StringBuilder stringBuilder = new StringBuilder(255);
                Item readItem = itemTable.getItems().get(row);
                stringBuilder.append(readItem.getItemName()+"|"+readItem.getItemDescription()+"|"+ readItem.getItemDueDate()+"|"+readItem.getItemStatus());
                System.out.println("DATA TO BE WRITTEN = " + stringBuilder.toString());
                writer.println(stringBuilder.toString());
            }
            writer.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("Exception e "+ ex.toString());
        }
    }


}