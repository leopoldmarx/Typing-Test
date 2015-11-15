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
	private static final String name = "Typing Test";
	
	private ArrayList<String> typingWords = new ArrayList<>();
	private ArrayList<String> typedWords  = new ArrayList<>();
	
	private int index = 0;
	
	private BorderPane borderPane = new BorderPane();
	private HBox hBox = new HBox(20);
	private ArrayList<HBox> hBoxArray = new ArrayList<>();
	
	private Words w = new Words();
	
	private JFXTextField field = new JFXTextField();
	private TextFlow textFlow = new TextFlow();
	private JFXButton resetButton = new JFXButton("Reset");
	
	private static final int  fontSize = 20;
	private static final Font FONT = new Font("Devanagari MT", fontSize);
	
	private static final Integer STARTTIME = 60;
	private Timeline timeline;
	private Label timerLabel = new Label(STARTTIME.toString());
	private Integer timeSeconds = STARTTIME;
	
	private Label wpm = new Label("0");
	private Label cpm = new Label("0");
	
	private String previousWord;
	
	@Override
	public void start(Stage window) throws Exception
	{
		window.setTitle(name);
		window.setWidth(700);
		window.setHeight(450);
		window.widthProperty().addListener(e -> 
				textFlow.setPrefWidth(window.getWidth() - 20));
		
		resetButton.setRipplerFill(Color.MAROON);
		resetButton.setPadding(new Insets(20));
		resetButton.setFont(FONT);
		
		resetButton.setOnAction(e -> 
		{
			//TODO reset all variables
			typingWords.clear();
			typedWords.clear();
			index = 0;
			field.clear();
			field.setDisable(false);
			textFlow = new TextFlow();
			timeline = null;
			timerLabel = new Label(STARTTIME.toString());
			previousWord = "";
			
			timerLabel.setFont(Font.font("Devanagari MT", FontWeight.BOLD, fontSize));
			
			for (int i = 1; i <= 20; i++)
			{
				String temp = w.randomWord();
				Text t = new Text(i % 10 == 0 ? temp + "\n" : temp + " ");
				t.setFont(i == 1 ? Font.font("Devanagari MT", FontWeight.BOLD, fontSize) : FONT);
				textFlow.getChildren().add(t);
				typingWords.add(temp);
			}
			
			textFlow.setPadding(new Insets(20));
			borderPane.setTop(textFlow);
		});
		
		textFlow.setPadding(new Insets(20));
		
		for (int i = 1; i <= 20; i++)
		{
			String temp = w.randomWord();
			Text t = new Text(i % 10 == 0 ? temp + "\n" : temp + " ");
			t.setFont(i == 1 ? Font.font("Devanagari MT", FontWeight.BOLD, fontSize) : FONT);
			textFlow.getChildren().add(t);
			typingWords.add(temp);
		}
		
		field.setFocusColor(Color.MAROON);
		field.setUnFocusColor(Color.CADETBLUE);
		field.setFont(FONT);
		field.setPadding(new Insets(20));
		field.setPromptText("Start typing!");
		
		field.setOnKeyTyped(e -> 
		{
			String typingWord = field.getText().toLowerCase();
			
			if (    (int)(e.getCharacter().charAt(0)) >= 65 &&
					(int)(e.getCharacter().charAt(0)) <= 122)
				typingWord += e.getCharacter().toLowerCase().charAt(0);
			
			//Backspace to previous word
			if (    typingWord.isEmpty()               && 
					e.getCharacter().charAt(0) == '\b' && 
					typedWords.size() != 0             &&
					typingWord.equals(previousWord))
			{
				index--;
				field.setText(typedWords.get(typedWords.size() - 1));
				typedWords.remove(typedWords.size() - 1);
				field.positionCaret(field.getText().length());
				previousWord = typingWord;
				
				for (int i = 1; i <= 10 && (index) % 10 == 9; i++)
				{
					typingWords.remove(typingWords.get(typingWords.size() - 1));
				}
			}
			
			//Add word to array and clear TextField when space is pressed
			else if (Character.isWhitespace(e.getCharacter().charAt(0)))
			{
				index++;
				typedWords.add(typingWord);
				field.clear();
				e.consume();
				typingWord =  "";
				previousWord = "";
				
				for (int i = 1; i <= 10 && (index) % 10 == 0; i++)
				{
					typingWords.add(w.randomWord());
				}
			}
			
			else 
			{
				previousWord = typingWord;
			}
			
			//Update TextFlow
			Text tempText;
			TextFlow tf = new TextFlow();
			
			for(int i = (index) / 10 * 10; i < index; i++) 
			{
				tempText = new Text((i + 1) % 10 == 0
						? typingWords.get(i) + "\n" : typingWords.get(i) + " ");
				
				tempText.setFont(FONT);
				if (!typingWords.get(i).equals(typedWords.get(i)))
					tempText.setFill(Color.RED);
				
				tf.getChildren().add(tempText);
			}
			
			//Comparing typed word to random word
			tempText = new Text((index + 1) % 10 == 0
					? typingWords.get(typedWords.size()) + "\n"
					: typingWords.get(typedWords.size()) + " ");
			tempText.setFont(Font.font("Devanagari MT", FontWeight.BOLD, fontSize));
			try
			{
				if (!typingWord.equals(
						typingWords.get(typedWords.size()).
						substring(0, typingWord.length())))
					tempText.setFill(Color.RED);
				
			} catch (Exception e1)
			{
				tempText.setFill(Color.RED);
				//e1.printStackTrace();
			}
			
			tf.getChildren().add(tempText);
			
			for (int i = index + 1; i < typingWords.size(); i++)
			{
				tempText = new Text((i + 1) % 10 == 0 
						? typingWords.get(i) + "\n" 
						: typingWords.get(i) + " ");
				tempText.setFont(FONT);
				tf.getChildren().add(tempText);
			}
			
			tf.setPadding(new Insets(20));
			borderPane.setTop(tf);
			
			//timer and functionality
			if (typedWords.isEmpty())
			{
				if (timeline != null)timeline.stop();
				
				timeSeconds = STARTTIME;
				
				// update timerLabel
				timerLabel.setText(timeSeconds.toString());
				timeline = new Timeline();
				timeline.setCycleCount(Timeline.INDEFINITE);
				timeline.getKeyFrames().add(
						new KeyFrame(Duration.seconds(1), e1 -> 
						{
							timeSeconds--;
							
							float t = typedWords.size();
							Integer temp = (int) (60 * t / timeSeconds.floatValue());
							wpm.setText(temp.toString());
							
							int numberOfCharecters = 0;
							for (String typedWord: typedWords)
								numberOfCharecters += typedWord.length();
							
							temp = (int) (60 * numberOfCharecters / timeSeconds.floatValue());
							cpm.setText(temp.toString());
							
							timerLabel.setText(timeSeconds.toString());
							if (timeSeconds <= 0)
							{
								timeline.stop();
								// TODO WHERE THE CODE GOES WHEN COMPLETED
								field.setDisable(true);
							}
						})
				);
				timeline.playFromStart();
			}
		});
		
		Label seconds  = new Label("Seconds: ");
		Label wpmLabel = new Label("WPM: ");
		Label cpmLabel = new Label("CMP: ");
		seconds .setFont(Font.font("Devanagari MT", FontWeight.BOLD, 25));
		wpmLabel.setFont(Font.font("Devanagari MT", FontWeight.BOLD, 25));
		cpmLabel.setFont(Font.font("Devanagari MT", FontWeight.BOLD, 25));
		
		timerLabel.setFont(Font.font("Devanagari MT", FontWeight.BOLD, 25));
		wpm       .setFont(Font.font("Devanagari MT", FontWeight.BOLD, 25));
		cpm       .setFont(Font.font("Devanagari MT", FontWeight.BOLD, 25));
		
		hBoxArray.add(new HBox(seconds, timerLabel));
		hBoxArray.add(new HBox(wpmLabel, wpm));
		hBoxArray.add(new HBox(cpmLabel, cpm));
		
		for (HBox h : hBoxArray)
		{
			h.setPadding(new Insets(16, 0, 0, 0));
		}
		
		hBox.getChildren().addAll(resetButton, hBoxArray.get(0), hBoxArray.get(1), hBoxArray.get(2));
		hBox.setPadding(new Insets(20));
		borderPane.setTop(textFlow);
		borderPane.setCenter(field);
		borderPane.setBottom(hBox);
		Scene s = new Scene(borderPane, 700, 450);
		window.setScene(s);
		window.show();
	}
}
