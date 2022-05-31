using log4net;
using System.Reflection;

namespace Euclid.Game
{
    public class PlayerTaskObject : ITaskObject
    {
        private class PlayerAttribute
        {
            public const string TYPING_STATUS = "TYPING_STATUS";
            public const string EFFECT_EXPIRY = "EFFECT_EXPIRY";
        }

        #region Fields

        private static readonly ILog log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        #endregion

        #region Constructor

        public PlayerTaskObject(IEntity entity) : base(entity) { }

        #endregion

        #region Override Properties

        public override bool RequiresTick => true;

        #endregion

        #region Public methods

        public override void OnTick() { }
        public override void OnTickComplete()
        {

            TicksTimer = RoomTaskManager.GetProcessTime(0.5);
        }

        #endregion
    }
}
