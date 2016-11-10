import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.Random;
import java.io.*; // for sound
import javax.sound.sampled.*; // for sound

public class Foothill
{
   public static void main(String[] args) throws IOException
   {
      final int HOR_APP_SIZE = 750, VERT_APP_SIZE = 425;
      
      // establish main frame in which program will run
      CasinoFrame myCasinoFrame = new CasinoFrame("Foothill Casino");
      myCasinoFrame.setSize(HOR_APP_SIZE, VERT_APP_SIZE);
      myCasinoFrame.setLocationRelativeTo(null);
      myCasinoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // show everything to the user
      myCasinoFrame.setVisible(true);
   }
}
                                                      // TODO: dereference methods by class
// CasinoFrame class is derived from JFrame class
class CasinoFrame extends JFrame
{
   Random randomGenerator = new Random();
   
   private int bet, winnings;
   private JButton btnMyButton;
   private JTextField txtMyTextArea;
   private JLabel lblReel1, lblReel2, lblReel3;
   private JLabel lblUserMsg, lblBet, lblWinnings;
   private JPanel reel1, reel2, reel3;
   private JPanel panelReels, panelMiddle, panelBottom, panelBet, panelMsg;
   
   // class constants
   // slot machine related constants
   static final int MAX_BET = 50;
   static final double BAR_RATE = 0.40;
   static final double CHERRY_RATE = 0.30;
   static final double SPACE_RATE = 0.05;
   static final double SEVENS_RATE = 0.25; // reference only, not used
   private static final String BAR_STR = "BAR";
   private static final String CHERRY_STR = "cherry";
   private static final String SPACE_STR = "(space)";
   private static final String SEVEN_STR = "7";
   static final String REEL_DEFAULT = SPACE_STR;
   static final int CH_MULT = 5;
   static final int CH_CH_MULT = 15;
   static final int CH_CH_CH_MULT = 30;
   static final int BAR_BAR_BAR_MULT = 50;
   static final int SEV_SEV_SEV_MULT = 100;
   
   // GUI related constants
   private final String RUN_ONCE_CMD = "PULL";
   private final int HOR_GAP = 10, VERT_GAP = 10;
   private final int REEL_HOR_GAP = 5, REEL_VERT_GAP = 10;
   private final int NUM_REEL_ROWS = 1, NUM_REEL_COLS = 3;
   private final int INIT_WAGER = 5, INIT_WINNINGS = 100;
   static final Color BACKGROUND_COLOR = Color.CYAN;
   static final Color BUTTON_COLOR = Color.GREEN;
   static final String MSG_PAD = "  ";
   static final Font GAME_FONT = new Font("San Serif", Font.BOLD, 20);
   // Default directory for resources is .../workspace/appname/
   static final ImageIcon BAR_IMG = new ImageIcon("bar.jpg");
   static final ImageIcon SEV_IMG = new ImageIcon("seven.png");
   static final ImageIcon CH_IMG = new ImageIcon("cherry.jpg");
   static final ImageIcon SPC_IMG = new ImageIcon("space.jpg");
   static final ImageIcon DEFAULT_IMG = BAR_IMG;
   static final File SOUND_FILE_LOST = new File("EXPLODE.wav");
   static final File SOUND_FILE_WON = new File("CASHREG.wav");
   
