/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Month;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.JTextField;
//tryagain commit

/**
 *
 * @author Felhasználó
 */
class GameEngine extends JPanel {

    private final int FPS = 240;
    private final int FIELDSIZE = 80;
    private final int FIELDROWSNUM = 8;
    private final int FIELDCOLSNUM = 16;
    private final int INITIALMONEY = 100000;
    private final int INITIALRESIDENT = 20;
    private final int ZONEPRICE = 250;
    private final int ROADPRICE = 75;
    private final int STADIUMPRICE = 1000;
    private final int POLICESTATIONPRICE = 750;
    private final int FIRESTATIONPRICE = 750;
    private final int FORESTPRICE = 500;
    private final double ANNUALFEEPERCENTAGE = 0.3; // playerBuildIt annualFee = price * ANNUALFEEPERCENTAGE
    private final int RESIDENTCAPACITY = 5;
    private final int WORKPLACECAPACITY = 18;
    private final double REFUND = 0.4;
    private final int RADIUS = 3;
    private final int CRITSATISFACTION = -5;
    private final int MOVEINATLEASTSATISFACTION = 5;
    private final double CHANCEOFFIRE = 0.05;
    private final JFrame saveGameFrame = new JFrame("Save game");
    private final JFrame loadGameFrame = new JFrame("Load game");
    private final JPanel saveGamePanel = new JPanel();
    private final JPanel loadGamePanel = new JPanel();

    private City city;
    private Date time;
    private boolean paused = false;
    private int speed;
    private final int[] minutesPerSecondIfSpeedIsIndex = { 180, 2880, 43200 }; // 3 ora, 2 nap, 30 nap
    private final Image background;
    private final Timer newFrameTimer;
    private final Timer gameTickTimer;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public int screenWidth = screenSize.width;
    public int screenHeight = screenSize.height;

    private final JButton pauseButton, timeSlowButton, timeAccButton, taxUpButton, taxDownButton, destroyButton,
            nominateIndButton, nominateResButton, nominateSerButton, buildRoadButton, buildStadiumButton, buildPSButton,
            buildFSButton, buildForestButton, showBudgetButton, fireFightingButton, zoneInfoButton, saveGameButton,
            loadGameButton;
    private final JLabel moneyLabel, taxLabel, dateLabel, satisfactionLabel, residentNumLabel;
    private int prevSelectedFieldX = -1;
    private int prevSelectedFieldY = -1;

