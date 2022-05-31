﻿namespace Euclid.Game
{
    public class RoomPlayer : RoomEntity
    {
        #region Fields

        #endregion

        #region Properties

        public Player Player { get; private set; }

        #endregion

        #region Constructors

        public RoomPlayer(Player player) : base(player)
        {
            Player = player;
            TaskObject = new PlayerTaskObject(player);
        }

        #endregion
    }
}
