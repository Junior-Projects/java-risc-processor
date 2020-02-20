import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelpStage extends Stage {



    public HelpStage(){
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get("help.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebView webView = new WebView();
        webView.getEngine().loadContent(text, "text/html");
        TextArea textArea = new TextArea(text);
        textArea.setEditable(false);
        Scene scene = new Scene(webView, 540, 660);
        setScene(scene);
        show();
    }

}
