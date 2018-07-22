import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("HPOG.fxml"));
        primaryStage.setTitle("Password Generator");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        NameBase.getInstance().initDatabase();
        Generator.getInstance().setPasswordLen(4);
        System.out.println(Generator.getInstance().generateCode());
        launch(args);
    }
}
