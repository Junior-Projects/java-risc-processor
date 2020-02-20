import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StartScene {

    private VBox vBoxLeft;
    private ComboBox<String> comboBox; //for drop down menu

    private HBox hBoxFields;
    private Label labelSource1;
    private Label labelSource2;
    private Label labelAddress;
    private TextField textFieldSource1;
    private TextField textFieldSource2;
    private TextField textFieldAddress;
    
    

    private Button buttonAdd;

    private List<Label> listLabels;
    private List<TextField> listTextFields;
//  belongs to vbox left
    private TableView tableViewInstructions;
    private Button buttonRemove;
    private Button buttonClear;
    private VBox vBoxButtons;
    private HBox hboxTableButtons;
    private ObservableList<TableViewInstructions> observableListInstructions = FXCollections.observableArrayList();

    private VBox vBoxRight;
    private Button buttonDefault;
    private Button buttonImport;
    private Button buttonHelp;

    private HBox hBoxBottom;
    private Button buttonRun;
    private Button buttonExit;

    private BorderPane borderPane;
    private StackPane stackPane;

    private List<String> listInstructions = Arrays.asList("add", "sub", "get", "geti", "set", "beq", "bgt", "jump");
    private List<String> threeFieldsList = Arrays.asList("add", "sub", "beq", "bgt");
    private List<String> twoFieldsList = Arrays.asList("get", "geti", "set");
    private List<String> oneFieldList = Arrays.asList("jump");

    private RestartInterface restartInterface;
    private Stage stage;
    private File defaultFile = new File("TestWhile.txt");

    public StartScene(Stage stage, RestartInterface restartInterface) throws FileNotFoundException {
        this.restartInterface = restartInterface;
        this.stage = stage;
        stackPane = new StackPane();
        borderPane = new BorderPane();
        setupComboBox();
        setupVBoxLeft();
        setupVBoxRight();
        setupHBoxBottom();
        borderPane.setLeft(vBoxLeft);
        borderPane.setRight(vBoxRight);
        borderPane.setBottom(hBoxBottom);
        addBackgroundImage();
        stackPane.getChildren().add(borderPane);
    }

    private void addBackgroundImage() throws FileNotFoundException {
        ImageView imageView = new ImageView(new Image(new FileInputStream("gradientImageBackground.jpg")));
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        stackPane.getChildren().add(imageView);
    }

    private void setupComboBox() {
        comboBox = new ComboBox<>(FXCollections.observableArrayList(listInstructions));
        comboBox.setStyle("-fx-background-color: #ffff");
        comboBox.setMaxWidth(150);
        comboBox.setPromptText("Select Instruction");
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, previousSelectedValue, currentSelectedValue) -> {
            if (currentSelectedValue.equalsIgnoreCase("geti")){
                labelAddress.setText("Constant");
            }else if (currentSelectedValue.equalsIgnoreCase("add") || currentSelectedValue.equalsIgnoreCase("sub")){
                    labelAddress.setText("Destination");
            }else{
                labelAddress.setText("Address");
            }
            hBoxFields.setVisible(true);
            hBoxFields.getChildren().clear();
            if (threeFieldsList.contains(currentSelectedValue)) {
                for (int i = 0; i < listLabels.size(); i++) {
                    hBoxFields.getChildren().addAll(listLabels.get(i), listTextFields.get(i));
                }
            } else if (twoFieldsList.contains(currentSelectedValue)) {
                hBoxFields.getChildren().addAll(labelSource2, textFieldSource2, labelAddress, textFieldAddress);
            } else if (oneFieldList.contains(currentSelectedValue)) {
                hBoxFields.getChildren().addAll(labelAddress, textFieldAddress);
            }
            clearFields();
            hBoxFields.getChildren().add(buttonAdd);//buttons add remove and clear next to the hBOX that has textfields and labels
        });
    }

    private void setupVBoxButtons(){
        buttonAdd = new Button("Add");
        buttonAdd.setStyle("-fx-background-color: #4998ff");
        buttonAdd.setTextFill(Color.WHITE);
        buttonRemove = new Button("Remove");
        buttonRemove.setStyle("-fx-background-color: #ff5b5b");
        buttonRemove.setTextFill(Color.WHITE);
        buttonClear = new Button("Clear");
        buttonClear.setStyle("-fx-background-color: #ffff");
        buttonAdd.setMaxWidth(65);
        buttonRemove.setMaxWidth(100);
        buttonClear.setMaxWidth(100);
        vBoxButtons = new VBox(buttonRemove, buttonClear);
        vBoxButtons.setPadding(new Insets(16));
        vBoxButtons.setSpacing(8);
        setButtonAddAction();
        setButtonRemoveAction();
        setBUttonClearAction();
    }

    private void setupHBoxFields() {
        labelSource1 = new Label("Src1");
        labelSource2 = new Label("Src2");
        labelAddress = new Label("Address");
        //}
        labelSource1.setTextFill(Color.WHITESMOKE);
        labelSource2.setTextFill(Color.WHITESMOKE);
        labelAddress.setTextFill(Color.WHITESMOKE);
        textFieldSource1 = createTextField();
        textFieldSource2 = createTextField();
        textFieldAddress = createTextField();
        setupVBoxButtons();
        listLabels = Arrays.asList(labelSource1, labelSource2, labelAddress);
        listTextFields = Arrays.asList(textFieldSource1, textFieldSource2, textFieldAddress);
        hBoxFields = new HBox(labelSource1, textFieldSource1, labelSource2, textFieldSource2, labelAddress, textFieldAddress, buttonAdd);
        hBoxFields.setSpacing(8);
        hBoxFields.setPadding(new Insets(8));
        hBoxFields.setVisible(false);
        hBoxFields.setAlignment(Pos.BASELINE_LEFT);
        HBox.setHgrow(buttonAdd, Priority.ALWAYS);
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setMaxWidth(60);
        return textField;
    }

    private void setupVBoxLeft() {
        setupHBoxFields();
        setupTableView();
        setupHBoxTableButtons();
        vBoxLeft = new VBox(comboBox, hBoxFields, hboxTableButtons);
        vBoxLeft.setPadding(new Insets(8));
        vBoxLeft.setSpacing(36);

//        vBoxLeft.setMaxWidth(800);
        tableViewInstructions.prefWidthProperty().bind(new SimpleDoubleProperty(600));
        HBox.setHgrow(vBoxButtons, Priority.ALWAYS);
        VBox.setVgrow(buttonClear, Priority.ALWAYS);
        VBox.setVgrow(buttonRemove, Priority.ALWAYS);
    }

    private void setupHBoxTableButtons() {
        hboxTableButtons = new HBox(tableViewInstructions, vBoxButtons);
        hboxTableButtons.setPadding(new Insets(16));
        hboxTableButtons.setSpacing(8);
    }

    private void setupVBoxRight() {
        buttonDefault = new Button("Default");
        buttonDefault.setStyle("-fx-background-color: #ffff");
        buttonImport = new Button("Import");
        buttonImport.setStyle("-fx-background-color: #ffff");
        buttonHelp = new Button("Help");
        buttonHelp.setStyle("-fx-background-color: #ffff");
        setButtonDefaultAction();
        setButtonImportAction();
        setButtonHelpAction();
        vBoxRight = new VBox(buttonDefault, buttonImport, buttonHelp);
        vBoxRight.setSpacing(8);
        vBoxRight.setPadding(new Insets(8));
        vBoxRight.setMaxWidth(100);
        buttonDefault.setMaxWidth(100);
        buttonImport.setMaxWidth(100);
        buttonHelp.setMaxWidth(100);
        VBox.setVgrow(buttonDefault, Priority.ALWAYS);
        VBox.setVgrow(buttonImport, Priority.ALWAYS);
        VBox.setVgrow(buttonHelp, Priority.ALWAYS);
    }

    private boolean isValidInput(String text){
        List<String> validInputsList = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            validInputsList.add("r" + i);
        }
        return validInputsList.contains(text);
    }
    
    private boolean isValidInput0(String text){
        List<String> validInputsList = new ArrayList<>();
        for (int i = 1; i < 8; i++){
            validInputsList.add("r" + i);
        }
        return validInputsList.contains(text);
    }

    private boolean isTextAnInt(String text){
        try{
            Integer.parseInt(text);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void setButtonDefaultAction(){
        buttonDefault.setOnAction(event -> {
            loadInstructions(defaultFile);
        });
    }

    private void loadInstructions(File file){
        observableListInstructions.clear();//to empty it. so that default can only be added once, even if clicked twice
        try {
            List<String> listInstructions = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            for (String line : listInstructions) {
                String instruction = line.split(" ")[0];
                String parameters = line.substring(instruction.length());
                TableViewInstructions model = new TableViewInstructions(instruction, parameters);
                
                if(!instruction.trim().equalsIgnoreCase("stop;"))
                	observableListInstructions.add(model);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setButtonImportAction(){
        buttonImport.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a file containing instructions");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(stage);
            loadInstructions(file);
        });
    }

    private void setButtonHelpAction(){
        buttonHelp.setOnAction(event -> new HelpStage());
    }

    private void showError(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid Fields");
        errorAlert.setTitle("ERROR");
        errorAlert.setContentText("Please enter valid values in the fields.");
        errorAlert.show();
    }
    
    private void showError0() {
    	Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid Fields");
        errorAlert.setTitle("ERROR");
        errorAlert.setContentText("r0 cannot be used as a destination register.");
        errorAlert.show();
    }
    
    private void showError2(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid Fields");
        errorAlert.setTitle("ERROR");
        errorAlert.setContentText("Jump address must be a multiple of 2.");
        errorAlert.show();
    }
    
    private void showError4(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid Fields");
        errorAlert.setTitle("ERROR");
        errorAlert.setContentText("Memory address must be a multiple of 4.");
        errorAlert.show();
    }

    private void setButtonAddAction(){
        buttonAdd.setOnAction(event -> {
            String instruction = comboBox.getSelectionModel().getSelectedItem();
            if (instruction.equalsIgnoreCase("add") || instruction.equalsIgnoreCase("sub")){
                if(!isValidInput(textFieldSource1.getText()) || !isValidInput(textFieldSource2.getText()) || !isValidInput(textFieldAddress.getText())){
                    showError();
                    return;
                }
                else if(!isValidInput0(textFieldAddress.getText())){
                	showError0();
                	return;
                }
            }
            else if (instruction.equalsIgnoreCase("set") || instruction.equalsIgnoreCase("geti")
                    || instruction.equalsIgnoreCase("get")){
                if (!isValidInput(textFieldSource2.getText()) || !isTextAnInt(textFieldAddress.getText())){
                    showError();
                    return; }
                
                else if (!instruction.equalsIgnoreCase("set") && !isValidInput0(textFieldSource2.getText())) {
                	showError0(); return;
                }
            }
            else if (instruction.equalsIgnoreCase("jump")){
                if (!isTextAnInt(textFieldAddress.getText())){
                    showError();
                    return;}
                else if(isTextAnInt(textFieldAddress.getText()) && Integer.parseInt(textFieldAddress.getText())%2!=0) { 
                	showError2();
                return;
                }
            }else if (!isValidInput(textFieldSource1.getText()) || !isValidInput(textFieldSource2.getText())
                        || !isTextAnInt(textFieldAddress.getText())){
                    showError();
                    return;
                }

            String operation;
            if (instruction.equalsIgnoreCase("add")){
                operation = textFieldSource1.getText() + " + " + textFieldSource2.getText() + " >> " + textFieldAddress.getText();
            }else if (instruction.equalsIgnoreCase("sub")){
                operation = textFieldSource1.getText() + " - " + textFieldSource2.getText() + " >> " + textFieldAddress.getText();
            }else if (instruction.equalsIgnoreCase("get")){
                operation = textFieldAddress.getText() + " >> "  + textFieldSource2.getText();
            }else if (instruction.equalsIgnoreCase("geti")){
                instruction = "gti";
                operation = textFieldAddress.getText() + " >> " + textFieldSource2.getText();
            }else if (instruction.equalsIgnoreCase("set")){
                operation = textFieldSource2.getText() + " >> " + textFieldAddress.getText();
            }else if (instruction.equalsIgnoreCase("beq")){
                operation = textFieldSource1.getText() + " = " + textFieldSource2.getText() + " >> " + textFieldAddress.getText();
            }else if (instruction.equalsIgnoreCase("bgt")){
                operation = textFieldSource1.getText() + " > " + textFieldSource2.getText() + " >> " + textFieldAddress.getText();
            }else{
                instruction = "jmp";
                operation = " >> " + textFieldAddress.getText();
            }
            TableViewInstructions model = new TableViewInstructions(instruction, operation+";");
            observableListInstructions.add(model);
            clearFields();
        });
    }

    private void setButtonRemoveAction(){
        buttonRemove.setOnAction(event -> {
            textFieldSource1.clear();
            textFieldSource2.clear();
            textFieldAddress.clear();
            if (observableListInstructions.size() == 0){
                return;
            }
            int index = tableViewInstructions.getSelectionModel().getSelectedIndex();
            if (index < 0){
                index = observableListInstructions.size() - 1;
            }
            observableListInstructions.remove(index);
        });
    }

    private void setBUttonClearAction(){
        buttonClear.setOnAction(event -> observableListInstructions.clear());
    }

    private void setupTableView(){
        tableViewInstructions = new TableView();
        TableColumn tableColumnInstruction = new TableColumn("Instruction");
        TableColumn tableColumnParameters = new TableColumn("Parameters");

        tableColumnInstruction.setStyle("-fx-alignment: CENTER");
        tableColumnParameters.setStyle("-fx-alignment: CENTER");

        tableViewInstructions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableColumnInstruction.setCellValueFactory(new PropertyValueFactory<TableViewInstructions, String>("instruction"));
        tableColumnParameters.setCellValueFactory(new PropertyValueFactory<TableViewInstructions, String>("parameters"));

        tableViewInstructions.getColumns().addAll(tableColumnInstruction, tableColumnParameters);
        tableViewInstructions.setEditable(false);
        tableViewInstructions.setItems(observableListInstructions);
    }

    private void setupButtonRun(){
        buttonRun = new Button("Simulate");
        buttonExit = new Button("EXIT");
        buttonExit.setMaxWidth(150);
        buttonExit.setStyle("-fx-background-color: #ff5b5b");
        buttonExit.setTextFill(Color.WHITE);
        buttonRun.setStyle("-fx-background-color: #afff82");
        buttonRun.setMaxWidth(150);
        buttonExit.setOnAction(event -> {Platform.exit();});
        buttonRun.setOnAction(event -> {
            if (observableListInstructions.size() == 0){
                showRunError();
                return;
            }
            try {
            	File file = new File("TInput.txt");
            	BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            	for(int i=0;i<observableListInstructions.size();i++)
            		bw.append(observableListInstructions.get(i).toString()+"\n");
            	bw.append("stop;");
            	bw.close();
                stage.close();//to close main stage which has start scene
                new RunStage(restartInterface,file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showRunError(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("ERROR");
        errorAlert.setHeaderText("Instructions Not Found");
        errorAlert.setContentText("You must add at least one instruction to run");
        errorAlert.show();
    }
    
    
    
    private void clearFields() {
    	textFieldSource1.clear();;
        textFieldSource2.clear();;
        textFieldAddress.clear();;
    }
    


    private void setupHBoxBottom(){
        setupButtonRun();
        hBoxBottom = new HBox(buttonRun,buttonExit);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);
        hBoxBottom.setPadding(new Insets(16));
        hBoxBottom.setSpacing(8);
        HBox.setHgrow(buttonRun, Priority.ALWAYS);
        HBox.setHgrow(buttonExit, Priority.ALWAYS);
    }

    public StackPane getStackPane(){
        return stackPane;
    }
}