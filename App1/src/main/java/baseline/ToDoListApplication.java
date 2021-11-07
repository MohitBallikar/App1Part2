package baseline;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javafx.collections.transformation.FilteredList;


public class ToDoListApplication extends Application
{


    //Global Variables.
    final int MAX_ITEM_SIZE = 100;
    final VBox vb = new VBox();
    final ScrollBar sc = new ScrollBar();
    final ScrollPane sp = new ScrollPane();
    private Stage stage;
    int i = 0;
    private BorderPane root;
    private final int SIZE = 60;
    private static final String ITEMS_FILE_NAME = "data/Item.txt";
    TreeItem<String> ti ;
    Label lblShowName;
    private TextField textField = new TextField();
    private final TreeView treeView = new TreeView();
    private final TextArea textArea = new TextArea();
    private TreeTableView<Item> treeTableView ;
    private String sStatus   = "";



    private TableView<Item> table = new TableView<Item>();

    ////START OF Running CODE>

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(ToDoListApplication.class.getResource("Application.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Item List (Mohit Ballikar) version 1.0");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    public void LoadDataFile(ObservableList names)
    {
        try {
            Scanner s = new Scanner(new File("data/items.txt"));
            while (s.hasNext()) {
                String line = s.nextLine();
                System.out.println("Value of line "+ line);

                names.add(line);

            }
        }
        catch (FileNotFoundException fnf)
        {
            System.out.println("File not Found");
        }
    }

    public GridPane createGridPane(List<Item> itemsGrid,Button btnAdd, Button btnRemove, Button btnSave, Button btnClear)
    {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: grey;");

        grid.setPadding(new Insets(20, 20, 20, 20));
        // grid.setMinSize(900, 500);
        grid.setVgap(10);
        grid.setHgap(10);

        treeTableView = new TreeTableView<Item>();
        treeTableView.setEditable(true);
        treeTableView.setPrefSize(830, 500);


        TreeTableColumn<Item, String> itemName = new TreeTableColumn<Item, String>("Item Name");
        TreeTableColumn<Item, String> itemdescription = new TreeTableColumn<Item, String>("Item Description");
        TreeTableColumn<Item, String> itemduedate = new TreeTableColumn<Item, String>("Item Due Date");
        TreeTableColumn<Item, String> itemStatus = new TreeTableColumn<Item, String>("Item Status");
        TreeTableColumn<Item, Boolean> itemdelete = new TreeTableColumn<Item, Boolean>("Complete?");

        // Add columns to TreeTable.
        treeTableView.getColumns().addAll( itemName, itemdescription, itemduedate, itemStatus, itemdelete);

        String[] itemValues ;
        int counter = 0;
        Item itemData = null;
        List<Item> items = LoadDatafromFileToCollections(ITEMS_FILE_NAME);
        TreeItem<Item> itemRoot = null;
        for (Item b : items)
        {

            if ( counter == 0)
            {
                itemData = new Item( b.getItemName(), b.getItemDescription(), b.getItemDueDate(), b.getItemStatus(), false);
                itemRoot = new TreeItem<Item>(itemData);
            }
            else
            {
                itemData = new Item( b.getItemName(), b.getItemDescription(), b.getItemDueDate(), b.getItemStatus(), false);
                TreeItem<Item> itemChildData = new TreeItem<Item>(itemData);
                itemRoot.getChildren().add(itemChildData);

            }
            counter++;
        }
        treeTableView.setRoot(itemRoot);

        btnRemove.setOnAction(e -> btnRemove_Click(btnRemove,treeTableView));

        btnClear.setOnAction(e -> btnClear_Click(btnClear,treeTableView));

        btnSave.setOnAction(event -> {
            //If the file exist delete it and save new info
            File file = new File(ITEMS_FILE_NAME);
            if(file.exists()) {
                file.delete();
            }
            TreeItem<Item>  ti = treeTableView.getRoot();
            saveParentAndChildren(ti, "root");
        });


        // Defines how to fill data for each cell.
        // Get value from property of Employee.
        itemName.setCellValueFactory(new TreeItemPropertyValueFactory<Item, String>("itemname"));
        itemdescription.setCellValueFactory(new TreeItemPropertyValueFactory<Item, String>("itemdescription"));
        itemduedate.setCellValueFactory(new TreeItemPropertyValueFactory<Item, String>("itemduedate"));
        itemStatus.setCellValueFactory(new TreeItemPropertyValueFactory<Item, String>("itemStatus"));


        itemdelete.setCellFactory(p -> {
            CheckBoxTreeTableCell<Item,Boolean> cell = new CheckBoxTreeTableCell<Item,Boolean>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        grid.getChildren().add(treeTableView);
        return grid;
    }


    public GridPane createEditFields()
    {
        final String[] datepickerValue = new String[1];

        //Add an ADD button.
        Button btnAdd = new Button();
        btnAdd.setText("Add Item");

        Button buttonSearch = new Button();
        buttonSearch.setText("Search");

        GridPane gridEdit = new GridPane();
        gridEdit.setAlignment(Pos.BASELINE_RIGHT);
        gridEdit.setHgap(5);
        gridEdit.setVgap(5);
        gridEdit.setPadding(new Insets(10, 10, 10, 10));

        Text itemname = new Text("Item Name:");
        gridEdit.add(itemname, 0, 0);
        TextField textName = new TextField();
        textName.setPrefColumnCount(10);
        btnAdd.setDisable(true);
        textName.textProperty().addListener((observable, oldValue, newValue) -> {
            btnAdd.setDisable(false);
            textName.setText(newValue);
            System.out.println("TextField Text Changed (newValue: " + newValue + ")\n");
        });


        gridEdit.add(textName, 1, 0);

        //Item Description.
        Text itemdescription = new Text("Item Description:");
        gridEdit.add(itemdescription, 0, 1);
        TextField textDesc = new TextField();
        textDesc.setPrefColumnCount(10);
        btnAdd.setDisable(true);
        textDesc.textProperty().addListener((observable, oldValue, newValue) -> {
            btnAdd.setDisable(false);

            if ( newValue.length() > 256 )
            {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                        "The Field can only accept 256 characters.",
                        ButtonType.CLOSE);
            }
            textDesc.setText(newValue);
            System.out.println("TextField Text Changed (newValue: " + newValue + ")\n");
        });


        gridEdit.add(textDesc, 1, 1);

        //Due Date
        Text dueDate = new Text("Due Date:");
        gridEdit.add(dueDate, 0, 2);
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("YYYY-MM-DD");
        datePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                LocalDate date = datePicker.getValue();
                datepickerValue[0] = date.toString();
                if ( datepickerValue[0].equals("") || datepickerValue[0] == null )
                {
                    datepickerValue[0] = "9999-99-99";
                }
                System.out.println("Selected date: " + datepickerValue[0]);
            }
        });
        gridEdit.add(datePicker, 1, 2);

