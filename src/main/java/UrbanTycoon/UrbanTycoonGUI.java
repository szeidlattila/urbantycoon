package UrbanTycoon;

import javax.swing.JFrame;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.io.File;

public class UrbanTycoonGUI {
    // properties to change for UX
    private final int FIELDSIZE = 60;

    // properties to change in UI
    private final int UIPADDING = 20;
    private final int ACTIONBSIZE = 60;
    private final int CONTROLBSIZE = 60;
    private final int INFOICONSIZE = 36;
    private final int BUTTONPADDING = 10;
    private final int INFOPANELWIDTH = 784;
    private final int INFOPANELHEIGHT = 50;
    private final int SIDEPANELWIDTH = 500;
    private final int INFOPANELPADDING = (INFOPANELHEIGHT - INFOICONSIZE) / 2;
    private final String buttonFilePath = "data/graphics/other/buttons/";
    private final String uiFilePath = "data/graphics/other/UIelements/";

    private final JFrame frame;
    private final JLayeredPane layeredPane;
    private GameEngine gameArea;
    private final JPanel controlPanel, actionPanel, uxPanel, sidePanel, mainMenuSidePanel, interactionPanel,
            loadSavePanel, creditsPanel;
    private final CustomPanel darkenBackground, mainMenuWallpaper, infoPanel, helpPanel;
    private final CustomButton menuButton, gameLogo;

    // screen properties
    private final Dimension screenSize;
    private final int screenWidth;
    private final int screenHeight;

    // action buttons
    private final String[] actionButtonNames = { "industrial", "service", "residental", "road", "stadium", "police",
            "firestation", "forest", "firefighting", "delete" };
    private static ArrayList<CustomButton> actionButtons = new ArrayList<CustomButton>();
    private static CustomButton selectedButton;

    // control buttons
    private final String[] controlButtonNames = { "play", "pause", "accTime", "deaccTime", "taxInc", "taxDec", "info" };
    private final ArrayList<CustomButton> controlButtons = new ArrayList<>();

    // info icons
    private final String[] infoIconNames = { "time", "residents", "residentsSat", "selectedSat", "tax", "money" };
    private final ArrayList<CustomButton> infoIcons = new ArrayList<>();
    private static final ArrayList<CustomLabel> infoLabels = new ArrayList<>();

    // UX buttons
    private final String[] uxButtonNames = { "plusSize", "minusSize", "leftPos", "rightPos", "upPos", "downPos" };
    private final ArrayList<CustomButton> uxButtons = new ArrayList<>();

    // side panel
    private final String[] sidePanelLabelNames = { "Continue", "Restart Level", "Help", "Exit & Save" };
    private final ArrayList<CustomLabelButton> sidePanelLabels = new ArrayList<>();

    // main menu
    private final String[] mainMenuLabelNames = { "New game", "Load Saves", "Credits", "Exit Game" };
    private final ArrayList<CustomLabelButton> mainMenuLabels = new ArrayList<>();