    public GameEngine() {
        super();
        background = new ImageIcon("data/graphics/other/background.jpeg").getImage();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!paused) {
                    // System.out.println("Clicked! Position: x = " + e.getX() + " y = " +
                    // e.getY());
                    fieldSelect(e.getX(), e.getY());
                }
            }
        });

        newGame();

        this.pauseButton = new JButton("pause");
        pauseButton.addActionListener((ActionEvent ae) -> togglePause());
        this.add(pauseButton);

        this.timeSlowButton = new JButton("slow Time");
        timeSlowButton.addActionListener((ActionEvent ae) -> slowDownTime());
        this.add(timeSlowButton);

        this.timeAccButton = new JButton("acc Time");
        timeAccButton.addActionListener((ActionEvent ae) -> speedUpTime());
        this.add(timeAccButton);

        this.taxUpButton = new JButton("inc Tax");
        taxUpButton.addActionListener((ActionEvent ae) -> increaseTax());
        this.add(taxUpButton);

        this.taxDownButton = new JButton("lower Tax");
        taxDownButton.addActionListener((ActionEvent ae) -> lowerTax());
        this.add(taxDownButton);

        this.destroyButton = new JButton("Destroy");
        destroyButton.addActionListener((ActionEvent ae) -> tryDenominateOrDestroyZone());
        this.add(destroyButton);

        this.nominateIndButton = new JButton("nominate industrial");
        nominateIndButton.addActionListener((ActionEvent ae) -> city.selectField(IndustrialZone.class));
        this.add(nominateIndButton);

        this.nominateResButton = new JButton("nominate residential");
        nominateResButton.addActionListener((ActionEvent ae) -> city.selectField(ResidentialZone.class));
        this.add(nominateResButton);

        this.nominateSerButton = new JButton("nominate service");
        nominateSerButton.addActionListener((ActionEvent ae) -> city.selectField(ServiceZone.class));
        this.add(nominateSerButton);

        this.buildRoadButton = new JButton("build Road");
        buildRoadButton.addActionListener((ActionEvent ae) -> city.build(Road.class));
        this.add(buildRoadButton);

        this.buildStadiumButton = new JButton("build Stad");
        buildStadiumButton.addActionListener((ActionEvent ae) -> city.build(Stadium.class));
        this.add(buildStadiumButton);

        this.buildPSButton = new JButton("build PS");
        buildPSButton.addActionListener((ActionEvent ae) -> city.build(PoliceStation.class));
        this.add(buildPSButton);

        this.buildFSButton = new JButton("build FS");
        buildFSButton.addActionListener((ActionEvent ae) -> city.build(FireStation.class));
        this.add(buildFSButton);

        this.buildForestButton = new JButton("build Forest");
        buildForestButton.addActionListener((ActionEvent ae) -> city.build(Forest.class));
        this.add(buildForestButton);

        this.showBudgetButton = new JButton("show budget");
        showBudgetButton.addActionListener((ActionEvent ea) -> new PopupInfo(new JFrame(), budgetInfo(), "Budget"));
        this.add(showBudgetButton);

        this.fireFightingButton = new JButton("fire-fighting");
        fireFightingButton.addActionListener((ActionEvent ea) -> city.fireFighting());
        this.add(fireFightingButton);

        saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener((ActionEvent ea) -> initSave());
        this.add(saveGameButton);

        loadGameButton = new JButton("Load Game");
        JComboBox<String> savesList = new JComboBox();
        loadGameButton.addActionListener((ActionEvent ea) -> initLoad(savesList));
        this.add(loadGameButton);

        JTextField saveNameTextField = new JTextField();
        JButton confirmSaveButton = new JButton("Save");
        confirmSaveButton.addActionListener((var ae) -> saveGame(saveNameTextField.getText() + ".sav"));
        saveGamePanel.add(saveNameTextField);
        saveNameTextField.setPreferredSize(new Dimension(80, 20));
        saveGamePanel.add(confirmSaveButton);
        saveGameFrame.add(saveGamePanel);
        saveGameFrame.setPreferredSize(new Dimension(300, 100));

        JButton confirmLoadButton = new JButton("Load");
        confirmLoadButton.addActionListener((var ae) -> loadGame(savesList.getItemAt(savesList.getSelectedIndex())));
        loadGamePanel.add(confirmLoadButton);
        loadGamePanel.add(savesList);
        loadGameFrame.add(loadGamePanel);
        loadGameFrame.setPreferredSize(new Dimension(300, 100));

        this.zoneInfoButton = new JButton("zone info");
        zoneInfoButton.addActionListener((ActionEvent ea) -> zoneInfoPopup());
        this.add(zoneInfoButton);

        this.moneyLabel = new JLabel("Funds: ");
        this.add(moneyLabel);
        this.taxLabel = new JLabel("Tax: ");
        this.add(taxLabel);
        this.satisfactionLabel = new JLabel("City satisfaction: ");
        this.add(satisfactionLabel);
        this.residentNumLabel = new JLabel("Residents: ");
        this.add(residentNumLabel);
        this.dateLabel = new JLabel(time.toString());
        this.add(dateLabel);
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
        gameTickTimer = new Timer(1000, new GameTickListener());
        gameTickTimer.start();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, screenWidth,
                screenHeight, null); // háttérkép kirajzolása
        for (int i = 0; i < FIELDROWSNUM; i++) {
            for (int j = 0; j < FIELDCOLSNUM; j++) {
                if (!city.getFields()[i][j].isFree()) {
                    city.getFields()[i][j].getBuilding().draw(grphcs);
                } else { // Buildable is null so cannot draw it -> have to call Field draw method
                    city.getFields()[i][j].draw(grphcs);
                }
                moneyLabel.setText("Funds: " + city.getBudget() + "$");
                taxLabel.setText("Tax: " + city.getTax() + "$");
                satisfactionLabel.setText("City satisfaction: " + city.getSatisfaction());
                residentNumLabel.setText("Residents: " + city.getResidents().size());
                dateLabel.setText(time.toString());
            }
        }
    }

    void newGame() {
        city = new City(INITIALRESIDENT, FIELDSIZE, FIELDROWSNUM, FIELDCOLSNUM, CRITSATISFACTION,
                MOVEINATLEASTSATISFACTION, INITIALMONEY,
                ZONEPRICE, ROADPRICE, STADIUMPRICE, POLICESTATIONPRICE, FIRESTATIONPRICE, FORESTPRICE,
                ANNUALFEEPERCENTAGE,
                RESIDENTCAPACITY, WORKPLACECAPACITY, REFUND, CHANCEOFFIRE, RADIUS, true);
        // date alaphelyzetbe
        time = new Date(1980, 1, 1, 0, 0);
        paused = false;
        speed = 1;
    }

    private void initSave() {
        paused = true;
        saveGameFrame.pack();
        saveGameFrame.setVisible(true);
    }

    private void saveGame(String saveName) {
        File[] usedFiles = getFiles();
        for (File f : usedFiles)
            if (f.getName().equals(saveName)) {
                JFrame frame = new JFrame("Létező mentés!");
                JPanel panel = new JPanel();
                frame.add(panel);
                panel.add(new JLabel("Felülírod?"));
                JButton confirmButton = new JButton("Igen");
                JButton rejectButton = new JButton("Nem");
                confirmButton.addActionListener((var ae) -> {
                    frame.dispose();
                    saveInto(f);
                    paused = false;
                    saveGameFrame.setVisible(false);
                });
                rejectButton.addActionListener((var ae) -> frame.dispose());
                panel.add(confirmButton);
                panel.add(rejectButton);
                frame.setPreferredSize(new Dimension(300, 100));
                frame.pack();
                frame.setVisible(true);
                return;
            }
        File f = new File("data/persistence/saves/" + saveName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.exit(1);
        }
        saveInto(f);
        paused = false;
        saveGameFrame.setVisible(false);
    }

    private void saveInto(File f) {
        try (PrintWriter pw = new PrintWriter(f)) {
            pw.println(time.toString());
            pw.print(city.saveGame());
        } catch (Exception e) {
            System.exit(1);
        }
    }

    private void initLoad(JComboBox<String> savesList) {
        paused = true;
        savesList.removeAllItems();
        File[] saves = getFiles();
        for (File f : saves) {
            savesList.addItem(f.getName().substring(0, f.getName().length() - 4));
        }
        loadGameFrame.pack();
        loadGameFrame.setVisible(true);
    }

    private void loadGame(String fileName) {
        try (Scanner s = new Scanner(new File("data/persistence/saves/" + fileName + ".sav"))) {
            time = new Date(s.nextLine());
            city = new City(INITIALRESIDENT, FIELDSIZE, FIELDROWSNUM, FIELDCOLSNUM, CRITSATISFACTION,
                    MOVEINATLEASTSATISFACTION, INITIALMONEY,
                    ZONEPRICE, ROADPRICE, STADIUMPRICE, POLICESTATIONPRICE, FIRESTATIONPRICE, FORESTPRICE,
                    ANNUALFEEPERCENTAGE,
                    RESIDENTCAPACITY, WORKPLACECAPACITY, REFUND, CHANCEOFFIRE, RADIUS, true);
            city.loadGame(s, true);
            loadGameFrame.setVisible(false);
            paused = false;
            speed = 1;
            prevSelectedFieldX = -1;
            prevSelectedFieldY = -1;
            repaint();
        } catch (FileNotFoundException e) {
            System.exit(100);
        }
    }

    private File[] getFiles() {
        return new File("data/persistence/saves").listFiles();
    }

    private void speedUpTime() {
        if (speed < 3) {
            speed++;
        }
    }

    private void slowDownTime() {
        if (speed > 1) {
            speed--;
        }
    }

    private void togglePause() {
        paused = !paused;
    }

    private void increaseTax() {
        city.increaseTax();
    }

    private void lowerTax() {
        city.lowerTax();
    }

    private void fieldSelect(int mouseX, int mouseY) {
        int fieldIndexX, fieldIndexY;

        fieldIndexX = (int) Math.floor((mouseX - city.getxOffset()) / (double) FIELDSIZE);
        fieldIndexY = (int) Math.floor((mouseY - city.getyOffset()) / (double) FIELDSIZE);

        if (fieldIndexX >= 0 && fieldIndexX < FIELDCOLSNUM && fieldIndexY >= 0 && fieldIndexY < FIELDROWSNUM) {
            if (prevSelectedFieldX != -1 && prevSelectedFieldY != -1) { // ha már van selected Field
                if (city.getFields()[prevSelectedFieldY][prevSelectedFieldX].isFree()) {
                    city.getFields()[prevSelectedFieldY][prevSelectedFieldX].unselect();
                } else {
                    boolean accessible = true;
                    if (city.getFields()[prevSelectedFieldY][prevSelectedFieldX].getBuilding() instanceof Zone zone
                            && !zone.isBuiltUp()
                            && !city.isAccessibleOnRoad(city.getFields()[prevSelectedFieldY][prevSelectedFieldX]))
                        accessible = false;
                    city.getFields()[prevSelectedFieldY][prevSelectedFieldX].getBuilding().unselect(accessible);
                }
            }
            if (city.getFields()[fieldIndexY][fieldIndexX].isFree()) {
                city.getFields()[fieldIndexY][fieldIndexX].select();
            } else {
                boolean accessible = true;
                if (city.getFields()[fieldIndexY][fieldIndexX].getBuilding() instanceof Zone zone && !zone.isBuiltUp()
                        && !city.isAccessibleOnRoad(city.getFields()[fieldIndexY][fieldIndexX]))
                    accessible = false;
                city.getFields()[fieldIndexY][fieldIndexX].getBuilding().select(accessible);
            }

            city.fieldSelect(fieldIndexX, fieldIndexY);
            // elmenteni x, y indexeket, hogy legközelebbi kiválasztáskor visszarakja
            // unselected-re
            prevSelectedFieldX = fieldIndexX;
            prevSelectedFieldY = fieldIndexY;
        }
    }

    // itt minden a ( city.selectedField : Field )-del dolgozik

    private void nominateAsIndustrialZone() {
        city.nominateAsIndustrialZone();
    }

    private void nominateAsServiceZone() {
        city.nominateAsServiceZone();
    }

    private void nominateAsResidentialZone() {
        city.nominateAsResidentialZone();
    }

    private void buildRoad() {
        city.buildRoad();
    }

    private void buildPoliceStation() {
        city.buildPoliceStation();
    }

    private void buildFireStation() {

    }

    private void buildStadium() {
        city.buildStadium();
    }

    private void tryDenominateOrDestroyZone() {
        city.tryDenominateOrDestroyZone();
    }

    private String budgetInfo() { // city.getTax()*city.getResidentsNum()*2, mert munkahely + lakóhely, nyugdíj
                                  // bezavarhat majd (aki már nem dolgozik)
        return "<html><h2><font color=#00a605>Annual incomes</font><h2></html>\nResident tax: " + city.getTax()
                + "$/residential  (" + city.getResidentsNum() +
                " residents)\nEmployer tax: " + city.getTax() + "$/employer  (" + city.getResidentsNum()
                + " employers)\n\n" +
                "<html><h2><font color=#fc1c03>Annual outcomes</font></h2></html>\nRoad maintenance fee: "
                + city.getAnnualFee(ROADPRICE) + "$/road  (" + city.countField(Road.class) +
                " roads)\nStadium maintenance fee: " + city.getAnnualFee(STADIUMPRICE) + "$/stadium  ("
                + city.countField(Stadium.class) + " stadiums)" +
                "\nPolice station maintenance fee: " + city.getAnnualFee(POLICESTATIONPRICE) + "$/police station  ("
                + city.countField(PoliceStation.class) + " police stations)" +
                "\nFire station maintenance fee: " + city.getAnnualFee(FIRESTATIONPRICE) + "$/fire station  ("
                + city.countField(FireStation.class) + " fire stations)\n\n" +
                "<html><h3><font color=#00a605>Annual income:</font></h3></html>\n+ $"
                + city.getTax() * city.getResidentsNum() * 2 +
                "\n<html><h3><font color=#fc1c03>Annual outcome:</font></h3></html>\n- $" + city.calculateAnnualFee();
    }

    private void zoneInfoPopup() {
        if (zoneInfo() == null || zoneInfo() == "")
            return;
        Zone selectedZone = (Zone) city.getFields()[prevSelectedFieldY][prevSelectedFieldX].getBuilding();
        String title = (selectedZone instanceof ResidentialZone ? "Residential zone (" : "Workplace zone (")
                + selectedZone.getPeopleNum() + "/" + selectedZone.getCapacity() + ")";
        new PopupInfo(null, zoneInfo(), title);
    }

    public static boolean showConfirmationDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    private String zoneInfo() {
        if (prevSelectedFieldX != -1 && prevSelectedFieldY != -1
                && !city.getFields()[prevSelectedFieldY][prevSelectedFieldX].isFree()
                && city.getFields()[prevSelectedFieldY][prevSelectedFieldX].getBuilding() instanceof Zone) {
            Field selectedField = city.getFields()[prevSelectedFieldY][prevSelectedFieldX];
            Buildable selectedBuilding = selectedField.getBuilding();
            if (selectedBuilding instanceof ResidentialZone) {
                int currentPeople = ((ResidentialZone) selectedBuilding).getPeopleNum();
                String residents = "\n<html><h2><font color=#00a605>Residents</font></h2></html>\n";
                int number = 0;
                for (Resident person : city.getResidents()) {
                    if (person.getHome() == ((ResidentialZone) selectedBuilding)) {
                        residents += "#" + ++number + ": " + person.toString() + "\n";
                    }
                }
                return residents;
            } else if (selectedBuilding instanceof Workplace) {
                int currentPeople = ((Workplace) selectedBuilding).getPeopleNum();
                String residents = "\n<html><h2><font color=#00a605>Employers</font></h2></html>\n";
                int number = 0;
                for (Resident person : city.getResidents()) {
                    if (person.getWorkplace() == (Workplace) selectedBuilding) {
                        residents += "#" + ++number + ": " + person.toString() + "\n";
                    }
                }
                return residents;
            } else {
                throw new IllegalArgumentException("Selected field must be ResidentialZone or Workplace!");
            }
        }
        return "";
    }

    private void gameOver() {
        new PopupInfo(new JFrame(), "You lost!\nCity satisfaction is critical.", "Game over");
        newGame();
    }

    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {

                repaint();
            }
        }
    }

    class GameTickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                int howManyMinutes = minutesPerSecondIfSpeedIsIndex[speed - 1];
                Date prevTime = new Date(time.getYear(), time.getMonth(), time.getDay(), time.getHour(),
                        time.getMinute());
                time.nMinutesElapsed(howManyMinutes);
                if (prevTime.getDay() != time.getDay()) {
                    city.dayElapsed(new Date(time));
                }
                if (prevTime.getMonth() != time.getMonth()) {
                    city.monthElapsed(new Date(time));
                }
                if (prevTime.getYear() != time.getYear()) {
                    city.yearElapsed();
                }

                int modelPerformTicks = time.howManyDaysPassed(prevTime);
                city.performTicks(modelPerformTicks);
                if (city.getSatisfaction() <= CRITSATISFACTION)
                    gameOver();
            }
        }
    }

    public City getCity() {
        return city;
    }
}
