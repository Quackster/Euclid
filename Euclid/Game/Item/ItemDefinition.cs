using Euclid.Storage.Database.Data;

namespace Euclid.Game
{
    public class ItemDefinition
    {
        #region Properties

        public ItemDefinitionData Data { get; }

        #endregion

        #region Constructors

        public ItemDefinition(ItemDefinitionData data)
        {
            Data = data;
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Get top height, but any top height below 0 will be returned as 0.
        /// </summary>
        /// <returns></returns>
        public double GetPositiveTopHeight()
        {
            if (Data.Height < 0)
            {
                return 0;
            }

            return Data.Height;
        }

        #endregion
    }
}