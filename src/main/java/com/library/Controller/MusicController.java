package com.library.Controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MusicController {

    private static MusicController instance; // Singleton instance
    private MediaPlayer mediaPlayer;
    private static boolean isMuted = false; // Variable to track mute status

    private MusicController() {
        // Private constructor to enforce Singleton
    }

    // Get the Singleton instance
    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    // Play a music file
    public void playMusic(String musicFilePath) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Stop current music if any
            }

            Media media = new Media(new File(musicFilePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(mediaPlayer.getStartTime())); // Loop music
            mediaPlayer.setMute(isMuted); // Apply mute status when playing
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }

    // Pause the music
    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    // Resume the music
    public void resumeMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
        }
    }

    // Stop the music
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // Mute or unmute the music
    public void toggleMute() {
        if (mediaPlayer != null) {
            isMuted = !isMuted;
            mediaPlayer.setMute(isMuted);
        }
    }

    // Set the mute status explicitly
    public void setMute(boolean mute) {
        isMuted = mute;
        if (mediaPlayer != null) {
            mediaPlayer.setMute(isMuted);
        }
    }

    // Get the mute status
    public boolean isMuted() {
        return isMuted;
    }

    // Adjust volume (value between 0.0 and 1.0)
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    // Check if music is playing
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}