using Euclid.Storage.Database.Data;

namespace Euclid.Game
{
    public class NavigatorCategory
    {
        #region Fields

        private readonly NavigatorCategoryData categoryData;

        #endregion

        #region Properties

        public NavigatorCategoryData Data
        {
            get { return categoryData; }
        }

        #endregion

        #region Constructors

        public NavigatorCategory(NavigatorCategoryData categoryData)
        {
            this.categoryData = categoryData;
        }

        #endregion

        #region Public methods

        public int GetCurrentVisitors()
        {
            int currentVisitors = 0;

            foreach (Room room in RoomManager.Instance.Rooms.Values)
            {
                if (room.Data.CategoryId == this.categoryData.Id)
                {
                    currentVisitors += room.Data.UsersNow;
                }
            }

            return currentVisitors;
        }

        public int GetMaxVisitors()
        {
            int maxVisitors = 0;

            foreach (Room room in RoomManager.Instance.Rooms.Values)
            {
                if (room.Data.CategoryId == this.categoryData.Id)
                {
                    maxVisitors += room.Data.UsersMax;
                }
            }

            return maxVisitors;
        }

        #endregion
    }
}
