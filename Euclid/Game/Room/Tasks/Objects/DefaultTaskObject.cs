namespace Euclid.Game
{
    public class DefaultTaskObject : ITaskObject
    {
        #region Constructor

        public DefaultTaskObject(object item) : base(item) { }

        #endregion

        #region Public methods

        public override void OnTick() { }
        public override void OnTickComplete() { }

        #endregion
    }
}
