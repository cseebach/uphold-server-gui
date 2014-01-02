package com.sinfoniasolutions.uphold;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.base.Joiner;
import com.sinfoniasolutions.uphold.model.ComputerDetails;
import com.sinfoniasolutions.uphold.model.UpholdModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron Seebach on 12/26/13.
 */
public class UpholdView {

    private final Stage stage;
    private final UpholdModel model;
    private final Skin skin;

    private List<TextButton> computerButtons;

    private TextButton backButton;
    private Label waitingLabel;
    private ScrollPane waitingPane;
    private ScrollPane donePane;
    private Label doneLabel;
    private Table commandTable;


    public UpholdView(UpholdModel model) {
        skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas("uiskin.atlas"));
        stage = new Stage();
        this.model = model;
        Gdx.input.setInputProcessor(stage);
    }

    public void draw() {
        stage.act();
        stage.draw();
    }

    private static float totalWidth(List<? extends Actor> actors) {
        float width = 0;
        for (Actor actor : actors) {
            width += actor.getWidth();
        }
        return width;
    }

    private static float spacing(List<? extends Actor> actors) {
        float extraSpace = Gdx.graphics.getWidth() - totalWidth(actors);
        float spaces = actors.size() + 1f;
        return extraSpace / spaces;
    }

    private void removeAll(List<? extends Actor> actors) {
        for (Actor actor : actors) {
            actor.remove();
        }
    }

    private void setupOverview() {
        if (computerButtons != null && computerButtons.size() > 0) {
            removeAll(computerButtons);
        }

        computerButtons = new ArrayList<TextButton>(model.getComputers().size());

        for (String computer : model.getComputers()) {
            TextButton computerButton = new TextButton(computer, skin);
            computerButtons.add(computerButton);
            stage.addActor(computerButton);
        }
    }

    private void layoutOverview() {
        float buttonSpacing = spacing(computerButtons);
        float x = buttonSpacing;
        for (TextButton button : computerButtons) {
            float desiredX = x;
            float desiredY = (Gdx.graphics.getHeight() - button.getHeight()) / 2f;
            button.setPosition(desiredX - 100, desiredY - 100);
            button.addAction(Actions.alpha(0f));
            button.addAction(
                    Actions.sequence(
                            Actions.delay(desiredX / 10000f),
                            Actions.parallel(
                                    Actions.fadeIn(.5f),
                                    Actions.moveTo(desiredX, desiredY, .5f, Interpolation.exp5Out))));
            x += buttonSpacing + button.getWidth();
        }
    }

    private void callbacksOverview() {
        for (final TextButton button : computerButtons) {
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    goToComputerDetail(button);
                }
            });
        }
    }

    public void goToOverview() {
        //cleanUpDetail();
        setupOverview();
        layoutOverview();
        callbacksOverview();
    }


    public void goToComputerDetail(TextButton clicked) {
        cleanUpOverview(clicked);
        setupDetail(clicked);
        layoutDetail(clicked);
//        callbacksDetail();

    }

    private void setupDetail(TextButton clicked) {
        ComputerDetails details = model.getDetails(clicked.getText().toString());

        String waitingText = Joiner.on("\n").join(details.getTasksWaiting());
        waitingPane = new ScrollPane(new Label(waitingText, skin));
        waitingLabel = new Label("Waiting Tasks:", skin);
        stage.addActor(waitingPane);
        stage.addActor(waitingLabel);

        String doneText = Joiner.on("\n").join(details.getRecentMessages());
        donePane = new ScrollPane(new Label(doneText, skin));
        doneLabel = new Label("Recent Messages:", skin);
        stage.addActor(donePane);
        stage.addActor(doneLabel);

        commandTable = new Table(skin);
        commandTable.add(new TextButton("MSI", skin));
        commandTable.add(new TextButton("Putfile", skin));
        commandTable.top();
        commandTable.left();
        stage.addActor(commandTable);
    }

    private void layoutDetail(TextButton clicked) {
        clicked.addAction(
                Actions.moveTo(
                        (Gdx.graphics.getWidth() - clicked.getWidth()) / 2f,
                        Gdx.graphics.getHeight() * (9.5f / 10f), .32f, Interpolation.exp5));

        float thirdScreen = Gdx.graphics.getWidth() / 3f;
        float eightTenths = Gdx.graphics.getHeight() * 8f / 10f;

        waitingPane.setBounds(-3 * thirdScreen, 0, thirdScreen, eightTenths);
        waitingPane.addAction(Actions.moveTo(0, 0, .4f, Interpolation.exp5Out));

        waitingLabel.setPosition(-3 * thirdScreen, eightTenths);
        waitingLabel.addAction(
                Actions.moveTo(0, eightTenths, .4f, Interpolation.exp5Out));

        donePane.setBounds(-2 * thirdScreen, 0, thirdScreen, eightTenths);
        donePane.addAction(Actions.moveTo(thirdScreen, 0, .4f, Interpolation.exp5Out));

        doneLabel.setPosition(-2 * thirdScreen, eightTenths);
        doneLabel.addAction(Actions.moveTo(thirdScreen, eightTenths, .4f, Interpolation.exp5Out));

        commandTable.setBounds(-thirdScreen, 0, thirdScreen, eightTenths);
        commandTable.addAction(Actions.moveTo(2 * thirdScreen, 0, .4f, Interpolation.exp5Out));
    }

    private void cleanUpOverview(TextButton clicked) {
        clicked.setTouchable(Touchable.disabled);
        for (TextButton otherButton : computerButtons) {
            if (otherButton != clicked) {
                otherButton.addAction(
                        Actions.moveBy(Gdx.graphics.getWidth(), 0f, .32f, Interpolation.exp5));
            }
        }
    }
}
