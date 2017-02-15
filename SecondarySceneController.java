import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import javafx.stage.FileChooser;
import java.io.File;

public class SecondarySceneController
{

    private Stage stage;
    private PrimarySceneController parent;

    @FXML   private Button addButton;
    @FXML   private Button browseButton;
    @FXML   private Button removeButton;    
    @FXML   private Button saveButton;
    @FXML   private TextField trackNameTextField;
    @FXML   private TextField artistNameTextField;
    @FXML   private TextField albumNameTextField;    
    @FXML   private TextField yearReleasedTextField;
    @FXML   private ChoiceBox genreChoiceBox;    
    @FXML   private Button cancelButton;

    private Track track;

    public SecondarySceneController()
    {
        System.out.println("Initialising controllers...");
    } 

    public void prepareStageEvents(Stage stage)
    {
        System.out.println("Preparing stage events...");

        this.stage = stage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Close button was clicked!");
                    stage.close();
                }
            });
    }         

    @FXML   void initialize() 
    {            
        System.out.println("Asserting controls...");
        try
        {
            assert trackNameTextField != null : "Can't find trackNameTextField";
            assert albumNameTextField != null : "Can't find albumNameTextField";
            assert artistNameTextField != null : "Can't find artistNameTextField";
            assert yearReleasedTextField != null : "Can't find yearReleasedTextField";
            assert genreChoiceBox != null : "Can't find genreChoiceBox";
            assert saveButton != null : "Can't find saveButton";
            assert cancelButton != null : "Can't find cancelButton";
            assert browseButton != null : "Can't find browseButton";

        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }

        System.out.println("Populating scene with items from the database...");        
        @SuppressWarnings("unchecked")
        List<Genre> targetList = genreChoiceBox.getItems();  // Grab a reference to the listView's current item list.
        Genre.readAll(targetList);       
        genreChoiceBox.getSelectionModel().selectFirst();
        
        List<Artist> targetList = artistNameTextField.getItems();  // Grab a reference to the listView's current item list.
        Artist.readAll(targetList);       
        artistNameTextField.getSelectionModel().selectFirst();

    }

    public void setParent(PrimarySceneController parent)
    {
        this.parent = parent;
    }

    public void loadItem(int id)
    {        
        track = Track.getById(id);
        
        trackNameTextField.setText(track.trackName);
        List<Genre> targetList = genreChoiceBox.getItems();
        for(Genre c : targetList)
        {
            if (c.genreID == track.genreId)
            {
                genreChoiceBox.getSelectionModel().select(c);
            }                
        }
        List<Artist> targetList = artistNameTextField.getItems();
        for(Artist c : targetList)
        {
            if (c.artistID == track.artistId)
            {
                artistNameTextField.getSelectionModel().select(c);
            }                
        }

    }

    @FXML   void saveButtonClicked()
    {
        System.out.println("Save button clicked!");        

        if (track == null)
        {   
            track = new Track(0, "", 0, 0);
        }

        track.trackName = trackNameTextField.getText();

        Genre selectedGenre = (Genre) genreChoiceBox.getSelectionModel().getSelectedItem();        
        track.genreId = selectedGenre.genreID;
        
        Artist selectedArtist = (Artist) artistNameTextField.getSelectionModel().getSelectedItem();        
        track.artistId = selectedArtist.artistID;

        track.save();

        parent.initialize();

        stage.close();
    }

    @FXML   void cancelButtonClicked()
    {
        System.out.println("Cancel button clicked!");        
        stage.close();
    }

    @FXML   void browseButtonClicked()
    {
        System.out.println("Browse button clicked!");        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick a file");
        List<File> list = fileChooser.showOpenMultipleDialog(stage);

        for (File file : list) {
            System.out.println(" >>>>>> " + file.getName());
        }
    }

}
