import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PrimarySceneController
{    
    /* The stage that the scene belongs to, required to catch stage events and test for duplicate controllers. */
    private static Stage stage;     

    /* These FXML variables exactly corrispond to the controls that make up the scene, as designed in Scene 
     * Builder. It is important to ensure that these match perfectly or the controls won't be interactive. */

    @FXML   private ListView mainListView;
    @FXML   private Button addButton;
    @FXML   private Button playButton;
    @FXML   private Button stopButton;
    @FXML   private Button editButton;
    @FXML   private Button deleteButton;
    @FXML   private TextField searchTextField;
    @FXML   private Button searchButton;
    @FXML   private Button clearButton;
    @FXML   private Button exitButton;
    @FXML   private Pane borderPane; 
    @FXML   private ChoiceBox searchChoiceBox;   

    public PrimarySceneController()          // The constructor method, called first when the scene is loaded.
    {
        System.out.println("Initialising controllers...");

        /* Our JavaFX application should only have one initial scene. The following checks to see
         * if a scene already exists (deterimed by if the stage variable has been set) and if so 
         * terminates the whole application with an error code (-1). */        
        if (stage != null)
        {
            System.out.println("Error, duplicate controller - terminating application!");
            System.exit(-1);
        }        

    } 

    @FXML   void initialize()           // The method automatically called by JavaFX after the constructor.
    {            
        /* The following assertions check to see if the JavaFX controls exists. If one of these fails, the
         * application won't work. If the control names in Scene Builder don't match the variables this fails. */ 
        System.out.println("Asserting controls...");
        try
        {
            assert mainListView != null : "Can't find mainListView";
            assert addButton != null : "Can't find addButton";
            assert playButton != null : "Can't find playButton";
            assert stopButton != null : "Can't find stopButton";
            assert editButton != null : "Can't find editButton";
            assert deleteButton != null : "Can't find deleteButton";
            assert searchTextField != null : "Can't find searchTextField";
            assert searchButton != null : "Can't find searchButton";
            assert clearButton != null : "Can't find clearButton";
            assert exitButton != null : "Can't find exitButton";
            assert borderPane != null : "Can't find border pane.";   
        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }

        /* Next, we load the list of fruit from the database and populate the listView. */
        System.out.println("Populating scene with items from the database...");        
        @SuppressWarnings("unchecked")
        List<Track> targetList = mainListView.getItems();  // Grab a reference to the listView's current item list.
        Track.readAll(targetList);                     // Hand over control to the fruit model to populate this list.*/           
    }

    /* In order to catch stage events (the main example being the close (X) button being clicked) we need
     * to setup event handlers. This happens after the constructor and the initialize methods. Once this is
     * complete, the scene is fully loaded and ready to use. */
    public void prepareStageEvents(Stage stage)
    {
        System.out.println("Preparing stage events...");

        PrimarySceneController.stage = stage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Close button was clicked!");
                    Application.terminate();
                }
            });
    }       

    /* The next three methods are event handlers for clicking on the buttons. 
     * The names of these methods are set in Scene Builder so they work automatically. */    
    @FXML   void yesClicked()
    {
        System.out.println("Yes was clicked!");        
    }

    @FXML   void addClicked()
    {
        System.out.println("Add was clicked, opening secondary scene.");
        openNewScene(0);
    }

    @FXML   void editClicked()
    {
        System.out.println("Edit was clicked, opening secondary scene.");
        Track selectedItem = (Track) mainListView.getSelectionModel().getSelectedItem();
        openNewScene(selectedItem.trackID);
    }

    @FXML   void deleteClicked()
    {
        int n = JOptionPane.showConfirmDialog(
                null, "Are you sure you want to delete this?","Delete",JOptionPane.YES_NO_OPTION);

        if(n == JOptionPane.YES_OPTION)
        {
            System.out.println("Delete was clicked!");

            Track selectedItem = (Track) mainListView.getSelectionModel().getSelectedItem();
            Track.deleteById(selectedItem.trackID);
            initialize();
            JOptionPane.showMessageDialog(null, "File Deleted");
        }

    }

    @FXML   void clearClicked()
    {
        System.out.println("Clear was clicked - this feature is not yet implemented!");        
    }

    @FXML   void playClicked()
    {
        System.out.println("Play was clicked"); 
        String uriString = new File("C:/Users/Public/Music/Sample Music/Rae Sremmurd - Black Beatles ft.mp3").toURI().toString();
        MediaPlayer player = new MediaPlayer( new Media(uriString));
        player.play();
    }

    @FXML   void stopClicked()
    {
        System.out.println("Stop was clicked");
        }

    @FXML   void searchChoiceBoxClicked()
    {
        System.out.println("searchChoiceBox was clicked - this feature is not yet implemented!");        
    }

    @FXML   void goClicked()
    {
        System.out.println("Go was clicked - this feature is not yet implemented!");
    }

    @FXML   void exitClicked()
    {
        System.out.println("Exit was clicked!");        
        Application.terminate();        // Call the terminate method in the main Application class.
    }

    /* This method, set in SceneBuilder to occur when the listView is clicked, establishes which
     * item in the view is currently selected (if any) and outputs it to the console. */    
    @FXML   void listViewClicked()
    {
        Track selectedItem = (Track) mainListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            System.out.println("Nothing selected!");
        }
        else
        {
            System.out.println(selectedItem + " (trackID: " + selectedItem.trackID + ") is selected.");
        }
    }

    void openNewScene(int trackID)
    {

        FXMLLoader loader = new FXMLLoader(Application.class.getResource("SecondaryScene.fxml"));

        try
        {
            Stage stage2 = new Stage();
            stage2.setTitle("Edit");
            stage2.setScene(new Scene(loader.load()));
            stage2.show();           
            SecondarySceneController controller2 = loader.getController();
            controller2.prepareStageEvents(stage2);

            controller2.setParent(this);
            if (trackID != 0) controller2.loadItem(trackID);            

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

}