        //Item Status
        Text itemStatus = new Text("Item Status:");
        gridEdit.add(itemStatus, 0, 3);
        CheckBox cbComplete = new CheckBox("Complete");

        // create a event handler
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e)
            {
                sStatus   = "Incomplete";
                if (cbComplete.isSelected()) {
                    sStatus = "Complete";
                }
            }
            final String sselected = sStatus;
        };


        // set event to checkbox
        cbComplete.setOnAction(event);
        gridEdit.add(cbComplete, 1, 3);

        btnAdd.setOnAction(e -> btnAdd_Click(btnAdd,treeTableView,textName.getText(),textDesc.getText(), datepickerValue[0],"Incomplete"));
        gridEdit.add(btnAdd, 0,4);

        Button buttonReset = new Button();
        buttonReset.setText("Reset");
        buttonReset.setOnAction( e -> btnReset_Click (buttonReset, textName,textDesc,datePicker, cbComplete) );
        gridEdit.add(buttonReset, 1,4);


        //Add Buttons for Search.


        //Add Radio Buttons for Search.
        ToggleGroup group = new ToggleGroup();
        RadioButton rbcomplete = new RadioButton("Complete");
        rbcomplete.setToggleGroup(group);
        rbcomplete.setSelected(true);
        RadioButton rbincomplete = new RadioButton("Incomplete");
        rbincomplete.setToggleGroup(group);
        gridEdit.add(rbcomplete, 1,5);
        gridEdit.add(rbincomplete, 2,5);

        if (rbcomplete.isSelected()) {
            //Set Search at the end
            buttonSearch.setOnAction(e -> btnSearch_Click(buttonSearch, treeTableView, rbcomplete));
        }

        if (rbincomplete.isSelected()) {
            //Set Search at the end
            buttonSearch.setOnAction(e -> btnSearch_Click(buttonSearch, treeTableView, rbincomplete));
        }

        //final ToggleGroup group1 = new ToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {

                    RadioButton selectedRadioButton =
                            (RadioButton) group.getSelectedToggle();
                    System.out.println("selectedRadioButton "  + selectedRadioButton.getText());

                }
            }
        });

        gridEdit.add(buttonSearch, 0,5);

        return gridEdit;
    }

    //Clear Button action code
    private void btnSearch_Click(Button buttonSearch, TreeTableView<Item> tvsearch, RadioButton rb)
    {
        buttonSearch.setOnAction(event -> {
            System.out.println("It comes here");

            ObservableList<Item> data = FXCollections.observableArrayList();
            FilteredList<Item> filtered = data.filtered(p->p.getItemStatus() == "Complete");

            System.out.println("****RB " + rb.getText());

//            tvsearch.setRoot(filtered);


            System.out.println("It comes here 111");
        });
    }

    public BorderPane createBorderPane() {

        BorderPane borderPane = new BorderPane();

        //MenuBar *****************
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll( new MenuItem("Open"), new MenuItem("Save"),  new SeparatorMenuItem(), new MenuItem("Close"));

        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(new MenuItem("Undo"),new MenuItem("Redo"), new MenuItem("Cut"), new MenuItem("Copy"), new MenuItem("Paste"));
        editMenu.setDisable(true);

        Menu FindMenu = new Menu("Find");
        editMenu.getItems().addAll(new MenuItem("Search/"));

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(new MenuItem("Help Contents"),new SeparatorMenuItem(), new MenuItem("About..."));
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        //Add an Open Button
        Button btnOpen = new Button();
        btnOpen.setText("Open");
        btnOpen.setOnAction( e -> btnOpen_Click () );

        //Add a Close Button.
        Button btnClose = new Button();
        btnClose.setText("Close");
        btnClose.setOnAction( e -> btnClose_Click () );

        //Add a Save Button.
        Button btnSave = new Button();
        btnSave.setText("Save");

        //Add a Load Button
        Button btnLoad = new Button();
        btnLoad.setText("Load Items");

        //Add a Remove Button
        Button btnRemove = new Button();
        btnRemove.setText("Remove Item");

        Button btnClear = new Button();
        btnClear.setText("Clear Items");
        // btnClear.setOnAction( e -> btnClear_Click () );

        //Add a Toolbar.
        ToolBar toolbar =   new ToolBar(
                btnOpen,
                new Separator(),
                btnSave,
                new Separator(),
                btnLoad,
                btnRemove,
                btnClear,
                new Separator(),
                btnClose
        );


        VBox vbox = new VBox();
        vbox.getChildren().addAll(menuBar, toolbar);

        ti = new TreeItem<>("Item List");
        ti.setExpanded(true);

        String[] itemValues = new String[100] ;
        List<Item> items = LoadDatafromFileToCollections(ITEMS_FILE_NAME);
        borderPane.setTop(vbox);

        //Center Section where Details will be displayed
        Label centerlbl = new Label("Item Details");
        centerlbl.setFont(Font.font(null, FontWeight.BOLD, 12));
        borderPane.setCenter(centerlbl);
        borderPane.setBottom(new Label("Status text:   Manage Item List"));


        GridPane grid = createGridPane(items,btnLoad, btnRemove, btnSave, btnClear);
        borderPane.setCenter(grid);

        GridPane gridEdit  = createEditFields();
        borderPane.setRight(gridEdit);

        return borderPane;
    }

    //Save the TreeTableView to a file.
    static private void saveParentAndChildren(TreeItem<Item> root, String parent)
    {
        // System.out.println("Current Parent :" + root.getChildren().get(1).toString());
        try(PrintWriter writer = new PrintWriter(new FileOutputStream(new File(ITEMS_FILE_NAME),true /* append = true */))){
            //writer.println(root.getValue() + "=" + parent);
            writer.println(parseStringToWriteFile(root.getValue().toString()));

            for(TreeItem<Item> child: root.getChildren())
            {
                if(child.getChildren().isEmpty())
                {
                    writer.println(parseStringToWriteFile(child.getValue().toString()));
                } else {
                    saveParentAndChildren(child, root.getChildren().get(1).toString());
                }
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("Exception e "+ ex.toString());
        }
    }

    static public String parseStringToWriteFile(String sValue)
    {
        String[] arrOfStr = sValue.split("\\,");
        int strindex = 0;
        String newStr = "";
        String returnString = "";
        StringBuilder stringBuilder = new StringBuilder(255);

        for (String a : arrOfStr)
        {
            strindex = a.indexOf("[value:");
            //7 is the length of the "[value:"
            newStr = a.substring(strindex+7);
            newStr = newStr.replace("]","");
            //System.out.println("**** NEW LINE = " + newStr);
            stringBuilder.append(newStr.trim()+"|");
        }

        //Remove the last char "|" from the String.
        returnString = stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1).toString();
        //System.out.println("****returnString = " + returnString);
        return returnString;

    }

    //Clear Button action code
    private void btnClear_Click(Button btnClear, TreeTableView<Item> tvClear)
    {
        btnClear.setOnAction(event -> {
            tvClear.getRoot().getChildren().clear();
        });
    }


    private void btnOpen_Click()
    {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Data File");
            fileChooser.showOpenDialog(stage);

            Scanner s = new Scanner(new File(ITEMS_FILE_NAME));
            while (s.hasNext()) {
                String line = s.nextLine();
                // System.out.println("Value of line "+ line);
                // create a checkbox
                CheckBox c = new CheckBox(line);
                // add label
                root.getChildren().add(c);
                c.setIndeterminate(false);
                c.setSelected(false);
            }
        }
        catch (FileNotFoundException fnf)
        {
            System.out.println("File not Found");
        }
    }

    //Add Method to include TreeItem.
    public void btnAdd_Click(Button btnAdd, TreeTableView<Item> tv, String Itemname, String ItemDescription, String Itemduedate, String Itemstatus)
    {

        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Item itemData = new Item(Itemname, ItemDescription, Itemduedate,Itemstatus, false);
                TreeItem<Item> itemRoot =new TreeItem<Item>(itemData);
                tv.getRoot().getChildren().add(itemRoot);
            }
        };

        // when button is pressed
        btnAdd.setOnAction(event);

    }

    //Add Method to include TreeItem.
    public void btnRemove_Click(Button btnRemove, TreeTableView<Item> tvRemove )
    {
        btnRemove.setOnAction(event -> {
            TreeItem<Item> selectedItem = tvRemove.getSelectionModel().getSelectedItem();

            if (selectedItem.getParent() == null) {
                return;
            }
            selectedItem.getParent().getChildren().remove(selectedItem);
        });
    }


    //Save Method to include TreeItem.
    public void btnSave_Click(Button btnSave, TreeItem<Item> ti)
    {

    }



    public void LoadDatafromFileToCollections()
    {
        List<Item> items = LoadDatafromFileToCollections("data/items.txt");


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
        boolean isComplete = false;

        // create and return book of this metadata
        if ( status.equals("Complete"))
        {
            isComplete = true;
        }
        return new Item(itemname, itemdescription,itemdate, status, isComplete );
    }


    public FlowPane createFlowPane()
    {
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setStyle("-fx-background-color: DAE6F3;");
        flow.setHgap(5);
        Button left  = new Button("left");
        Button center  = new Button("center");

        flow.getChildren().addAll(left, center);

        return flow;
    }

    private void btnClose_Click()
    {
        Alert a =
                new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to quit?",
                        ButtonType.YES,
                        ButtonType.NO);
        Optional<ButtonType> confirm = a.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
            stage.close();
        }

    }


    private void btnReset_Click(Button buttonReset, TextField textName, TextField textDesc,DatePicker datePicker,CheckBox cbComplete)
    {

        buttonReset.setOnAction(event -> {
            textName.clear();
            textDesc.clear();

        });
    }



}