package chire.val.tutorial;

import chire.val.tutorial.asset.Atlas;
import chire.val.tutorial.asset.Audio;
import chire.val.tutorial.core.SceneManager;
import chire.val.tutorial.io.CRFile;
import chire.val.tutorial.io.FileType;
import chire.val.tutorial.scene.GameScene;
import chire.val.tutorial.scene.MenuScene;
import chire.val.tutorial.scene.Scene;
import chire.val.tutorial.scene.SelectorScene;
import javazoom.jl.player.Player;

import java.applet.AudioClip;
import java.awt.*;

public class Vars {
    public static boolean time_stop = false;

    //Scene
    public static Scene menuScene;

    public static Scene gameScene;

    public static Scene selectorScene;

    public static SceneManager sceneManager;


    //Directory
    public static CRFile spritesDirectory = new CRFile("sprites", FileType.internal);

    public static CRFile fontsDirectory = new CRFile("fonts", FileType.internal);

    public static CRFile musicDirectory = new CRFile("music", FileType.internal);

    public static CRFile soundsDirectory = new CRFile("sounds", FileType.internal);


    //Atlas∫ÕImage
    public static Image img_menu_background, img_VS, img_1P, img_2P, img_1P_desc, img_2P_desc, img_gravestone_left,
            img_gravestone_right,img_selector_tip,img_selector_background,img_1P_selector_btn_idle_left,
            img_1P_selector_btn_idle_right,img_1P_selector_btn_down_left,img_1P_selector_btn_down_right,
            img_2P_selector_btn_idle_left,img_2P_selector_btn_idle_right,img_2P_selector_btn_down_left,
            img_2P_selector_btn_down_right,img_peashooter_selector_background_left,img_peashooter_selector_background_right,
            img_sunflower_selector_background_left,img_sunflower_selector_background_right,img_sky,
            img_hills,img_platform_large,img_platform_small,img_1P_cursor,img_2P_cursor;

    // Õ„∂π…‰ ÷
    public static Atlas atlas_peashooter_idle_left, atlas_peashooter_idle_right, atlas_peashooter_run_left,
            atlas_peashooter_run_right, atlas_peashooter_attack_ex_left, atlas_peashooter_attack_ex_right,
            atlas_peashooter_die_left, atlas_peashooter_die_right;

    //¡˙»’ø˚
    public static Atlas atlas_sunflower_idle_left, atlas_sunflower_idle_right, atlas_sunflower_run_left,
            atlas_sunflower_run_right, atlas_sunflower_attack_ex_left, atlas_sunflower_attack_ex_right,
            atlas_sunflower_die_left, atlas_sunflower_die_right;


    public static Image img_pea;

    public static Atlas atlas_pea_break, atlas_sun, atlas_sun_exploed, atlas_sun_ex, atlas_sun_ex_exploed,
            atlas_sun_text, atlas_run_effect, atlas_jump_effect, atlas_land_effect;

    public static Image img_1P_winner, img_2p_winner, img_winner_bar, img_avatar_peashooter, img_avatar_sunflower;

    public static Audio bgm_menu, bgm_game;

    public static Audio ui_switch, ui_confirm, ui_win;

    public static void load(){
        bgm_menu = Audio.create(musicDirectory.child("bgm_menu.mp3"));
        bgm_game = Audio.create(musicDirectory.child("bgm_game.mp3"));

        ui_switch = Audio.create(soundsDirectory.child("ui_switch.wav"));
        ui_confirm = Audio.create(soundsDirectory.child("ui_confirm.wav"));
        ui_win = Audio.create(soundsDirectory.child("ui_win.wav"));

        atlas_peashooter_run_right = new Atlas("peashooter_run", 5);
        atlas_peashooter_run_left = Atlas.flipAtlas(atlas_peashooter_run_right);

        img_menu_background = Atlas.getFileImage(spritesDirectory.child("menu_background.png"));
    }

    public static void init(){
        menuScene = new MenuScene();
        gameScene = new GameScene();
        selectorScene = new SelectorScene();

        sceneManager = new SceneManager();
        sceneManager.set_current_scene(menuScene);
    }
}
