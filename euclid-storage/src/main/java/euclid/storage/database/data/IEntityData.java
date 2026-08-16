package euclid.storage.database.data;

public interface IEntityData {
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    String getFigure();
    void setFigure(String figure);
    String getSex();
    void setSex(String sex);
    String getCustomData();
    void setCustomData(String customData);
}