    public UrbanTycoonGUI() {
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;

        // window
        frame = new JFrame("UrbanTycoon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        // layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(screenSize);

        // game area
        gameArea = new GameEngine(screenSize, FIELDSIZE);
        gameArea.setBackground(Color.decode("#78C877"));
        gameArea.setBounds(0, 0, screenWidth, screenHeight);
        gameArea.togglePause();

        // control buttons
        int panelX = UIPADDING;
        controlPanel = new JPanel();
        createButtonPanel(controlButtonNames, CONTROLBSIZE, panelX, controlPanel, 1, "controlButton", controlButtons);

        // action buttons
        actionPanel = new JPanel();
        panelX = screenWidth - ACTIONBSIZE - UIPADDING;
        createButtonPanel(actionButtonNames, ACTIONBSIZE, panelX, actionPanel, 2, "actionButton", actionButtons);

        // info panel
        infoPanel = new CustomPanel();
        createInfoPanel(infoPanel, 3, 16);

        // UX buttons
        uxPanel = new JPanel();
        createUXPanel(uxPanel, 4);

        // menu button
        menuButton = new CustomButton(buttonFilePath + "menu", CONTROLBSIZE, "menu",
                "menu");
        menuButton.setBounds(UIPADDING, UIPADDING, CONTROLBSIZE, CONTROLBSIZE);

        // darken background
        darkenBackground = new CustomPanel();
        sidePanel = new JPanel();
        createSidePanel(darkenBackground, sidePanel);

        // main menu
        mainMenuWallpaper = new CustomPanel();
        mainMenuSidePanel = new JPanel();
        gameLogo = new CustomButton(uiFilePath + "gameLogo", 300, "logo", "logo");
        createMainMenu(mainMenuWallpaper, mainMenuSidePanel, gameLogo);

        // new game panel
        interactionPanel = new JPanel();
        createNewGamePanel(interactionPanel);

        // load saves panel
        loadSavePanel = new JPanel();
        createSavesPanel(loadSavePanel);

        // credits panel
        creditsPanel = new JPanel();
        createCreditsPanel(creditsPanel);

        // help panel
        helpPanel = new CustomPanel();
        createHelpPanel(helpPanel);

        // add the layered pane to the frame and make it visible
        frame.add(layeredPane);
        frame.pack();
        frame.setVisible(true);

    }

    /**
     * add component to layered pane
     * 
     * @param screen
     */
    private void screenController(String screen) {
        switch (screen) {
            case "creditsPanel" -> {
                layeredPane.removeAll();
                layeredPane.add(mainMenuWallpaper, Integer.valueOf(0));
                layeredPane.add(creditsPanel, Integer.valueOf(1));
            }
            case "removeHelpPanel" -> {
                layeredPane.remove(helpPanel);
                layeredPane.add(sidePanel, Integer.valueOf(7));
            }
            case "helpPanel" -> {
                layeredPane.remove(sidePanel);
                layeredPane.add(helpPanel, Integer.valueOf(7));
            }

            case "loadSavePanel" -> {
                layeredPane.removeAll();
                layeredPane.add(mainMenuWallpaper, Integer.valueOf(0));
                layeredPane.add(loadSavePanel, Integer.valueOf(1));
            }
            case "interactionPanel" -> {
                layeredPane.removeAll();
                layeredPane.add(mainMenuWallpaper, Integer.valueOf(0));
                layeredPane.add(interactionPanel, Integer.valueOf(1));
            }
            case "mainMenu" -> {
                layeredPane.removeAll();
                layeredPane.add(mainMenuWallpaper, Integer.valueOf(0));
                layeredPane.add(mainMenuSidePanel, Integer.valueOf(1));
                layeredPane.add(gameLogo, Integer.valueOf(2));
            }
            case "game" -> {
                layeredPane.removeAll();
                layeredPane.add(gameArea, Integer.valueOf(0));
                layeredPane.add(controlPanel, Integer.valueOf(1));
                layeredPane.add(actionPanel, Integer.valueOf(2));
                layeredPane.add(infoPanel, Integer.valueOf(3));
                layeredPane.add(uxPanel, Integer.valueOf(4));
                layeredPane.add(menuButton, Integer.valueOf(5));
                menuButton.setFunc(() -> {
                    screenController("sidePanel");
                });
                gameArea.togglePause();
            }
            case "sidePanel" -> {
                layeredPane.add(darkenBackground, Integer.valueOf(6));
                layeredPane.add(sidePanel, Integer.valueOf(7));
            }
            case "removeSidePanel" -> {
                layeredPane.remove(darkenBackground);
                layeredPane.remove(sidePanel);
            }
            default -> {
            }
        }
    }

    /**
     * create panel for credits
     * 
     * @param creditsPanel
     */

    private void createCreditsPanel(JPanel creditsPanel) {
        int panelX = centerPanelPosition(1000, screenWidth);
        creditsPanel.setBounds(panelX, 0, 1000, screenHeight);
        creditsPanel.setBackground(Color.decode("#404040"));
        creditsPanel.setBorder(BorderFactory.createEmptyBorder(300, 0, 0, 0));
        creditsPanel.setLayout(new BoxLayout(creditsPanel, BoxLayout.Y_AXIS));

        CustomLabel name1 = new CustomLabel("Fekete Martin", 33, "Bold", "center", "loadSavedGame");
        name1.setAlignmentX(Component.CENTER_ALIGNMENT);
        name1.setPreferredSize(new Dimension(1000, 40));
        name1.setMaximumSize(new Dimension(1000, 40));
        name1.setMinimumSize(new Dimension(1000, 40));
        CustomLabel title1 = new CustomLabel("Graphics & UX", 28, "Medium", "center", "loadSavedGame");
        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        title1.setPreferredSize(new Dimension(1000, 32));
        title1.setMaximumSize(new Dimension(1000, 32));
        title1.setMinimumSize(new Dimension(1000, 32));

        CustomLabel name2 = new CustomLabel("Sándor Kornél", 33, "Bold", "center", "loadSavedGame");
        name2.setAlignmentX(Component.CENTER_ALIGNMENT);
        name2.setPreferredSize(new Dimension(1000, 40));
        name2.setMaximumSize(new Dimension(1000, 40));
        name2.setMinimumSize(new Dimension(1000, 40));
        CustomLabel title2 = new CustomLabel("Game mechanics", 28, "Medium", "center", "loadSavedGame");
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setPreferredSize(new Dimension(1000, 32));
        title2.setMaximumSize(new Dimension(1000, 32));
        title2.setMinimumSize(new Dimension(1000, 32));

        CustomLabel name3 = new CustomLabel("Szeidl Attila", 33, "Bold", "center", "loadSavedGame");
        name3.setAlignmentX(Component.CENTER_ALIGNMENT);
        name3.setPreferredSize(new Dimension(1000, 40));
        name3.setMaximumSize(new Dimension(1000, 40));
        name3.setMinimumSize(new Dimension(1000, 40));
        CustomLabel title3 = new CustomLabel("Model & JUnit", 28, "Medium", "center", "loadSavedGame");
        title3.setAlignmentX(Component.CENTER_ALIGNMENT);
        title3.setPreferredSize(new Dimension(1000, 32));
        title3.setMaximumSize(new Dimension(1000, 32));
        title3.setMinimumSize(new Dimension(1000, 32));

        CustomLabelButton back = new CustomLabelButton("Back", 33, "Bold", "center", "back");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setPreferredSize(new Dimension(1000, 40));
        back.setMaximumSize(new Dimension(1000, 40));
        back.setMinimumSize(new Dimension(1000, 40));
        back.setFunc(() -> {
            screenController("mainMenu");
        });
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        creditsPanel.add(name1);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        creditsPanel.add(title1);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        creditsPanel.add(name2);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        creditsPanel.add(title2);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        creditsPanel.add(name3);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        creditsPanel.add(title3);
        creditsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        creditsPanel.add(back);

    }

    /**
     * create panel for info
     * 
     * @param helpPanel
     */
    private void createHelpPanel(CustomPanel helpPanel) {
        int panelX = centerPanelPosition(1400, screenWidth);
        int panelY = centerPanelPosition(1000, screenHeight);
        helpPanel.setBounds(panelX, panelY, 1400, 1000);
        helpPanel.setBackground(new Color(0, 0, 0, 0));
        helpPanel.setImage(uiFilePath + "helpPanel");
        helpPanel.setBorder(BorderFactory.createEmptyBorder(900, 0, 0, 0));
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        CustomLabelButton back = new CustomLabelButton("Back", 33, "Bold", "center", "back");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setPreferredSize(new Dimension(1000, 40));
        back.setMaximumSize(new Dimension(1000, 40));
        back.setMinimumSize(new Dimension(1000, 40));
        back.setFunc(() -> {
            screenController("removeHelpPanel");
        });
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpPanel.add(back);

    }

    /**
     * create panel for saved games
     * 
     * @param savesPanel
     */
    private void createSavesPanel(JPanel savesPanel) {
        int panelX = centerPanelPosition(1000, screenWidth);
        savesPanel.setBounds(panelX, 0, 1000, screenHeight);
        savesPanel.setBackground(Color.decode("#404040"));
        savesPanel.setBorder(BorderFactory.createEmptyBorder(100, 261, 0, 261));
        savesPanel.setLayout(new BorderLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(Color.decode("#404040"));
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        CustomLabel loadSavedGame = new CustomLabel("Load a saved game", 33, "Bold", "center", "loadSavedGame");
        loadSavedGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadSavedGame.setPreferredSize(new Dimension(478, 40));
        loadSavedGame.setMaximumSize(new Dimension(478, 40));
        loadSavedGame.setMinimumSize(new Dimension(478, 40));
        innerPanel.add(loadSavedGame);
        innerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        File[] saves = gameArea.getFiles();
        for (File f : saves) {
            String saveName = f.getName().substring(0, f.getName().length() - 4);
            CustomLabelButton save = new CustomLabelButton(saveName, 30, "Medium", "center", saveName);
            save.setAlignmentX(Component.CENTER_ALIGNMENT);
            save.setPreferredSize(new Dimension(478, 40));
            save.setMaximumSize(new Dimension(478, 40));
            save.setMinimumSize(new Dimension(478, 40));
            save.setCursor(new Cursor(Cursor.HAND_CURSOR));
            save.setFunc(() -> {
                gameArea.setGameName(saveName);
                gameArea.loadGame(saveName);
                screenController("game");
            });
            innerPanel.add(save);
            innerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        CustomLabelButton back = new CustomLabelButton("Back", 33, "Bold", "center", "back");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setPreferredSize(new Dimension(478, 40));
        back.setMaximumSize(new Dimension(478, 40));
        back.setMinimumSize(new Dimension(478, 40));
        back.setFunc(() -> {
            screenController("mainMenu");
        });
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        innerPanel.add(back);
        savesPanel.add(innerPanel);
    }

    private void createMainMenu(CustomPanel mainMenuWallpaper, JPanel mainMenuSidePanel, CustomButton gameLogo) {

        mainMenuWallpaper.setBounds(0, 0, screenWidth, screenHeight);
        mainMenuWallpaper.setBackground(new Color(0, 0, 0, 0));
        mainMenuWallpaper.setImage(uiFilePath + "wallpaper");
        // main menu sidepanel

        mainMenuSidePanel.setBounds(0, 0, SIDEPANELWIDTH, screenHeight);
        mainMenuSidePanel.setBackground(Color.decode("#404040"));
        mainMenuSidePanel.setBorder(
                BorderFactory.createEmptyBorder((screenHeight) - 75 - (mainMenuLabelNames.length * 50), 45, 75, 0));
        mainMenuSidePanel.setLayout(new BoxLayout(mainMenuSidePanel, BoxLayout.Y_AXIS));
        for (int index = 0; index < mainMenuLabelNames.length; index++) {
            CustomLabelButton mainMenuLabel = new CustomLabelButton(mainMenuLabelNames[index], 33, "Bold", "left",
                    mainMenuLabelNames[index]);
            mainMenuLabels.add(mainMenuLabel);
            mainMenuSidePanel.add(mainMenuLabel);
            if (index != mainMenuLabelNames.length - 1) {
                // mainMenuSidePanel.add(Box.createRigidArea(new Dimension(0, BUTTONPADDING)));
            }
        }
        // main menu logo
        gameLogo.setBounds(45, 75, 300, 300);
        gameLogo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * create panel for side menu
     * 
     * @param darkenBackground
     * @param sidePanel
     */
    private void createSidePanel(CustomPanel darkenBackground, JPanel sidePanel) {
        darkenBackground.setBounds(0, 0, screenWidth, screenHeight);
        darkenBackground.setOpaque(false);
        darkenBackground.setImage(uiFilePath + "darkenBackground");
        // side panel
        sidePanel.setBounds(0, 0, SIDEPANELWIDTH, screenHeight);
        sidePanel.setBackground(Color.decode("#404040"));
        sidePanel.setBorder(
                BorderFactory.createEmptyBorder((screenHeight) - 75 - (sidePanelLabelNames.length * 50), 45, 75, 0));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        for (int index = 0; index < sidePanelLabelNames.length; index++) {
            CustomLabelButton sidePanelLabel = new CustomLabelButton(sidePanelLabelNames[index], 33, "Bold", "left",
                    sidePanelLabelNames[index]);
            sidePanelLabels.add(sidePanelLabel);
            sidePanel.add(sidePanelLabel);
            if (index != sidePanelLabelNames.length - 1) {
                // sidePanel.add(Box.createRigidArea(new Dimension(0, BUTTONPADDING)));
            }
        }
    }

    /**
     * create panel to init new game
     * 
     * @param interactionPanel
     */
    private void createNewGamePanel(JPanel interactionPanel) {

        // main menu enter game name
        int panelX = centerPanelPosition(1000, screenWidth);
        interactionPanel.setBounds(panelX, 0, 1000, screenHeight);
        interactionPanel.setBackground(Color.decode("#404040"));
        interactionPanel.setBorder(BorderFactory.createEmptyBorder(100, 261, 300, 261));
        interactionPanel.setLayout(new BorderLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setBackground(Color.decode("#404040"));
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        CustomLabel enterGameName = new CustomLabel("Name your game", 33, "Bold", "center", "enterGameName");
        enterGameName.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterGameName.setPreferredSize(new Dimension(478, 40));
        enterGameName.setMaximumSize(new Dimension(478, 40));
        enterGameName.setMinimumSize(new Dimension(478, 40));

        CustomTextfield gameName = new CustomTextfield(30, "Medium", "center");
        gameName.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameName.setPreferredSize(new Dimension(478, 59));
        gameName.setMaximumSize(new Dimension(478, 59));
        gameName.setMinimumSize(new Dimension(478, 59));
        gameName.setBackground(new Color(0, 0, 0, 0));
        gameName.setBackgroundImage(uiFilePath + "textFieldBackground");

        CustomLabelButton startGame = new CustomLabelButton("Start Game", 33, "Medium", "center", "startGame");
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.setPreferredSize(new Dimension(478, 40));
        startGame.setMaximumSize(new Dimension(478, 40));
        startGame.setMinimumSize(new Dimension(478, 40));
        startGame.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startGame.setFunc(() -> {
            screenController("game");
        });
        CustomLabelButton back = new CustomLabelButton("Back", 33, "Medium", "center", "back");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setPreferredSize(new Dimension(478, 40));
        back.setMaximumSize(new Dimension(478, 40));
        back.setMinimumSize(new Dimension(478, 40));
        back.setFunc(() -> {
            screenController("mainMenu");
        });
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomLabelButton invalidName = new CustomLabelButton("", 33, "Medium", "center", "invalidName");
        invalidName.setAlignmentX(Component.CENTER_ALIGNMENT);
        invalidName.setPreferredSize(new Dimension(478, 40));
        invalidName.setMaximumSize(new Dimension(478, 40));
        invalidName.setMinimumSize(new Dimension(478, 40));
        invalidName.setForeground(Color.RED);

        innerPanel.add(enterGameName);
        innerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        innerPanel.add(gameName);
        innerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        innerPanel.add(startGame);
        innerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        innerPanel.add(back);
        innerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        innerPanel.add(invalidName);
        startGame.setFunc(() -> {
            if (gameName.getText().equals("")) {
                invalidName.setText("Invalid name");
            } else {
                Boolean canStartgame = true;
                File[] usedFiles = gameArea.getFiles();
                for (File file : usedFiles) {
                    if (file.getName().equals(gameName.getText() + ".sav")) {
                        invalidName.setText("Name already used");
                        gameName.setText("");
                        canStartgame = false;
                    }
                }
                if (canStartgame) {
                    gameArea.setGameName(gameName.getText());
                    screenController("game");
                    invalidName.setText("");
                    gameName.setText("");
                }
            }

        });
        interactionPanel.add(innerPanel, BorderLayout.CENTER);

        setUpControlButtons();
        setUpActionButtons();
        setUpInfoPanel();
        setUpUxButtons();
        setUpLabelButtons();
        screenController("mainMenu");
    }

    private void createUXPanel(JPanel uxPanel, int paneIndex) {
        uxPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTONPADDING - 2, 0));
        int panelLength = panelSize(uxButtonNames, CONTROLBSIZE, BUTTONPADDING);
        int uxPanelX = centerPanelPosition(panelLength, screenWidth);
        int uxPanelY = screenHeight - UIPADDING - CONTROLBSIZE;
        uxPanel.setBounds(uxPanelX, uxPanelY, panelLength, CONTROLBSIZE);
        uxPanel.setOpaque(false);
        // uxPanel.setBackground(Color.RED);
        uxPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        for (String uxButtonName : uxButtonNames) {
            String filePath = buttonFilePath + uxButtonName;
            CustomButton uxButton = new CustomButton(filePath, CONTROLBSIZE, "uxButton", uxButtonName);
            uxPanel.add(uxButton);
            uxButtons.add(uxButton);
        }
    }

    private void createInfoPanel(CustomPanel infoPanel, int paneIndex, int textSize) {
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTONPADDING, INFOPANELPADDING));
        int infoPanelX = centerPanelPosition(INFOPANELWIDTH, screenWidth);
        infoPanel.setBounds(infoPanelX, UIPADDING, INFOPANELWIDTH, INFOPANELHEIGHT);
        infoPanel.setBackground(new Color(0, 0, 0, 0));
        infoPanel.setImage(uiFilePath + "infoBackground");
        for (String infoIconName : infoIconNames) {
            String filePath = buttonFilePath + infoIconName;
            CustomButton iconButton = new CustomButton(filePath, INFOICONSIZE, "infoIcon", infoIconName);
            iconButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            infoIcons.add(iconButton);
            infoPanel.add(iconButton);
            CustomLabel infoLabel = new CustomLabel("No info", textSize, "Bold", "left", infoIconName);
            infoLabels.add(infoLabel);
            infoPanel.add(infoLabel);
        }
    }

    private void createButtonPanel(String[] buttonNames, int BUTTONSIZE, int panelX, JPanel buttonPanel,
            int paneIndex, String type, ArrayList<CustomButton> buttons) {
        int panelLength = panelSize(buttonNames, BUTTONSIZE, BUTTONPADDING);
        int panelCenterY = centerPanelPosition(panelLength, screenHeight);
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(panelX, panelCenterY, BUTTONSIZE, panelLength);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        for (int i = 0; i < buttonNames.length; i++) {
            String filePath = buttonFilePath + buttonNames[i];
            CustomButton button = new CustomButton(filePath, BUTTONSIZE,
                    type,
                    buttonNames[i]);
            buttons.add(button);
            buttonPanel.add(button);
            if (i != (buttonNames.length - 1)) {
                buttonPanel.add(Box.createRigidArea(new Dimension(0, BUTTONPADDING)));
            }
        }
    }

    private int panelSize(String[] arr, int buttonSize, int buttonPadding) {
        return (arr.length * buttonSize + (arr.length - 1) * buttonPadding);
    }

    private int centerPanelPosition(int panelSize, int screenSize) {
        return ((screenSize - panelSize) / 2);
    }

    private static CustomButton getButtonByName(String name, ArrayList<CustomButton> arr) {
        for (CustomButton customButton : arr) {
            if (customButton.getName() == null ? name == null : customButton.getName().equals(name)) {
                return customButton;
            }
        }
        return null;
    }

    private static CustomLabel getLabelByType(String type, ArrayList<CustomLabel> arr) {
        for (CustomLabel customLabel : arr) {
            if (customLabel.getType() == null ? type == null : customLabel.getType().equals(type)) {
                return customLabel;
            }
        }
        return null;
    }

    private static CustomLabelButton getLabelButtonByType(String type, ArrayList<CustomLabelButton> arr) {
        for (CustomLabelButton customLabel : arr) {
            if (customLabel.getType() == null ? type == null : customLabel.getType().equals(type)) {
                return customLabel;
            }
        }
        return null;
    }

    // starting position

    private void setUpLabelButtons() {
        // side panel
        // private final String[] sidePanelLabelNames = { "Continue", "Restart Level",
        // "Help", "Settings", "Exit & Save" };
        // main menu
        // private final String[] mainMenuLabelNames = { "New game", "Load Saves",
        // "Credits", "Settings", "Exit Game" };
        CustomLabelButton newGameButton = getLabelButtonByType("New game", mainMenuLabels);
        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newGameButton.setFunc(() -> {
            // screenController("game");
            screenController("interactionPanel");
        });
        CustomLabelButton loadSavesButton = getLabelButtonByType("Load Saves", mainMenuLabels);
        loadSavesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadSavesButton.setFunc(() -> {
            screenController("loadSavePanel");
        });
        CustomLabelButton creditsButton = getLabelButtonByType("Credits", mainMenuLabels);
        creditsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        creditsButton.setFunc(() -> {
            screenController("creditsPanel");
        });
        CustomLabelButton exitGameButton = getLabelButtonByType("Exit Game", mainMenuLabels);
        exitGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitGameButton.setFunc(() -> {
            System.exit(0);
        });
        CustomLabelButton continueButton = getLabelButtonByType("Continue", sidePanelLabels);
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueButton.setFunc(() -> {
            screenController("removeSidePanel");
        });
        CustomLabelButton restartLevelButton = getLabelButtonByType("Restart Level", sidePanelLabels);
        restartLevelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartLevelButton.setFunc(() -> {
            gameArea.newGame();
            screenController("removeSidePanel");
        });
        CustomLabelButton helpButton = getLabelButtonByType("Help", sidePanelLabels);
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpButton.setFunc(() -> {
            screenController("helpPanel");
        });
        CustomLabelButton exitSaveButton = getLabelButtonByType("Exit & Save", sidePanelLabels);
        exitSaveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitSaveButton.setFunc(() -> {
            gameArea.saveGame(gameArea.getGameName() + ".sav");
            loadSavePanel.removeAll();
            createSavesPanel(loadSavePanel);
            screenController("mainMenu");
        });
    }

    private void setUpUxButtons() {
        CustomButton plusSize = getButtonByName("plusSize", uxButtons);
        plusSize.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton minusSize = getButtonByName("minusSize", uxButtons);
        minusSize.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton rightPos = getButtonByName("rightPos", uxButtons);
        rightPos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton leftPos = getButtonByName("leftPos", uxButtons);
        leftPos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton upPos = getButtonByName("upPos", uxButtons);
        upPos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton downPos = getButtonByName("downPos", uxButtons);
        downPos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        plusSize.setFunc(() -> {
            changeCellsSize(10);
        });
        minusSize.setFunc(() -> {
            changeCellsSize(-10);
        });
        rightPos.setFunc(() -> {
            changeCellsPositions(10, 0);
        });
        leftPos.setFunc(() -> {
            changeCellsPositions(-10, 0);
        });
        upPos.setFunc(() -> {
            changeCellsPositions(0, -10);
        });
        downPos.setFunc(() -> {
            changeCellsPositions(0, 10);
        });
    }

    private void setUpControlButtons() {
        CustomButton play = getButtonByName("play", controlButtons);
        CustomButton pause = getButtonByName("pause", controlButtons);
        play.disable();
        play.setFunc(() -> {
            gameArea.togglePause();
            pause.enable();
            play.disable();
        });
        pause.setFunc(() -> {
            gameArea.togglePause();
            play.enable();
            pause.disable();
        });

        CustomButton accTime = getButtonByName("accTime", controlButtons);
        CustomButton deaccTime = getButtonByName("deaccTime", controlButtons);
        deaccTime.disable();
        deaccTime.setFunc(() -> {
            gameArea.slowDownTime();
            if (gameArea.getSpeed() == 1) {
                deaccTime.disable();
            }
            accTime.enable();
        });
        accTime.setFunc(() -> {
            gameArea.speedUpTime();
            if (gameArea.getSpeed() == 3) {
                accTime.disable();
            }
            deaccTime.enable();
        });

        CustomButton taxInc = getButtonByName("taxInc", controlButtons);
        CustomButton taxDec = getButtonByName("taxDec", controlButtons);
        CustomButton info = getButtonByName("info", controlButtons);
        taxInc.setFunc(() -> gameArea.increaseTax());
        taxDec.setFunc(() -> gameArea.lowerTax());
        info.setFunc(() -> gameArea.zoneInfoPopup());

    }

    private void setUpActionButtons() {
        // remove chechActionPrice() from all buttons
        City city = gameArea.getCity();
        CustomButton industrial = getButtonByName("industrial", actionButtons);
        industrial.setFunc(() -> {
            if (!industrial.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = industrial;
                industrial.select();
                gameArea.setSelectedFunction(
                        () -> city.selectField(IndustrialZone.class));
            } else {
                selectedButton = null;
                industrial.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton service = getButtonByName("service", actionButtons);
        service.setFunc(() -> {
            if (!service.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = service;
                service.select();
                gameArea.setSelectedFunction(
                        () -> city.selectField(ServiceZone.class));
            } else {
                selectedButton = null;
                service.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton residential = getButtonByName("residental", actionButtons);
        residential.setFunc(() -> {
            if (!residential.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = residential;
                residential.select();
                gameArea.setSelectedFunction(
                        () -> city.selectField(ResidentialZone.class));
            } else {
                selectedButton = null;
                residential.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton road = getButtonByName("road", actionButtons);
        road.setFunc(() -> {
            if (!road.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = road;
                road.select();
                gameArea.setSelectedFunction(
                        () -> city.build(Road.class));
            } else {
                selectedButton = null;
                road.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton stadium = getButtonByName("stadium", actionButtons);
        stadium.setFunc(() -> {
            if (!stadium.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = stadium;
                stadium.select();
                gameArea.setSelectedFunction(
                        () -> city.build(Stadium.class));
            } else {
                selectedButton = null;
                stadium.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton police = getButtonByName("police", actionButtons);
        police.setFunc(() -> {
            if (!police.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = police;
                police.select();
                gameArea.setSelectedFunction(
                        () -> city.build(PoliceStation.class));
            } else {
                selectedButton = null;
                police.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton fireStation = getButtonByName("firestation", actionButtons);
        fireStation.setFunc(() -> {
            if (!fireStation.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = fireStation;
                fireStation.select();
                gameArea.setSelectedFunction(
                        () -> city.build(FireStation.class));
            } else {
                if (selectedButton != null)
                    selectedButton = null;
                fireStation.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton forest = getButtonByName("forest", actionButtons);
        forest.setFunc(() -> {
            if (!forest.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = forest;
                forest.select();
                gameArea.setSelectedFunction(
                        () -> city.build(Forest.class));
            } else {
                selectedButton = null;
                forest.unselect();
                gameArea.setSelectedFunction(null);
            }
        });
        CustomButton firefighting = getButtonByName("firefighting", actionButtons);
        firefighting.setFunc(() -> {
            if (!firefighting.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = firefighting;
                firefighting.select();
                gameArea.setSelectedFunction(
                        () -> city.fireFighting());
            } else {
                selectedButton = null;
                firefighting.unselect();
                gameArea.setSelectedFunction(null);
            }

        });

        CustomButton delete = getButtonByName("delete", actionButtons);
        delete.setFunc(() -> {
            if (!delete.isSelected()) {
                if (selectedButton != null)
                    selectedButton.unselect();
                selectedButton = delete;
                delete.select();
                gameArea.setSelectedFunction(
                        () -> gameArea.tryDenominateOrDestroyZone());
            } else {
                selectedButton = null;
                delete.unselect();
                gameArea.setSelectedFunction(null);
            }

        });
    }

    private void setUpInfoPanel() {
        // private final String[] infoIconNames = { "time", "residents", "residentsSat",
        // "selectedSat", "tax", "money" };
        CustomButton tax = getButtonByName("tax", infoIcons);
        tax.setFunc(() -> new PopupInfo(new JFrame(), gameArea.budgetInfo(), "Budget"));
        tax.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton time = getButtonByName("time", infoIcons);
        time.setFunc(() -> {
            changeCellsSize(10);
        });
        time.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton residents = getButtonByName("residents", infoIcons);
        residents.setFunc(() -> {
            changeCellsSize(-10);
        });
        residents.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton residentsSat = getButtonByName("residentsSat", infoIcons);
        residentsSat.setFunc(() -> {
            changeCellsPositions(10, 0);
        });
        residentsSat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CustomButton selectedSat = getButtonByName("selectedSat", infoIcons);
        selectedSat.setFunc(() -> {
            changeCellsPositions(-10, 0);
        });
        selectedSat.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    /**
     * disable buttons if player has not got enough money
     * 
     * @param currentMoney
     * @param zonePrice
     * @param roadPrice
     * @param stadiumPrice
     * @param policePrice
     * @param firestationPrice
     * @param forestPrice
     */
    public static void checkActionPrice(long currentMoney, int zonePrice, int roadPrice, int stadiumPrice,
            int policePrice, int firestationPrice, int forestPrice) {
        CustomButton industrial = getButtonByName("industrial", actionButtons);
        CustomButton service = getButtonByName("service", actionButtons);
        CustomButton residental = getButtonByName("residental", actionButtons);
        CustomButton road = getButtonByName("road", actionButtons);
        CustomButton stadium = getButtonByName("stadium", actionButtons);
        CustomButton police = getButtonByName("police", actionButtons);
        CustomButton fireStation = getButtonByName("firestation", actionButtons);
        CustomButton forest = getButtonByName("forest", actionButtons);

        if (currentMoney < zonePrice && industrial.isEnabled() && service.isEnabled() && residental.isEnabled()) {
            industrial.disable();
            service.disable();
            residental.disable();
        } else if (!industrial.isEnabled() && !service.isEnabled() && !residental.isEnabled()) {
            industrial.enable();
            service.enable();
            residental.enable();
        }

        if (currentMoney < roadPrice && road.isEnabled()) {
            road.disable();
        } else if (!road.isEnabled()) {
            road.enable();
        }

        if (currentMoney < stadiumPrice && stadium.isEnabled()) {
            stadium.disable();
        } else if (!stadium.isEnabled()) {
            stadium.enable();
        }

        if (currentMoney < policePrice && police.isEnabled()) {
            police.disable();
        } else if (!police.isEnabled()) {
            police.enable();
        }

        if (currentMoney < firestationPrice && fireStation.isEnabled()) {
            fireStation.disable();
        } else if (!fireStation.isEnabled()) {
            fireStation.enable();
        }

        if (currentMoney < forestPrice && forest.isEnabled()) {
            forest.disable();
        } else if (!forest.isEnabled()) {
            forest.enable();
        }
    }

    public static void changeLabels(String timeS, int residentsS, int rSatS, String taxS, String moneyS) {
        CustomLabel time = getLabelByType("time", infoLabels);
        CustomLabel residents = getLabelByType("residents", infoLabels);
        CustomLabel residentsSat = getLabelByType("residentsSat", infoLabels);
        CustomLabel selectedSat = getLabelByType("selectedSat", infoLabels);
        CustomLabel tax = getLabelByType("tax", infoLabels);
        CustomLabel money = getLabelByType("money", infoLabels);
        time.setText(timeS);
        residents.setText(residentsS + "");
        residentsSat.setText(rSatS + "");
        tax.setText(taxS);
        money.setText(moneyS);
    }

    /**
     * zoom in / out
     * 
     * @param size
     */
    public void changeCellsSize(int size) {
        Field fields[][] = gameArea.getCity().getFields();
        int CurrentOffsetX = fields[0][0].getX();
        int CurrentOffsetY = fields[0][0].getY();
        int CurrentCellsWidth = fields[0][0].getWidth() * fields[0].length;
        int CurrentCellsHeight = fields[0][0].getHeight() * fields.length;
        int NewCellsWidth = (fields[0][0].getWidth() + size) * fields[0].length;
        int NewCellsHeight = (fields[0][0].getHeight() + size) * fields.length;
        int NewOffsetX = CurrentOffsetX - ((NewCellsWidth - CurrentCellsWidth) / 2);
        int NewOffsetY = CurrentOffsetY - ((NewCellsHeight - CurrentCellsHeight) / 2);

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                Field field = fields[i][j];
                int newX = NewOffsetX + (j * (field.getWidth() + size));
                int newY = NewOffsetY + (i * (field.getHeight() + size));
                if (field.getBuilding() instanceof Stadium) {
                    if (fields[i][j + 1].getBuilding() == field.getBuilding()
                            && fields[i + 1][j].getBuilding() == field.getBuilding()
                            && fields[i + 1][j + 1].getBuilding() == field.getBuilding()) {
                        field.getBuilding().changeSize(size * 2);
                        field.getBuilding().setX(newX);
                        field.getBuilding().setY(newY);
                        field.changeSize(size);
                        field.setX(newX);
                        field.setY(newY);
                    }
                } else if (!field.isFree()) {
                    newX = NewOffsetX + (j * (field.getBuilding().getWidth() + size));
                    newY = NewOffsetY + (i * (field.getBuilding().getHeight() + size));
                    field.getBuilding().changeSize(size);
                    field.getBuilding().setX(newX);
                    field.getBuilding().setY(newY);
                    field.changeSize(size);
                    field.setX(newX);
                    field.setY(newY);
                } else {
                    field.changeSize(size);
                    field.setX(newX);
                    field.setY(newY);
                }
            }
        }

    }

    /**
     * move view left / right
     * 
     * @param x
     * @param y
     */
    public void changeCellsPositions(int x, int y) {
        Field fields[][] = gameArea.getCity().getFields();
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                Field field = fields[i][j];
                if (field.getBuilding() instanceof Stadium) {
                    if (fields[i][j + 1].getBuilding() == field.getBuilding()
                            && fields[i + 1][j].getBuilding() == field.getBuilding()
                            && fields[i + 1][j + 1].getBuilding() == field.getBuilding()) {
                        field.getBuilding().changeX(x);
                        field.getBuilding().changeY(y);
                        field.changeX(x);
                        field.changeY(y);
                    }
                } else if (!field.isFree()) {
                    field.getBuilding().changeX(x);
                    field.getBuilding().changeY(y);
                    field.changeX(x);
                    field.changeY(y);
                } else {
                    field.changeX(x);
                    field.changeY(y);
                }
            }
        }

    }
}
