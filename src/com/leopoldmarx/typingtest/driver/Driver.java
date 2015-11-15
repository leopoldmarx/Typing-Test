package com.leopoldmarx.typingtest.driver;

public class Driver {
	
	public static void main (String args[]) {
		
		Program.getInstance();
	}
}

//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.geometry.Pos;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//import javafx.util.Duration;
// 
//public class Driver extends Application {
// 
//    // private class constant and some variables
//    private static final Integer STARTTIME = 15;
//    private Timeline timeline;
//    private Label timerLabel = new Label();
//    private Integer timeSeconds = STARTTIME;
// 
//    public static void main(String[] args) {
//        Application.launch(args);
//    }
// 
//    @Override
//    public void start(Stage primaryStage) {
// 
//        // Setup the Stage and the Scene (the scene graph)
//        primaryStage.setTitle("FX Timer");
//        Group root = new Group();
//        Scene scene = new Scene(root, 300, 250);
// 
//        // Configure the Label
//        timerLabel.setText(timeSeconds.toString());
//        timerLabel.setTextFill(Color.RED);
//        timerLabel.setStyle("-fx-font-size: 4em;");
// 
//        // Create and configure the Button
//        Button button = new Button();
//        button.setText("Start Timer");
//        button.setOnAction(event -> {
//				if (timeline != null) {
//		            timeline.stop();
//		        }
//		        timeSeconds = STARTTIME;
//		 
//		        // update timerLabel
//		        timerLabel.setText(timeSeconds.toString());
//		        timeline = new Timeline();
//		        timeline.setCycleCount(Timeline.INDEFINITE);
//		        timeline.getKeyFrames().add(
//		                new KeyFrame(Duration.seconds(1), event1 -> {
//		                        timeSeconds--;
//		                        // update timerLabel
//		                        timerLabel.setText(
//		                              timeSeconds.toString());
//		                        if (timeSeconds <= 0) {
//		                            timeline.stop();
//		                        }
//		                }));
//		        timeline.playFromStart();
//        });
// 
//        // Create and configure VBox
//        // gap between components is 20
//        VBox vb = new VBox(20);
//        // center the components within VBox
//        vb.setAlignment(Pos.CENTER);
//        // Make it as wide as the application frame (scene)
//        vb.setPrefWidth(scene.getWidth());
//        // Move the VBox down a bit
//        vb.setLayoutY(30);
//        // Add the button and timerLabel to the VBox
//        vb.getChildren().addAll(button, timerLabel);
//        // Add the VBox to the root component
//        root.getChildren().add(vb);
// 
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//}
