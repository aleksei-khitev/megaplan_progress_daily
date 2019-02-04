package ru.akhitev.megaplan.progress.daily;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher extends Application {
    private static ApplicationContext springContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        springContext = new ClassPathXmlApplicationContext("/beans.xml");
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("JavaFX and Gradle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static ApplicationContext getSpringContext() {
        return springContext;
    }
}
