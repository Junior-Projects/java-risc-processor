import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import processor.Instruction;
import processor.Microprocessor;

public class RunStage extends Stage {

    private Scene scene;
    private BorderPane borderPane;
    private BorderPane borderPane2;

    private VBox vBoxLeft;
    private VBox vBoxLeft2;
    private VBox bigVbox;
    TableView tableRegValues;
    TableView tablePC;
    TableView tableMBR;
    TableView tableMemSlot;

    private StackPane stackPane;
    private StackPane stackPane2;

    private VBox vBoxRight;
    private Label labelCurrentInstruction;
    private ProgressBar progressBarOverall;
    private ProgressBar progressBarInstruction;
    private ProgressIndicator progressIndicatorOverall;
    private ProgressIndicator progressIndicatorInstruction;
    private HBox hBoxInstructionProgress;
    private HBox hBoxOverallProgress;

    private Microprocessor microprocessor;

    //private StackPane stackPaneInstructionProgress = new StackPane();

    private HBox hBoxBottom;
    private HBox hBoxTop;
    private Button buttonRun;
    private Button buttonRestart;
    private Button buttonExit;

    private RestartInterface restartInterface;

    private ObservableList<TableViewInstructions> instructionList = FXCollections.observableArrayList();
    private ObservableList<RegistersTableModel> tableRegObservableList = FXCollections.observableArrayList();
    private ObservableList<pcModel> pcList = FXCollections.observableArrayList();
    private ObservableList<RegistersTableModel> mbrList = FXCollections.observableArrayList();
    
    //private ObservableList<String> listInstructions = FXCollections.observableArrayList();
    private ObservableList<MemorySlotTableModel> observableListMemorySlot = FXCollections.observableArrayList(Arrays.asList());

    private int currentInstruction = 0;
    private File file;
    private boolean finished=false;
    private int seconds = 1;
    


    public RunStage(RestartInterface restartInterface,File file) throws IOException {
    	this.file = file;
    	microprocessor = new Microprocessor(file);
        fillObservableList();
        fillObservableListRegisters();
        this.restartInterface = restartInterface;
        stackPane = new StackPane();
        borderPane = new BorderPane();
        borderPane2 = new BorderPane();
        setupTableRegValues();
        setupTableMemSlot();
       
        setupTableMBR();
        setupButtonExit();
        setupButtonRestart();
        setupButtonRun();
        addBackgroundImage();
        setupProgressBar();
        setupLabelCurrentInstruction();
        setupVBoxRight();
        setupTablePC();
        setupHBoxTop();
        setupVBoxLeft();
        
        setupHBoxBottom();
        borderPane.setLeft(vBoxLeft);
        setupVBox2Left();
        borderPane2.setLeft(vBoxLeft2);
        borderPane2.setRight(vBoxRight);
        
        //borderPane2.setMaxSize(1000, 1000);
        stackPane.getChildren().add(borderPane);
        stackPane.getChildren().add(borderPane2);
        
        
        scene = new Scene(stackPane, 1080, 800);
        setScene(scene);
        show();
    }

    private void setupVBoxLeft() {
        vBoxLeft = new VBox(tableRegValues);
        vBoxLeft.setPadding(new Insets(16,16,16,16));
        vBoxLeft.setSpacing(16);
        vBoxLeft.setMaxWidth(750);
        vBoxLeft.setMaxHeight(260);
        tableRegValues.prefWidthProperty().bind(new SimpleDoubleProperty(500));
        tableRegValues.prefHeightProperty().bind(new SimpleDoubleProperty(260));
    }
    
    private void setupVBox2Left() {
        vBoxLeft2 = new VBox(tableMBR,tableMemSlot);
        vBoxLeft2.setAlignment(Pos.TOP_CENTER);
        vBoxLeft2.setPadding(new Insets(30,16,100,16));
        vBoxLeft2.setSpacing(16);
        vBoxLeft2.setMaxSize(500, 500);
        vBoxLeft2.setPrefSize(500, 700);
        tableMBR.prefHeightProperty().bind(new SimpleDoubleProperty(500));
        tableMBR.prefWidthProperty().bind(new SimpleDoubleProperty(500));
        tableMemSlot.prefHeightProperty().bind(new SimpleDoubleProperty(500));
        tableMemSlot.prefWidthProperty().bind(new SimpleDoubleProperty(500));
    }

