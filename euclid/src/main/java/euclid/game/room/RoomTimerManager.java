package euclid.game.room;

import euclid.game.values.ValueManager;
import euclid.util.DateUtil;

public class RoomTimerManager {

    private long speechBubbleDate;

    public RoomTimerManager() {
        reset();
    }

    public long getSpeechBubbleDate() {
        return speechBubbleDate;
    }

    public void setSpeechBubbleDate(long speechBubbleDate) {
        this.speechBubbleDate = speechBubbleDate;
    }

    public void reset() {
        resetSpeechBubbleTimer();
    }

    public void resetSpeechBubbleTimer() {
        speechBubbleDate = -1;
    }

    public void startSpeechBubbleTimer() {
        speechBubbleDate = DateUtil.getUnixTimestamp() + ValueManager.getInstance().getInt("timer.speech.bubble");
    }
}
