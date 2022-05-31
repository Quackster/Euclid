﻿using Euclid.Util;

namespace Euclid.Game
{
    public class RoomTimerManager
    {
        #region Properties

        public long SpeechBubbleDate { get; set; }

        #endregion

        #region Constructors

        public RoomTimerManager()
        {
            Reset();
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Reset all timers to off
        /// </summary>
        public void Reset()
        {
            ResetSpeechBubbleTimer();
        }

        /// <summary>
        /// Reset the speech bubble timer back to -1 which means off
        /// </summary>
        public void ResetSpeechBubbleTimer()
        {
            SpeechBubbleDate = -1;
        }

        /// <summary>
        /// Start the speech bubble timer back to countdown until turn off, which is 15 seconds by default.
        /// </summary>
        public void StartSpeechBubbleTimer()
        {
            SpeechBubbleDate = DateUtil.GetUnixTimestamp() + ValueManager.Instance.GetInt("timer.speech.bubble");
        }

        #endregion
    }
}