   // CasinoFrame constructor
   public CasinoFrame(String title)
   {
      super(title);
      
      bet = INIT_WAGER;
      winnings = INIT_WINNINGS;
      
      // create panelReels (top Panel) with reels and labels
      // note: reels are the randomly spinning symbols of the slot machine
      panelReels = new JPanel( new GridLayout(NUM_REEL_ROWS, NUM_REEL_COLS, 
            HOR_GAP, VERT_GAP));
      panelReels.setBackground(BACKGROUND_COLOR);
      panelReels.setBorder(new TitledBorder("Slot Machine Reels"));
      FlowLayout reelPos = new FlowLayout(FlowLayout.CENTER, 
            REEL_HOR_GAP, REEL_VERT_GAP);
      reel1 = new JPanel(reelPos);
      reel2 = new JPanel(reelPos);
      reel3 = new JPanel(reelPos);
      lblReel1 = new JLabel(DEFAULT_IMG);
      lblReel2 = new JLabel(DEFAULT_IMG);
      lblReel3 = new JLabel(DEFAULT_IMG);
      reel1.add(lblReel1);
      reel2.add(lblReel2);
      reel3.add(lblReel3);
      panelReels.add(reel1); // reels added Left to Right onto panelReels
      panelReels.add(reel2);
      panelReels.add(reel3);
      
      // middle panel contains 2 rows as sub-panels
      panelMiddle = new JPanel( new GridLayout(2,1, HOR_GAP, VERT_GAP));              // final constant
      panelMiddle.setBackground(BACKGROUND_COLOR);
      // top sub-panel of middle panel
      panelMsg = new JPanel( new FlowLayout(FlowLayout.LEFT, HOR_GAP, 0));
      panelMsg.setBackground(BACKGROUND_COLOR);
      lblUserMsg = new JLabel("Click " + RUN_ONCE_CMD + " to try your luck!");
      lblUserMsg.setFont(GAME_FONT);
      panelMsg.add(lblUserMsg);
      panelMiddle.add(panelMsg);
      
      // bottom sub-panel of middle panel contains 3 horizontal sub-panels
      panelBet = new JPanel( new FlowLayout(FlowLayout.LEFT, 0, 0));
      panelBet.setBackground(BACKGROUND_COLOR);
      // left horizontal sub-panel
      lblWinnings = new JLabel(MSG_PAD + "Winnings are $" + winnings);
      lblWinnings.setFont(GAME_FONT); 
      panelBet.add(lblWinnings);
      // middle horizontal sub-panel
      lblBet = new JLabel(MSG_PAD + MSG_PAD + "Bet: $");
      lblBet.setFont(GAME_FONT); 
      panelBet.add(lblBet);
      // right horizontal sub-panel
      txtMyTextArea = new JTextField(4); // input bet sub-panel
      txtMyTextArea.setBackground(BACKGROUND_COLOR);
      txtMyTextArea.setText("" + bet); // pre-load input with bet value
      txtMyTextArea.setFont(GAME_FONT); 
      panelBet.add(txtMyTextArea);      
      panelMiddle.add(panelBet);
      
      // bottom panel
      panelBottom = new JPanel( new GridLayout(1,3, HOR_GAP, VERT_GAP));
      panelBottom.setBackground(BACKGROUND_COLOR);
      panelBottom.add( new JLabel("")); // pad left grid component
      btnMyButton = new JButton(RUN_ONCE_CMD);
      btnMyButton.setFont(GAME_FONT);       
      btnMyButton.setBackground(BUTTON_COLOR);
      panelBottom.add(btnMyButton);
      panelBottom.add( new JLabel("")); // pad button on right to center

      // instantiate and register listener for both button and bet text field
      CasinoListener myListener = new CasinoFrame.CasinoListener();
      btnMyButton.addActionListener( myListener );
      txtMyTextArea.addActionListener( myListener );

      // add to JFrame object
      add(panelReels, BorderLayout.NORTH);
      add(panelMiddle, BorderLayout.CENTER );
      add(panelBottom, BorderLayout.SOUTH);
   }
    
   /**
    * Computes pay multiplier based on TripleString thePull values. Multipliers
    * are defined in CH_MULT, CH_CH_MULT, CH_CH_CH_MULT, BAR_BAR_BAR_MULT and
    * SEV_SEV_SEV_MULT.
    * @param thePull
    * @return specified pay multiplier
    */
   static int getPayMultiplier (TripleString thePull)
   {
      if (thePull.getString1().equals(CHERRY_STR))
      {
         if (thePull.getString2().equals(CHERRY_STR))
         {
            if (thePull.getString3().equals(CHERRY_STR))
               return CH_CH_CH_MULT;
            else
               return CH_CH_MULT;
         }
         else
            return CH_MULT;
      }
      else if (thePull.getString1().equals(BAR_STR)
            && thePull.getString2().equals(BAR_STR)
            && thePull.getString3().equals(BAR_STR))
         return BAR_BAR_BAR_MULT;
      else if (thePull.getString1().equals(SEVEN_STR)
            && thePull.getString2().equals(SEVEN_STR)
            && thePull.getString3().equals(SEVEN_STR))
         return SEV_SEV_SEV_MULT;
      else
         return 0;
   }
   
   /**
    * Fills the three strings of a TripleString object each with a random
    * string given by randString()
    * @return specified TripleString object
    */
   public static TripleString pull()
   {
      TripleString pullVal = new TripleString();

      pullVal.setString1(randString());
      pullVal.setString2(randString());
      pullVal.setString3(randString());
      
      return pullVal;
   }
   
   /**
    * Randomly selects one of the four strings of the slot machine (i.e.,
    * BAR_STR, CHERRY_STR, SPACE_STR, SEVEN_STR) according to the frequency rate
    * defined in BAR_RATE, CHERRY_RATE, SPACE_RATE, and SEVENS_RATE,
    * respectively.
    * @return specified String
    */
   private static String randString()
   {
      final int RAND_SCALER = 1000;
      final int BAR_THRESH = (int) (RAND_SCALER * BAR_RATE);
      final int CHERRY_THRESH = (int) (RAND_SCALER * CHERRY_RATE) + BAR_THRESH;
      final int SPACE_THRESH = (int) (RAND_SCALER * SPACE_RATE) + CHERRY_THRESH;
      // SEV_THRESH not needed for the sole remaining case of SEVEN_STR
      
      // Generates [0, RAND_SCALER - 1] for randNumber
      int randNumber = (int) (RAND_SCALER * Math.random());
      if (randNumber < BAR_THRESH) 
         return BAR_STR;
      if (randNumber < CHERRY_THRESH) 
         return CHERRY_STR;
      if (randNumber < SPACE_THRESH) 
         return SPACE_STR;
      return SEVEN_STR;
   }
   
