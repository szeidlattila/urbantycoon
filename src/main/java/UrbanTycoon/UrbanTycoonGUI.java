
package UrbanTycoon;

import javax.swing.JFrame;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

public class UrbanTycoonGUI {
    // properties to change for UX
    private final int FIELDSIZE = 60;
    private final int addedX = 0;
    private final int addedY = 0;

    // properties to change in UI
    private final int UIPADDING = 20;
    private final int ACTIONBSIZE = 60;
    private final int CONTROLBSIZE = 50;
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
    private final GameEngine gameArea;
    private final JPanel controlPanel, actionPanel, uxPanel, sidePanel, mainMenuSidePanel;
    private final CustomPanel darkenBackground, mainMenuWallpaper, infoPanel;
    private final CustomButton menuButton, gameLogo;

    // screen properties
    private final Dimension screenSize;
    private final int screenWidth;
    private final int screenHeight;

    // action buttons
    private final String[] actionButtonNames = { "industrial", "service", "residental", "road", "stadium", "police",
            "firestation", "forest", "firefighting", "delete" };
    private static ArrayList<CustomButton> actionButtons = new ArrayList<CustomButton>();

    // control buttons
    private final String[] controlButtonNames = { "play", "pause", "accTime", "deaccTime", "taxInc", "taxDec", "info",
            "load",
            "save" };
    private final ArrayList<CustomButton> controlButtons = new ArrayList<>();

    // info icons
    private final String[] infoIconNames = { "time", "residents", "residentsSat", "selectedSat", "tax", "money" };
    private final ArrayList<CustomButton> infoIcons = new ArrayList<>();
    private static final ArrayList<CustomLabel> infoLabels = new ArrayList<>();

    // UX buttons
    private final String[] uxButtonNames = { "plusSize", "minusSize", "leftPos", "rightPos", "upPos", "downPos" };
    private final ArrayList<CustomButton> uxButtons = new ArrayList<>();

    // side panel
    private final String[] sidePanelLabelNames = { "Continue", "Restart Level", "Help", "Settings", "Exit & Save" };
    private final ArrayList<CustomLabelButton> sidePanelLabels = new ArrayList<>();

    // main menu
    private final String[] mainMenuLabelNames = { "New game", "Load Saves", "Credits", "Settings", "Exit Game" };
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
        gameArea.setBackground(Color.decode("#4DC25F"));
        gameArea.setBounds(0, 0, screenWidth, screenHeight);

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
        darkenBackground.setBounds(0, 0, screenWidth, screenHeight);
        darkenBackground.setOpaque(false);
        darkenBackground.setImage(uiFilePath + "darkenBackground");
        // side panel
        sidePanel = new JPanel();
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

        // main menu
        mainMenuWallpaper = new CustomPanel();
        mainMenuWallpaper.setBounds(0, 0, screenWidth, screenHeight);
        mainMenuWallpaper.setBackground(new Color(0, 0, 0, 0));
        mainMenuWallpaper.setImage(uiFilePath + "wallpaper");
        // main menu sidepanel
        mainMenuSidePanel = new JPanel();
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
        gameLogo = new CustomButton(uiFilePath + "gameLogo", 300, "logo", "logo");
        gameLogo.setBounds(45, 45, 300, 300);
        gameLogo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        setUpControlButtons();
        setUpActionButtons();
        setUpInfoPanel();
        setUpUxButtons();
        setUpLabelButtons();
        screenController("mainMenu");

        // add the layered pane to the frame and make it visible
        frame.add(layeredPane);
        frame.pack();
        frame.setVisible(true);

