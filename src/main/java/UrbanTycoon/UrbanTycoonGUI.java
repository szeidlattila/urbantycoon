/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UrbanTycoon;

import javax.swing.JFrame;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

/**
 *
 * @author Felhasználó
 */
public class UrbanTycoonGUI {
    private final int FIELDSIZE = 80;
    private final int UIPADDING = 20;
    private final int ACTIONBSIZE = 60;
    private final int CONTROLBSIZE = 50;
    private final int INFOICONSIZE = 36;
    private final int BUTTONPADDING = 10;
    private final String buttonFilePath = "data/graphics/other/buttons/";
    private final String uiFilePath = "data/graphics/other/UIelements/";

    private final JFrame frame;
    private final JLayeredPane layeredPane;
    private final GameEngine gameArea;

    // screen properties
    private final Dimension screenSize;
    private final int screenWidth;
    private final int screenHeight;

    // action buttons
    private final String[] actionButtonNames = { "industrial", "service", "residental", "road", "stadium", "police",
            "firestation", "forest", "firefighting", "delete" };
    private final ArrayList<CustomButton> actionButtons = new ArrayList<>();

    // control buttons
    private final String[] controlButtonNames = { "play", "pause", "accTime", "deaccTime", "taxInc", "taxDec", "info", "load",
            "save" };
    private final ArrayList<CustomButton> controlButtons = new ArrayList<>();

    // info icons
    private final String[] infoIconNames = { "time", "residents", "residentsSat", "selectedSat", "tax", "money" };
    private final ArrayList<CustomButton> infoIcons = new ArrayList<>();
    private static final ArrayList<CustomLabel> infoLabels = new ArrayList<>();

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
        gameArea.setBackground(Color.decode("#BBF38F"));
        gameArea.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(gameArea, Integer.valueOf(0));

        // control buttons
        int panelLength = panelSize(controlButtonNames, CONTROLBSIZE, BUTTONPADDING);
        int panelCenterY = centerPanelPosition(panelLength, screenHeight);
        {
            JPanel controlPanel = new JPanel();
            controlPanel.setOpaque(false);
            controlPanel.setBounds(UIPADDING, panelCenterY, CONTROLBSIZE, panelLength);
            controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
            controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            for (int i = 0; i < controlButtonNames.length; i++) {
                String filePath = buttonFilePath + controlButtonNames[i];
                CustomButton controlButton = new CustomButton(filePath, CONTROLBSIZE, "controlButton",
                        controlButtonNames[i]);
                controlButtons.add(controlButton);
                controlPanel.add(controlButton);
                if (i != (controlButtonNames.length - 1)) {
                    controlPanel.add(Box.createRigidArea(new Dimension(0, BUTTONPADDING)));
                }
            }
            layeredPane.add(controlPanel, Integer.valueOf(1));
        }

        // action buttons
        {
            panelLength = panelSize(actionButtonNames, ACTIONBSIZE, BUTTONPADDING);
            panelCenterY = centerPanelPosition(panelLength, screenHeight);
            int panelX = screenWidth - ACTIONBSIZE - UIPADDING;
            JPanel actionPanel = new JPanel();
            actionPanel.setOpaque(false);
            actionPanel.setBounds(panelX, panelCenterY, ACTIONBSIZE, panelLength);
            actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
            actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            for (int i = 0; i < actionButtonNames.length; i++) {
                String filePath = buttonFilePath + actionButtonNames[i];
                CustomButton actionButton = new CustomButton(filePath, ACTIONBSIZE,
                        "actionButton",
                        actionButtonNames[i]);
                actionButtons.add(actionButton);
                actionPanel.add(actionButton);
                if (i != (actionButtonNames.length - 1)) {
                    actionPanel.add(Box.createRigidArea(new Dimension(0, BUTTONPADDING)));
                }
            }
            layeredPane.add(actionPanel, Integer.valueOf(2));
        }

