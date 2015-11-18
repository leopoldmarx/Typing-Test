package com.leopoldmarx.typingtest.view;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.leopoldmarx.typingtest.words.Words;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ViewMain extends Application
{
	private static final String NAME = "Typing Test";
	
	private ArrayList<String> randomWords = new ArrayList<>();
	private ArrayList<String> typedWords  = new ArrayList<>();
	
	private int index = 0;
	private static final int WIDTH  = 670;
	private static final int HEIGHT = 270;
	
	private BorderPane borderPane = new BorderPane();
	private HBox bottomHBox = new HBox(20);
	private ArrayList<HBox> hBoxArray = new ArrayList<>();
	private Scene mainScene = new Scene(borderPane);
	
	private Words w = new Words();
	
	private JFXTextField field = new JFXTextField();
	private TextFlow textFlow = new TextFlow();
	private JFXButton resetButton = new JFXButton("Reset");
	private JFXButton restart = new JFXButton("Restart");
	
	private static final int  FONTSIZE = 20;
	private static final Font COMMONFONT = new  Font("Devanagari MT", FONTSIZE);
	private static final Font BOLDFONT =   Font.font("Devanagari MT", FontWeight.BOLD, FONTSIZE);
	private static final Font INFOFONT =   Font.font("Devanagari MT", FontWeight.BOLD, 25);
	
	private static final Integer STARTTIME = 60;
	private Timeline timeline;
	private Label timerLabel = new Label(STARTTIME.toString());
	private Integer timeSeconds = STARTTIME;
	
	private Label wpm = new Label("0");
	private Label cpm = new Label("0");
	
	private Image icon = new Image(getClass().getResourceAsStream(
			"/com/leopoldmarx/typingtest/resources/computer7.png"));
	
	private String previousWord;
	
	private Stage window;
	
	@Override
	public void start(Stage stage) throws Exception
	{
		window = stage;
		window.setTitle(NAME);
		window.setMinWidth(WIDTH);
		window.setMinHeight(HEIGHT);
		
		window.widthProperty().addListener(e -> 
				bottomHBox.setSpacing((window.getWidth() - WIDTH) / 5 + 40));
		restart.setOnKeyPressed(e -> e.consume());
		
		resetButton.setRipplerFill(Color.MAROON);
		resetButton.setPadding(new Insets(10));
		resetButton.setFont(COMMONFONT);
		resetButton.setOnKeyPressed(e -> e.consume());
		
		resetButton.setOnAction(e -> 
		{
			randomWords.clear();
			typedWords.clear();
			index = 0;
			field.clear();
			field.setDisable(false);
			textFlow = new TextFlow();
			timeline.getKeyFrames().clear();
			timeline = null;
			previousWord = "";
			timerLabel.setText(STARTTIME.toString());
			wpm.setText("0");
			cpm.setText("0");
			
			for (int i = 1; i <= 20; i++)
			{
				String temp = w.randomWord();
				Text t = new Text(i % 10 == 0 && i != 20
						? temp + "\n" 
						: temp + " ");
				t.setFont(i == 1 ? BOLDFONT : COMMONFONT);
				textFlow.getChildren().add(t);
				randomWords.add(temp);
			}
			
			textFlow.setPadding(new Insets(20, 30, 0, 30));
			borderPane.setTop(textFlow);
		});
		
		textFlow.setPadding(new Insets(20, 30, 0, 30));
		
		for (int i = 1; i <= 20; i++)
		{
			String temp = w.randomWord();
			Text t = new Text(i % 10 == 0 && i != 20
					? temp + "\n" 
					: temp + " ");
			t.setFont(i == 1 ? BOLDFONT : COMMONFONT);
			textFlow.getChildren().add(t);
			randomWords.add(temp);
		}
		
		field.setFocusColor  (Color.MAROON);
		field.setUnFocusColor(Color.CADETBLUE);
		field.setFont(COMMONFONT);
		field.setPadding(new Insets(5, 30, 0, 30));
		field.setPromptText("Start typing!");
		
		field.setOnKeyTyped(e -> 
		{
			String fieldWord = field.getText().toLowerCase();
			
			if (    (int)(e.getCharacter().charAt(0)) >= 65 &&
					(int)(e.getCharacter().charAt(0)) <= 122)
				fieldWord += e.getCharacter().toLowerCase().charAt(0);
			
			//Backspace to previous word
			if (    fieldWord.isEmpty()                && 
					e.getCharacter().charAt(0) == '\b' && 
					typedWords.size() != 0             &&
					fieldWord.equals(previousWord))
			{
				index--;
				field.setText(typedWords.get(typedWords.size() - 1));
				typedWords.remove(typedWords.size() - 1);
				field.positionCaret(field.getText().length());
				previousWord = fieldWord;
				
				for (int i = 1; i <= 10 && (index) % 10 == 9; i++)
					randomWords.remove(randomWords.get(randomWords.size() - 1));
			}
			
			//Add word to array and clear TextField when space is pressed
			else if (Character.isWhitespace(e.getCharacter().charAt(0)))
			{
				index++;
				typedWords.add(fieldWord);
				field.clear();
				e.consume();
				fieldWord =  "";
				previousWord = "";
				
				for (int i = 1; i <= 10 && (index) % 10 == 0; i++)
					randomWords.add(w.randomWord());
			}
			
			else 
			{
				previousWord = fieldWord;
			}
			
			//Update TextFlow, bold current word, and color wrong words.
			Text tempText;
			TextFlow tf = new TextFlow();
			
			for(int i = (index) / 10 * 10; i < index; i++) 
			{
				tempText = new Text((i + 1) % 10 == 0
						? randomWords.get(i) + "\n" : randomWords.get(i) + " ");
				
				tempText.setFont(COMMONFONT);
				if (!randomWords.get(i).equals(typedWords.get(i)))
					tempText.setFill(Color.RED);
				
				tf.getChildren().add(tempText);
			}
			
			tempText = new Text((index + 1) % 10 == 0
					? randomWords.get(typedWords.size()) + "\n"
					: randomWords.get(typedWords.size()) + " ");
			
			tempText.setFont(BOLDFONT);
			
			try
			{
				if (!fieldWord.equals(
						randomWords.get(typedWords.size()).
						substring(0, fieldWord.length())))
					tempText.setFill(Color.RED);
				
			} catch (Exception e1)
			{
				tempText.setFill(Color.RED);
			}
			
			tf.getChildren().add(tempText);
			
			for (int i = index + 1; i < randomWords.size(); i++)
			{
				tempText = new Text((i + 1) % 10 == 0 && i != randomWords.size() - 1
						? randomWords.get(i) + "\n" 
						: randomWords.get(i) + " ");
				tempText.setFont(COMMONFONT);
				tf.getChildren().add(tempText);
			}
			
			tf.setPadding(new Insets(20, 30, 0, 30));
			borderPane.setTop(tf);
			
			//timer and functionality
			if (typedWords.isEmpty())
			{
				if (timeline != null) timeline.stop();
				
				timeSeconds = STARTTIME;
				
				// update timerLabel
				timerLabel.setText(timeSeconds.toString());
				timeline = new Timeline();
				timeline.setCycleCount(Timeline.INDEFINITE);
				timeline.getKeyFrames().add(
						new KeyFrame(Duration.seconds(1), e1 -> 
						{
							timeSeconds--;
							
							int wordCount = 0;
							int charCount = 0;
							for (int i = 0; i < typedWords.size(); i++){
								if (randomWords.get(i).equals(typedWords.get(i)))
								{
									wordCount++;
									charCount += randomWords.get(i).length();
								}
							}
							
							float t = wordCount;
							Integer temp = (int) (STARTTIME * t / 
									(STARTTIME - timeSeconds.floatValue()));
							wpm.setText(temp.toString());
							
							temp = (int) (STARTTIME * charCount / 
									(STARTTIME - timeSeconds.floatValue()));
							cpm.setText(temp.toString());
							
							timerLabel.setText(timeSeconds.toString());
							
							if (timeSeconds <= 0)
							{
								timeline.stop();
								
								Integer wordCountInteger = wordCount;
								Integer charCountInteger = charCount;
								
								window.setScene(completeScene(
										wordCountInteger.toString(),
										charCountInteger.toString()));
								
								randomWords.clear();
								typedWords.clear();
								index = 0;
								field.clear();
								textFlow = new TextFlow();
								timeline = null;
								previousWord = "";
								timerLabel.setText(STARTTIME.toString());
								wpm.setText("0");
								cpm.setText("0");
								
								for (int i = 1; i <= 20; i++)
								{
									String temp1 = w.randomWord();
									Text t1 = new Text(i % 10 == 0 && i != 20
											? temp1 + "\n" 
											: temp1 + " ");
									t1.setFont(i == 1 ? BOLDFONT : COMMONFONT);
									textFlow.getChildren().add(t1);
									randomWords.add(temp1);
								}
								
								textFlow.setPadding(new Insets(20, 30, 0, 30));
								borderPane.setTop(textFlow);
							}
						})
				);
				timeline.playFromStart();
			}
		});
		
		restart.setOnAction(e -> 
		{
			if (borderPane.getScene() == null)
			{
				Scene scene = new Scene(borderPane);
				window.setScene(scene);
			}
			else {
				window.setScene(borderPane.getScene());
			}
		});
		
		Label seconds  = new Label("Seconds: ");
		Label wpmLabel = new Label("WPM: ");
		Label cpmLabel = new Label("CPM: ");
		
		seconds   .setFont(INFOFONT);
		wpmLabel  .setFont(INFOFONT);
		cpmLabel  .setFont(INFOFONT);
		
		timerLabel.setFont(INFOFONT);
		wpm       .setFont(INFOFONT);
		cpm       .setFont(INFOFONT);
		
		hBoxArray.add(new HBox(seconds, timerLabel));
		hBoxArray.add(new HBox(wpmLabel, wpm));
		hBoxArray.add(new HBox(cpmLabel, cpm));
		
		for (HBox h : hBoxArray)
			h.setPadding(new Insets(6, 0, 0, 0));
		
		bottomHBox.getChildren().addAll(hBoxArray);
		bottomHBox.getChildren().add(resetButton);
		bottomHBox.setPadding(new Insets(0, 30, 15, 30));
		
		borderPane.setTop(textFlow);
		borderPane.setCenter(field);
		borderPane.setBottom(bottomHBox);
		window.getIcons().add(icon);
		window.setScene(mainScene);
		window.show();
	}
	
	private Scene completeScene(String wpm, String cpm)
	{
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(50));
		vBox.setSpacing(20);
		
		Scene scene = new Scene(vBox);
		
		Label congratulations = new Label("Congratulations!");
		congratulations.setAlignment(Pos.CENTER);
		congratulations.setTextAlignment(TextAlignment.CENTER);
		congratulations.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		congratulations.setFont(new Font("Devanagari MT", 25));
		
		Label infoLabel = new Label(wpm + " WPM, " + cpm + " CPM");
		infoLabel.setAlignment(Pos.CENTER);
		infoLabel.setTextAlignment(TextAlignment.CENTER);
		infoLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		infoLabel.setFont(INFOFONT);
		
		restart.setAlignment(Pos.CENTER);
		restart.setTextAlignment(TextAlignment.CENTER);
		restart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		restart.setPadding(new Insets(8, 20, 8, 20));
		restart.setFont(new Font("Devanagari MT", 25));
		
		vBox.getChildren().addAll(congratulations, infoLabel, restart);
		
		return scene;
	}
}