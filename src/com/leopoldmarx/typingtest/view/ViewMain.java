package com.leopoldmarx.typingtest.view;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.leopoldmarx.typingtest.words.Words;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
	
	private Words w = new Words();
	
	private JFXTextField field = new JFXTextField();
	private TextFlow textFlow = new TextFlow();
	private JFXButton resetButton = new JFXButton("Reset");
	
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
	
	@Override
	public void start(Stage window) throws Exception
	{
		window.setTitle(NAME);
		window.setMinWidth(WIDTH);
		window.setMinHeight(HEIGHT);
		
		window.widthProperty().addListener(e ->
		{
//			for (HBox h : hBoxArray)
//			{
//				//TODO add responsivness to components
//				h.setPadding(new Insets(16, (window.getWidth() - WIDTH) / 3 + 20 , 0, 0));
//			}
			bottomHBox.setSpacing((window.getWidth() - WIDTH) / 5 + 40);
			//bottomHBox.setPadding(new Insets((window.getWidth() - WIDTH) / 10));
		});
		
		resetButton.setRipplerFill(Color.MAROON);
		resetButton.setPadding(new Insets(10));
		resetButton.setFont(COMMONFONT);
		
		resetButton.setOnAction(e -> 
		{
			randomWords.clear();
			typedWords.clear();
			index = 0;
			field.clear();
			field.setDisable(false);
			textFlow = new TextFlow();
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
								
								field.setDisable(true);
								
								Integer wordCountInteger = wordCount;
								Integer charCountInteger = charCount;
								
								wpm.setText(wordCountInteger.toString());
								cpm.setText(charCountInteger.toString());
								
								ViewResults vr = new ViewResults(
										wordCountInteger.toString(), 
										charCountInteger.toString());
								vr.display();
							}
						})
				);
				timeline.playFromStart();
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
		
		bottomHBox.getChildren().add(resetButton);
		bottomHBox.getChildren().addAll(hBoxArray);
		bottomHBox.setPadding(new Insets(0, 30, 15, 30));
		
		borderPane.setTop(textFlow);
		borderPane.setCenter(field);
		borderPane.setBottom(bottomHBox);
		Scene s = new Scene(borderPane);
		window.getIcons().add(icon);
		window.setScene(s);
		window.show();
	}
}