        // info panel
        {
            CustomPanel infoPanel = new CustomPanel();
            infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTONPADDING, 7));
            int infoPanelX = centerPanelPosition(784, screenWidth);
            infoPanel.setBounds(infoPanelX, UIPADDING, 784, 50);
            infoPanel.setBackground(new Color(0, 0, 0, 0));
            infoPanel.setImage(uiFilePath + "infoBackground");
            for (String infoIconName : infoIconNames) {
                String filePath = buttonFilePath + infoIconName;
                CustomButton iconButton = new CustomButton(filePath, INFOICONSIZE, "infoIcon", infoIconName);
                iconButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                infoIcons.add(iconButton);
                infoPanel.add(iconButton);
                CustomLabel infoLabel = new CustomLabel("No info", 16, "Bold", "left", infoIconName);
                infoLabels.add(infoLabel);
                infoPanel.add(infoLabel);
            }
            layeredPane.add(infoPanel, Integer.valueOf(3));
        }

        setUpControlButtons();
        setUpActionButtons();
        setUpInfoPanel();
        // add the layered pane to the frame and make it visible
        frame.add(layeredPane);
        frame.pack();
        frame.setVisible(true);

    }

    private int panelSize(String[] arr, int buttonSize, int buttonPadding) {
        return (arr.length * buttonSize + (arr.length - 1) * buttonPadding);
    }

    private int centerPanelPosition(int panelSize, int screenSize) {
        return ((screenSize - panelSize) / 2);
    }

    private CustomButton getButtonByName(String name, ArrayList<CustomButton> arr) {
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

    // starting position
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
        load.setFunc(() -> gameArea.initLoad(gameArea.savesList));
        save.setFunc(() -> gameArea.initSave());

    }

    private void setUpActionButtons() {
        City city = gameArea.getCity();
        CustomButton industrial = getButtonByName("industrial", actionButtons);
        industrial.setFunc(() -> {
            city.selectField(IndustrialZone.class);
            checkActionPrice();

        });
        CustomButton service = getButtonByName("service", actionButtons);
        service.setFunc(() -> {
            city.selectField(ServiceZone.class);
            checkActionPrice();

        });
        CustomButton residential = getButtonByName("residental", actionButtons);
        residential.setFunc(() -> {
            city.selectField(ResidentialZone.class);
            checkActionPrice();

        });
        CustomButton road = getButtonByName("road", actionButtons);
        road.setFunc(() -> {
            city.build(Road.class);
            checkActionPrice();

        });
        CustomButton stadium = getButtonByName("stadium", actionButtons);
        stadium.setFunc(() -> {
            city.build(Stadium.class);
            checkActionPrice();
        });
        CustomButton police = getButtonByName("police", actionButtons);
        police.setFunc(() -> {
            city.build(PoliceStation.class);
            checkActionPrice();
        });
        CustomButton fireStation = getButtonByName("firestation", actionButtons);
        fireStation.setFunc(() -> {
            city.build(FireStation.class);
            checkActionPrice();
        });
        CustomButton forest = getButtonByName("forest", actionButtons);
        forest.setFunc(() -> {
            city.build(Forest.class);
            checkActionPrice();
        });
        CustomButton firefighting = getButtonByName("firefighting", actionButtons);
        firefighting.setFunc(() -> {
            city.fireFighting();
        });
        CustomButton delete = getButtonByName("delete", actionButtons);
        delete.setFunc(() -> {
            gameArea.tryDenominateOrDestroyZone();
            checkActionPrice();
        });
    }

    private void setUpInfoPanel() {
        CustomButton tax = getButtonByName("tax", infoIcons);
        tax.setFunc(() -> new PopupInfo(new JFrame(), gameArea.budgetInfo(), "Budget"));
        tax.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    public static void changeLabels(String timeS, int residentsS, int rSatS, String taxS, String moneyS) {
        CustomLabel time = getLabelByType("time", infoLabels);
        CustomLabel residents = getLabelByType("residents", infoLabels);
        CustomLabel residentsSat = getLabelByType("residentsSat", infoLabels);
        //CustomLabel selectedSat = getLabelByType("selectedSat", infoLabels);
        CustomLabel tax = getLabelByType("tax", infoLabels);
        CustomLabel money = getLabelByType("money", infoLabels);
        time.setText(timeS);
        residents.setText(residentsS + "");
        residentsSat.setText(rSatS + "");
        tax.setText(taxS);
        money.setText(moneyS);
    }

    private void checkActionPrice() {
        long currentMoney = getCurrentMoney();
        CustomButton industrial = getButtonByName("industrial", actionButtons);
        CustomButton service = getButtonByName("service", actionButtons);
        CustomButton residental = getButtonByName("residental", actionButtons);
        CustomButton road = getButtonByName("road", actionButtons);
        CustomButton stadium = getButtonByName("stadium", actionButtons);
        CustomButton police = getButtonByName("police", actionButtons);
        CustomButton fireStation = getButtonByName("firestation", actionButtons);
        CustomButton forest = getButtonByName("forest", actionButtons);
        int zonePrice = gameArea.getZONEPRICE();
        int roadPrice = gameArea.getROADPRICE();
        int stadiumPrice = gameArea.getSTADIUMPRICE();
        int policePrice = gameArea.getPOLICESTATIONPRICE();
        int firestationPrice = gameArea.getFIRESTATIONPRICE();
        int forestPrice = gameArea.getFORESTPRICE();

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

    private long getCurrentMoney() {
        return gameArea.getCity().getBudget();
    }
}