        // changeCellsPositions(200, 200);
        // changeCellsSize(30);

    }

    private void screenController(String screen) {
        switch (screen) {
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
            }
            case "sidePanel" -> {
                layeredPane.remove(menuButton);
                layeredPane.add(darkenBackground, Integer.valueOf(5));
                layeredPane.add(sidePanel, Integer.valueOf(6));
                layeredPane.add(menuButton, Integer.valueOf(7));
                menuButton.setFunc(() -> {
                    screenController("removeSidePanel");
                });
            }
            case "removeSidePanel" -> {
                layeredPane.remove(darkenBackground);
                layeredPane.remove(sidePanel);
                layeredPane.remove(menuButton);
                layeredPane.add(menuButton, Integer.valueOf(5));
                menuButton.setFunc(() -> {
                    screenController("sidePanel");
                });
            }
            default -> {
            }
        }
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
            screenController("game");
        });
        CustomLabelButton loadSavesButton = getLabelButtonByType("Load Saves", mainMenuLabels);
        CustomLabelButton creditsButton = getLabelButtonByType("Credits", mainMenuLabels);
        CustomLabelButton settingsButtonSide = getLabelButtonByType("Settings", mainMenuLabels);
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
        CustomLabelButton helpButton = getLabelButtonByType("Help", sidePanelLabels);
        CustomLabelButton settingsButtonMain = getLabelButtonByType("Settings", sidePanelLabels);
        CustomLabelButton exitSaveButton = getLabelButtonByType("Exit & Save", sidePanelLabels);
        exitSaveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitSaveButton.setFunc(() -> {
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
        CustomButton load = getButtonByName("load", controlButtons);
        CustomButton save = getButtonByName("save", controlButtons);
        taxInc.setFunc(() -> gameArea.increaseTax());
        taxDec.setFunc(() -> gameArea.lowerTax());
        info.setFunc(() -> gameArea.zoneInfoPopup());
        load.setFunc(() -> gameArea.initLoad());
        save.setFunc(() -> gameArea.initSave());

    }

    private void setUpActionButtons() {
        // remove chechActionPrice() from all buttons
        City city = gameArea.getCity();
        CustomButton industrial = getButtonByName("industrial", actionButtons);
        industrial.setFunc(() -> {
            city.selectField(IndustrialZone.class);
        });
        CustomButton service = getButtonByName("service", actionButtons);
        service.setFunc(() -> {
            city.selectField(ServiceZone.class);
        });
        CustomButton residential = getButtonByName("residental", actionButtons);
        residential.setFunc(() -> {
            city.selectField(ResidentialZone.class);
        });
        CustomButton road = getButtonByName("road", actionButtons);
        road.setFunc(() -> {
            city.build(Road.class);
        });
        CustomButton stadium = getButtonByName("stadium", actionButtons);
        stadium.setFunc(() -> {
            city.build(Stadium.class);
        });
        CustomButton police = getButtonByName("police", actionButtons);
        police.setFunc(() -> {
            city.build(PoliceStation.class);
        });
        CustomButton fireStation = getButtonByName("firestation", actionButtons);
        fireStation.setFunc(() -> {
            city.build(FireStation.class);
        });
        CustomButton forest = getButtonByName("forest", actionButtons);
        forest.setFunc(() -> {
            city.build(Forest.class);
        });
        CustomButton firefighting = getButtonByName("firefighting", actionButtons);
        firefighting.setFunc(() -> {
            city.fireFighting();
        });
        CustomButton delete = getButtonByName("delete", actionButtons);
        delete.setFunc(() -> {
            gameArea.tryDenominateOrDestroyZone();
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

        if (currentMoney < zonePrice) {
            industrial.disable();
            service.disable();
            residental.disable();
        } else {
            industrial.enable();
            service.enable();
            residental.enable();
        }

        if (currentMoney < roadPrice) {
            road.disable();
        } else {
            road.enable();
        }

        if (currentMoney < stadiumPrice) {
            stadium.disable();
        } else {
            stadium.enable();
        }

        if (currentMoney < policePrice) {
            police.disable();
        } else {
            police.enable();
        }

        if (currentMoney < firestationPrice) {
            fireStation.disable();
        } else {
            fireStation.enable();
        }

        if (currentMoney < forestPrice) {
            forest.disable();
        } else {
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
                if (!field.isFree()) {
                    newX = NewOffsetX + (j * (field.getBuilding().getWidth() + size));
                    newY = NewOffsetY + (i * (field.getBuilding().getHeight() + size));
                    field.getBuilding().changeSize(size);
                    field.getBuilding().setX(newX);
                    field.getBuilding().setY(newY);
                } else {
                    field.changeSize(size);
                    field.setX(newX);
                    field.setY(newY);
                }
            }
        }

    }

    public void changeCellsPositions(int x, int y) {
        Field fields[][] = gameArea.getCity().getFields();
        for (Field[] field1 : fields) {
            for (int j = 0; j < fields[0].length; j++) {
                Field field = field1[j];
                if (!field1[j].isFree()) {
                    field.getBuilding().changeX(x);
                    field.getBuilding().changeY(y);
                } else {
                    field.changeX(x);
                    field.changeY(y);
                }
            }
        }

    }
}
