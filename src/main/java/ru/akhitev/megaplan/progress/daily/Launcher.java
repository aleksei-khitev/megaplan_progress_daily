package ru.akhitev.megaplan.progress.daily;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.akhitev.megaplan.progress.daily.config.SpringDBConfig;

public class Launcher extends Application {
    private static ApplicationContext springContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        springContext = new AnnotationConfigApplicationContext(SpringDBConfig.class);
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
