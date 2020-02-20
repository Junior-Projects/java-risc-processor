import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application implements RestartInterface {

    private Label labelCourseName;
    private Label labelProjectTitle;
    private Label labelDoneBy;

    private Button buttonStart;
    private Button buttonExit;
    private HBox hBoxButtons;

    private VBox vBox;
    private StackPane stackPane;
    private Scene scene;
    private Stage stage;

    private final double BUTTON_WIDTH = 150;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        this.stage = stage;
        labelCourseName = createLabel("Computer Organization", 50);
        labelProjectTitle = createLabel("Risc Processor", 40);
        labelDoneBy = createLabel("Done by: Jeffrey Joumjian\n\t\tMaria Kantardjian", 20);

        buttonStart = createButton("Start");
        buttonExit = createButton("EXIT");

        setupHBox();
        setupVBox();

        stackPane = new StackPane();
        ImageView imageView = new ImageView(new Image(new FileInputStream("backgroundImage.png")));
        stackPane.getChildren().add(imageView);
        stackPane.getChildren().add(vBox);

        setButtonExitAction();
        setButtonStartAction();

        scene = new Scene(stackPane, 1000, 550);
        stage.setScene(scene);
        stage.show();
    }

    private void setupVBox() {
        vBox = new VBox(labelCourseName, labelProjectTitle, labelDoneBy, hBoxButtons);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(8);
    }

    private void setupHBox(){
        hBoxButtons = new HBox(buttonStart, buttonExit);
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.setSpacing(16);
        hBoxButtons.setPadding(new Insets(16));
        buttonStart.setMaxWidth(BUTTON_WIDTH);
        buttonExit.setMaxWidth(BUTTON_WIDTH);
        HBox.setHgrow(buttonStart, Priority.ALWAYS);
        HBox.setHgrow(buttonExit, Priority.ALWAYS);
    }

    private Label createLabel(String text, int fontSize){
        Label label = new Label(text);
        label.setTextFill(Color.WHITESMOKE);
        label.setStyle("-fx-font-weight:  bold; -fx-font-size: " + fontSize + "px");
        return label;
    }

    private Button createButton(String text){
        Button button = new Button(text);
        return button;
    }

    private void setButtonExitAction(){
        buttonExit.setOnAction(event -> Platform.exit());
    }

    private void setButtonStartAction() {
        buttonStart.setOnAction(event -> {
            StartScene startScene = null;
            try {
                startScene = new StartScene(stage,this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            scene.setRoot(startScene.getStackPane());
        });
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void restartApp() {
        scene.setRoot(stackPane); //stackpane has background and elements on it
        stage.show();
    }
}
