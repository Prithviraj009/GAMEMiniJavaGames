import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class MiniGameApp {
    public static void main(String[] args) {
        new RockPaperScissors();
    }
}

class WhackAMole extends Frame implements ActionListener {
     Button[] buttons = new Button[9];
     int moleIndex = -1;
     Random random = new Random();
     Timer timer;
     int score = 0;
     Label scoreLabel;

    public WhackAMole() {
        setTitle("Whack-A-Mole");
        setSize(500, 500);
        setLayout(new GridLayout(4, 3));		
        setBackground(new Color(255, 228, 196));

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button("");
            buttons[i].setBackground(new Color(211, 211, 211)); 
            buttons[i].setFont(new Font("Arial", Font.BOLD, 16));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        scoreLabel = new Label("Score: 0", Label.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setBackground(new Color(255, 255, 224));
        scoreLabel.setForeground(Color.BLUE);
        add(scoreLabel);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spawnMole();
            }
        }, 0, 1000);

        Button switchGame = new Button("Switch to Rock-Paper-Scissors");
        switchGame.setBackground(new Color(100, 149, 237));
        switchGame.setForeground(Color.WHITE);
        switchGame.setFont(new Font("Arial", Font.BOLD, 16));
        switchGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RockPaperScissors();
            }
        });
        add(switchGame);

        setVisible(true);
    }

    private void spawnMole() {
        if (moleIndex != -1) {
            buttons[moleIndex].setBackground(new Color(211, 211, 211)); 
            buttons[moleIndex].setLabel("");
        }

        moleIndex = random.nextInt(buttons.length);
        buttons[moleIndex].setBackground(Color.GREEN);
        buttons[moleIndex].setLabel("Mole!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();
        if (clickedButton == buttons[moleIndex]) {
            score++;
            scoreLabel.setText("Score: " + score);
            clickedButton.setBackground(new Color(211, 211, 211)); // reset to grey
            clickedButton.setLabel("");
        }
    }
}

class RockPaperScissors extends Frame implements MouseListener {
    Image rockImg, paperImg, scissorImg;
    Canvas rockCanvas, paperCanvas, scissorCanvas;
    TextField userChoiceField, computerChoiceField, resultField;
    String userChoice, computerChoice;
    Random random;

    RockPaperScissors() {
        random = new Random();
        setSize(600, 600);
        setTitle("Rock Paper Scissors Game");
        setLayout(new BorderLayout());
        setBackground(new Color(135, 206, 250));

        Panel imagePanel = new Panel();
        imagePanel.setLayout(new GridLayout(1, 3));

        rockCanvas = createCanvas("rock.png");
        paperCanvas = createCanvas("paper.png");
        scissorCanvas = createCanvas("scissors.png");

        imagePanel.add(rockCanvas);
        imagePanel.add(paperCanvas);
        imagePanel.add(scissorCanvas);

        Panel infoPanel = new Panel();
        infoPanel.setLayout(new GridLayout(4, 1));

        userChoiceField = new TextField("Your Choice: ");
        userChoiceField.setEditable(false);
        computerChoiceField = new TextField("Computer's Choice: ");
        computerChoiceField.setEditable(false);
        resultField = new TextField("Result: ");
        resultField.setEditable(false);

        infoPanel.add(userChoiceField);
        infoPanel.add(computerChoiceField);
        infoPanel.add(resultField);

        Button switchGame = new Button("Switch to Whack-A-Mole");
        switchGame.setBackground(new Color(255, 99, 71));
        switchGame.setForeground(Color.WHITE);
        switchGame.setFont(new Font("Arial", Font.BOLD, 16));
        switchGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new WhackAMole();
            }
        });
        infoPanel.add(switchGame);

        add(imagePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private Canvas createCanvas(String imgPath) {
        Canvas canvas = new Canvas() {
            public void paint(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(imgPath);
                g.drawImage(img, 0, 0, 100, 100, this);
            }
        };
        canvas.setSize(100, 100);
        canvas.addMouseListener(this);
        return canvas;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == rockCanvas) {
            userChoice = "rock";
        } else if (e.getSource() == paperCanvas) {
            userChoice = "paper";
        } else if (e.getSource() == scissorCanvas) {
            userChoice = "scissor";
        }

        userChoiceField.setText("Your Choice: " + userChoice);

        setComputerChoice();

        String result = determineWinner(userChoice, computerChoice);
        resultField.setText("Result: " + result);
        computerChoiceField.setText("Computer's Choice: " + computerChoice);
    }

    private void setComputerChoice() {
        int b = random.nextInt(3);
        if (b == 0) computerChoice = "rock";
        else if (b == 1) computerChoice = "paper";
        else computerChoice = "scissor";
    }

    public String determineWinner(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) return "It's a tie!";
        if (userChoice.equals("rock")) return computerChoice.equals("scissor") ? "You win!" : "You lose!";
        if (userChoice.equals("paper")) return computerChoice.equals("rock") ? "You win!" : "You lose!";
        return computerChoice.equals("paper") ? "You win!" : "You lose!";
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