   // inner class for JButton and JTextField Listener
   class CasinoListener implements ActionListener
   {
      private final int BAD_BET_FLAG = -1;
      
      // event handler for JButton and JTextField
      public void actionPerformed(ActionEvent event)
      {
         if (winnings <= 0.0)
         {
            lblUserMsg.setText("Out of Money! Game Over!!");
            return;
         }
         
         bet = getBet();
         if (bet == BAD_BET_FLAG)
            return;

         TripleString pullString = pull();
         lblReel1.setIcon(getSymbolImg(pullString.getString1()));
         lblReel2.setIcon(getSymbolImg(pullString.getString2()));
         lblReel3.setIcon(getSymbolImg(pullString.getString3()));

         int multiplier = getPayMultiplier(pullString);
         if (multiplier == 0)
         {
            playSound(SOUND_FILE_LOST);
            winnings -= bet;
            lblUserMsg.setText("Sorry, you lose");      
         }
         else
         {
            playSound(SOUND_FILE_WON);
            int amountWon = multiplier * bet;
            winnings += amountWon;
            lblUserMsg.setText("You won $" +  amountWon + " !");                  
         }
         lblWinnings.setText(MSG_PAD + "Winnings are $" + winnings);
      } // end actionPerformed
      
      /**
       * Reads bet from input field. Gives error messages for bets not between 
       * range of  1 and MAX_BET, inclusive. Returns NOT_POSITIVE_FLAG 
       * if input is not a valid number or out of range, otherwise returns bet.
       * @return specified int
       */
      int getBet()
      {
         String currBet = txtMyTextArea.getText();
         try
         {
            bet = (int) Double.parseDouble(currBet);
         }
         catch (NumberFormatException exceptionInfo)
         {
            bet = BAD_BET_FLAG;
         }
         if (bet <= 0 || bet > MAX_BET || bet == BAD_BET_FLAG)
         {
            bet = BAD_BET_FLAG;
            lblUserMsg.setText("Error: Please enter only $1 to $" +
                  MAX_BET + " bets");            
         }
         return bet;
      } // end getBet
      
      /**
       * Returns ImageIcon corresponding to str parameter. Values for str are
       * in final values BAR_STR, CHERRY_STR, SPACE_STR and SEVEN_STR with 
       * corresponding images in final values BAR_IMG, CH_IMG, SPC_IMG and 
       * SEV_IMG, respectively.
       * @param str selects the correct ImageIcon to return
       * @return the specified ImageIcon
       */
      private ImageIcon getSymbolImg(String str)
      {
         if (str.equals(BAR_STR))
            return BAR_IMG;
         if (str.equals(CHERRY_STR))
            return CH_IMG;
         if (str.equals(SPACE_STR))
            return SPC_IMG;
         return SEV_IMG; 
      } // end getSymbolImg
      
      /**
       * Plays File sound asynchronously
       * @param sound File with path to playable file such as .wav 
       */
      private void playSound(File sound)
      {
         // code for this method modified from:
         // www.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
         // try-catch block required to handle exceptions from instantiating
         // an AudioInputStream
         try 
         {
            // Open an audio input stream.           
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(sound); 
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
         } 
         catch (UnsupportedAudioFileException exceptionInfo) 
         {
            exceptionInfo.printStackTrace();
         } 
         catch (IOException exceptionInfo) 
         {
            exceptionInfo.printStackTrace();
         } 
         catch (LineUnavailableException exceptionInfo) 
         {
            exceptionInfo.printStackTrace();
         }
      } // end playSound
      
   } // end inner class CasinoListener   
} // end class CasinoFrame


/**
 * Class object contains three strings of length up to MAX_LEN each with
 * supporting set and get methods.
 * @author Paul Hayter
 *
 */
class TripleString 
{
   public String TO_STRING_PADS = "   ";
   private String string1, string2, string3;
   
   // class constants
   public static final int MAX_LEN = 20;
   public static final String TRIPLESTRING_DEFAULT = "";
   
   TripleString()
   {
      string1 = TRIPLESTRING_DEFAULT;
      string2 = TRIPLESTRING_DEFAULT;
      string3 = TRIPLESTRING_DEFAULT;         
   }
   
   private static boolean validString( String str )
   {
      if (str != null && str.length() <= MAX_LEN)
         return true;
      return false;
   }
   
   // mutators
   boolean setString1( String str )
   {
      if (!TripleString.validString(str))
         return false;
      string1 = str;
      return true;
   }
   
   boolean setString2( String str )
   {
      if (!TripleString.validString(str))
         return false;
      string2 = str;
      return true;
   }
   
   boolean setString3( String str )
   {
      if (!TripleString.validString(str))
         return false;
      string3 = str;
      return true;
   }
   
   // accessors
   String getString1(){ return string1; }
   String getString2(){ return string2; }
   String getString3(){ return string3; }
   
   @Override
   public String toString()
   {
      return string1 + TO_STRING_PADS + string2 + TO_STRING_PADS + string3;
   }
} // end class TripleString