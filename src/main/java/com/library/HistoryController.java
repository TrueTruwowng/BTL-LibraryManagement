package com.library;

import static com.library.SceneLoader.stage;

public class HistoryController {

    public void onDashboardBtnClick() {
        SceneLoader.handleDashboardButton(stage);
    }

    public void onHistoryBtnClick() {
        SceneLoader.handleHistoryButton(stage);
    }
    public void onGameBtnClick() {
        SceneLoader.handleGameButton(stage);
    }
    public void onSettingsBtnClick() {
        SceneLoader.handleSettingbutton(stage);
    }
    public void onMyCollectionBtnClick() {
        SceneLoader.handleMyCollectionButton(stage);
    }
    public void onLogOutBtnClk() {
        SceneLoader.handleLogoutButton(stage);
    }
}