    private void setupHBoxBottom() {
        hBoxBottom = new HBox(buttonRun, buttonRestart, buttonExit);
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);
        hBoxBottom.setSpacing(8);
        hBoxBottom.setPadding(new Insets(8));
        borderPane2.setBottom(hBoxBottom);
        hBoxBottom.setPadding(new Insets(16));
        hBoxBottom.setSpacing(8);
    }
    
    private void setupHBoxTop() {
    	hBoxTop = new HBox (vBoxRight,tablePC);
    	hBoxTop.setAlignment(Pos.CENTER_RIGHT);
    	hBoxTop.setSpacing(8);
    	hBoxTop.setPadding(new Insets(8));
        borderPane2.setTop(hBoxTop);
        hBoxTop.setPadding(new Insets(16));
        hBoxTop.setSpacing(8);
    }

    private void setupVBoxRight() {
        vBoxRight = new VBox(labelCurrentInstruction,hBoxOverallProgress,hBoxInstructionProgress);
        vBoxRight.setPadding(new Insets(8));
        vBoxRight.setSpacing(8);
        progressBarOverall.prefWidthProperty().bind(new SimpleDoubleProperty(200));
        progressBarInstruction.prefWidthProperty().bind(new SimpleDoubleProperty(200));
        progressBarInstruction.setVisible(false);
        progressIndicatorInstruction.setVisible(false);
        HBox.setHgrow(progressBarOverall, Priority.ALWAYS);
        HBox.setHgrow(progressBarInstruction, Priority.ALWAYS);
    }

    private void setupTableRegValues() {
        tableRegValues = new TableView();
        TableColumn tableColumnRegisters = new TableColumn("Registers");
        tableColumnRegisters.setMaxWidth(100);
        TableColumn tableColumnValues = new TableColumn("Binary Value");
        tableColumnValues.setEditable(false);
        tableColumnValues.setMaxWidth(450);
        TableColumn tableColumnIntegerValue = new TableColumn("Integer Value");
        tableColumnIntegerValue.setMaxWidth(200);
        tableColumnIntegerValue.setEditable(false);
        tableColumnRegisters.setStyle("-fx-alignment: CENTER");
        tableColumnValues.setStyle("-fx-alignment: CENTER");
        tableColumnIntegerValue.setStyle("-fx-alignment: CENTER");
        tableColumnRegisters.setCellValueFactory(new PropertyValueFactory<RegistersTableModel, String>("register"));
        tableColumnValues.setCellValueFactory(new PropertyValueFactory<RegistersTableModel, String>("value"));
        tableColumnIntegerValue.setCellValueFactory(new PropertyValueFactory<RegistersTableModel, String>("integerProperty"));
        tableRegValues.getColumns().addAll(tableColumnRegisters, tableColumnValues, tableColumnIntegerValue);
        tableColumnRegisters.setEditable(false);
        tableRegValues.setColumnResizePolicy((param) -> true);
        tableRegValues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableRegValues.setItems(tableRegObservableList);
        
        tableRegValues.setMaxSize(750, 260);
    }
    
    private void setupTablePC() {
    	tablePC = new TableView();
        TableColumn tableColumnPC = new TableColumn("PC");
        TableColumn instructionColumn = new TableColumn("Instruction");
        tableColumnPC.setMaxWidth(75);
        tablePC.setMaxSize(350, 200);
        instructionColumn.setMaxWidth(275);
        tableColumnPC.setStyle("-fx-alignment: CENTER");
        instructionColumn.setStyle("-fx-alignment: CENTER");
        instructionColumn.setCellValueFactory(new PropertyValueFactory<String, SimpleStringProperty>("instruction"));
        tableColumnPC.setCellValueFactory(new PropertyValueFactory<String,SimpleStringProperty>("intValue"));
        tablePC.getColumns().addAll(tableColumnPC,instructionColumn);
        tableColumnPC.setEditable(false);
        instructionColumn.setEditable(false);
        tablePC.setColumnResizePolicy((param) -> true);
        tablePC.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablePC.setItems(pcList);
        
    }
    
    private void setupTableMBR() {
    	tableMBR = new TableView();
        TableColumn tableColumnMBR = new TableColumn("MBR Binary Value");
        TableColumn tableColumnIntegerValue = new TableColumn("Integer Value");
        
        tableColumnMBR.setMaxWidth(500);
        tableColumnIntegerValue.setMaxWidth(200);
        tableColumnIntegerValue.setStyle("-fx-alignment: CENTER");
        tableColumnMBR.setStyle("-fx-alignment: CENTER");
        tableColumnMBR.setCellValueFactory(new PropertyValueFactory<RegistersTableModel, String>("value"));
        tableColumnIntegerValue.setCellValueFactory(new PropertyValueFactory<RegistersTableModel, String>("integerProperty"));
        tableMBR.getColumns().addAll(tableColumnMBR,tableColumnIntegerValue);
        tableColumnMBR.setEditable(false);
        tableColumnIntegerValue.setEditable(false);
        tableMBR.setColumnResizePolicy((param) -> true);
        tableMBR.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableMBR.setItems(mbrList);
        
        tableMBR.setMaxSize(700, 500);
    }

    private void setupTableMemSlot() {
        tableMemSlot = new TableView();
        //TableColumn tableColumnInstruction = new TableColumn("Instruction");
        TableColumn address = new TableColumn("Memory Address");
        address.setMaxWidth(200);
        TableColumn tableColumnMemSlot = new TableColumn("Memory Slot");
        tableColumnMemSlot.setMaxWidth(500);
        
        
        //tableColumnInstruction.setStyle("-fx-alignment: CENTER");
        tableColumnMemSlot.setStyle("-fx-alignment: CENTER");
        address.setStyle("-fx-alignment: CENTER");

       // tableColumnInstruction.setCellValueFactory(new PropertyValueFactory<String, SimpleStringProperty>("instruction"));
        address.setCellValueFactory(new PropertyValueFactory<String, SimpleStringProperty>("instruction"));
        tableColumnMemSlot.setCellValueFactory(new PropertyValueFactory<String, SimpleStringProperty>("memorySlot"));
       
        tableMemSlot.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableMemSlot.setMaxSize(700, 500);
        tableMemSlot.getColumns().addAll(address,tableColumnMemSlot);
        tableColumnMemSlot.setEditable(false);
        address.setEditable(false);
        tableMemSlot.setItems(observableListMemorySlot);
    }

    private void addBackgroundImage() throws FileNotFoundException {
        ImageView imageView = new ImageView(new Image(new FileInputStream("gradientImageBackground.jpg")));
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        stackPane.getChildren().add(imageView);
    }

    private void setupButtonRun() {
        buttonRun = new Button("Simulate");
        buttonRun.setStyle("-fx-background-color: #afff82");
        buttonRun.setMaxWidth(100);
        HBox.setHgrow(buttonRun, Priority.ALWAYS);
        buttonRun.setOnAction(event -> {
			try {
				finished = false;
				seconds=1;
				labelCurrentInstruction.setText("");
				run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }

    private void setupButtonRestart() {
        buttonRestart = new Button("Restart");
        buttonRestart.setStyle("-fx-background-color: #ffff");
        buttonRestart.setMaxWidth(100);
        HBox.setHgrow(buttonRestart, Priority.ALWAYS);
        buttonRestart.setOnAction(event -> {
            close();
            restartInterface.restartApp();
        });
    }

    private void setupButtonExit() {
        buttonExit = new Button("EXIT");
        buttonExit.setMaxWidth(100);
        buttonExit.setStyle("-fx-background-color: #ff5b5b");
        buttonExit.setTextFill(Color.WHITE);
        HBox.setHgrow(buttonExit, Priority.ALWAYS);
        buttonExit.setOnAction(event -> Platform.exit());
    }

    private void setupLabelCurrentInstruction() {
        labelCurrentInstruction = new Label("Ready to simulate...");
        labelCurrentInstruction.setTextFill(Color.WHITESMOKE);
        labelCurrentInstruction.setFont(new Font(labelCurrentInstruction.getFont().getName(),16));
    }

    private void setupProgressBar() {
        progressBarInstruction = new ProgressBar(0);
        progressBarOverall = new ProgressBar(0);
        progressIndicatorInstruction = new ProgressIndicator(0);
        progressIndicatorOverall = new ProgressIndicator(0);
        hBoxOverallProgress = new HBox(progressBarOverall, progressIndicatorOverall);
        hBoxInstructionProgress = new HBox(progressBarInstruction, progressIndicatorInstruction);
        
        progressBarOverall.setMaxWidth(Double.MAX_VALUE);
        hBoxOverallProgress.setMaxWidth(Double.MAX_VALUE);

        progressIndicatorInstruction.progressProperty().bind(progressBarInstruction.progressProperty());
        progressIndicatorOverall.progressProperty().bind(progressBarOverall.progressProperty());
        progressBarOverall.progressProperty().addListener((observable, oldValue, newValue) -> {
            double progress = newValue == null ? 0 : newValue.doubleValue();
            if (progress < 0.1){
                progressBarOverall.setStyle("-fx-accent: #FF0000");
            }else if (progress < 0.2){
                progressBarOverall.setStyle("-fx-accent: #FF2E00");
            }else if (progress < 0.3){
                progressBarOverall.setStyle("-fx-accent: #FF4E00");
            }else if (progress < 0.4){
                progressBarOverall.setStyle("-fx-accent: #FF7500");
            }else if (progress < 0.5){
                progressBarOverall.setStyle("-fx-accent: #FF9C00");
            }else if (progress < 0.6){
                progressBarOverall.setStyle("-fx-accent: #FFC400");
            }else if (progress < 0.7){
                progressBarOverall.setStyle("-fx-accent: #FFEB00");
            }else if (progress < 0.8){
                progressBarOverall.setStyle("-fx-accent: #EBFF00");
            }else if (progress < 0.9){
                progressBarOverall.setStyle("-fx-accent: #C4FF00");
            }else if(progress<0.95){
                progressBarOverall.setStyle("-fx-accent: #9CFF00");
            }else{
                progressBarOverall.setStyle("-fx-accent: #00FF00");
            }
            progressIndicatorOverall.setStyle(progressBarOverall.getStyle());
        });

        progressBarInstruction.setStyle("-fx-accent: #43ff00");
        progressIndicatorInstruction.setStyle("-fx-accent: #43ff00");
    }

    private void fillObservableList() throws IOException {
    	String line;
    	BufferedReader br = new BufferedReader(new FileReader(file));
    	while((line=br.readLine())!=null)
    		instructionList.add(new TableViewInstructions(line.split(" ")[0], line.substring(3)));
    	br.close();
    }
    
    private void fillObservableListRegisters() {
        for (int i = 0; i < instructionList.size(); i++) {
            TableViewInstructions instructions = instructionList.get(i);
            if (instructions.getInstruction().equalsIgnoreCase("jmp")) {
                continue;
            }
            String parameters = instructions.getParameters();
            for (int j = 0; j < parameters.length(); j++) {
                if (parameters.charAt(j) == 'r') {
                    String register = parameters.charAt(j) + "" + parameters.charAt(j + 1);
                    RegistersTableModel model = new RegistersTableModel(register, "0");
                    if (!tableRegObservableList.contains(model)) {
                        tableRegObservableList.add(model);
                    }
                }
            }
        }
        Collections.sort(tableRegObservableList);
    }

    private String removeInstructionSemicolon(String instruction){
        if (instruction.contains(";"))
            return instruction.substring(0, instruction.length()-1);
        return instruction;
    }

    private void run() throws IOException {
        observableListMemorySlot.clear();
        mbrList.clear();
        pcList.clear();
        buttonRun.setDisable(true);
        currentInstruction = 0;
        labelCurrentInstruction.setText("Starting execution...");
        microprocessor = new Microprocessor(file);
        progressBarInstruction.setProgress(0);
        progressIndicatorInstruction.setVisible(false);
        progressBarInstruction.setVisible(false);
        progressBarOverall.setProgress(0);
        Timeline oneSecondCommand = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(seconds), event -> {
            labelCurrentInstruction.setText(removeInstructionSemicolon(microprocessor.getInstructionSyntax()[currentInstruction]));

            updatePCTable();
            updateMicroprocessor();
            updateRegTable();
            updateMemorySlotTable();
            
            updateMBRTable();
            currentInstruction=Integer.parseInt(microprocessor.getPCValue());
            double progress = (double) currentInstruction / microprocessor.index();
            if(progress>progressBarOverall.getProgress())
            progressBarOverall.setProgress(progress);
            
            if(progress==1)
            	{
            		finished = true;
            		labelCurrentInstruction.setText("Finishing execution...");
            		showComplete();
            		seconds = 0;
            		oneSecondCommand.stop();
            		return;
            		
            	}
        });
        oneSecondCommand.getKeyFrames().add(keyFrame);
        if(finished)
        	return;
        else
        	oneSecondCommand.setCycleCount(Animation.INDEFINITE);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBarInstruction.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(seconds), e -> {
                }, new KeyValue(progressBarInstruction.progressProperty(), 1))
        );
       if(finished)
    	   return;
       else
        timeline.setCycleCount(Animation.INDEFINITE);
        oneSecondCommand.play();
        timeline.play();
        if(finished)
    	{timeline.stop(); showComplete();return;}
        oneSecondCommand.setOnFinished(event -> {
            showComplete(); 
        });
    }
    
    private void showComplete() {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("100%");
        alert.setHeaderText("SUCCESS");
        alert.setContentText("All instructions have been executed successfully");
        alert.show();
        buttonRun.setDisable(false);
    }

    private void updateMemorySlotTable() {
        Instruction ins = microprocessor.getInstruction();
        if(ins.getType().equalsIgnoreCase("M")) {
        String instruction = ins.getMadd();
        String memoryAddress = microprocessor.getMemory().loadFromMemory(instruction);
        //instruction = removeInstructionSemicolon(instruction);
        MemorySlotTableModel model = new MemorySlotTableModel(instruction, memoryAddress);
        if((observableListMemorySlot.size()>1 && instruction.equals(observableListMemorySlot.get(observableListMemorySlot.size()-1).getMemorySlot()))||
        		ins.getFunct().equals("101"))
    		return;
        else
        	observableListMemorySlot.add(model);}
        else {
        	if(observableListMemorySlot.size()>1 && "null".equals(observableListMemorySlot.get(observableListMemorySlot.size()-1).getMemorySlot()) && 
        			"null".equals(observableListMemorySlot.get(observableListMemorySlot.size()-1).getInstruction()))
        		return;
        	observableListMemorySlot.add(new MemorySlotTableModel("null","null"));
        }
    }
    
    private void updateMBRTable() {
    	String value = microprocessor.getMBRValue();
    	int integerProperty = microprocessor.getBinary().toInteger(value);
    	if((mbrList.size()>1 && value.equals(mbrList.get(mbrList.size()-1).getValue()) && integerProperty==mbrList.get(mbrList.size()-1).getIntegerProperty()))
    		return;
    	mbrList.add(new RegistersTableModel("",value));

    	mbrList.get(mbrList.size()-1).setIntegerProperty(integerProperty);
    	microprocessor.clearMBR();
    }
    
    private void updatePCTable() {
    	
    	String intValue = Integer.parseInt(microprocessor.getPCValue())+"";
    	String instruction = microprocessor.getInstructionSyntax()[Integer.parseInt(microprocessor.getPCValue())];
    	if(!instruction.trim().equals("stop;"))
    		pcList.add(new pcModel(intValue,instruction));
    	
    }

    private void updateRegTable() {
        for (int i = 0; i < tableRegObservableList.size(); i++){
            String register = tableRegObservableList.get(i).getRegister();
            int id = Character.getNumericValue(register.charAt(1));
            tableRegObservableList.get(i).setValue(microprocessor.getRegisterByIndex(id).getValue());
            tableRegObservableList.get(i).setIntegerProperty(microprocessor.getIntRegisterValue(id));
        }
    }
    private void updateMicroprocessor() {
    	 microprocessor.process();}
}
